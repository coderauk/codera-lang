package uk.co.bssd.hank;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

public class Announcer<T> {

	public static interface ExceptionHandler {
		void onException(Throwable e);
	}

	private static final class DoNothingExceptionHandler implements
			ExceptionHandler {
		public void onException(Throwable e) {
			// do nothing
		}
	}

	public static final ExceptionHandler EXCEPTION_HANDLER_DO_NOTHING = new DoNothingExceptionHandler();

	private final T proxy;
	private final List<T> listeners = new ArrayList<T>();

	private ExceptionHandler exceptionHandler = EXCEPTION_HANDLER_DO_NOTHING;

	private Announcer(Class<? extends T> listenerType) {
		this.proxy = listenerType.cast(Proxy.newProxyInstance(
				listenerType.getClassLoader(), new Class<?>[] { listenerType },
				new InvocationHandler() {
					public Object invoke(Object proxy, Method method,
							Object[] args) throws Throwable {
						announce(method, args);
						return null;
					}
				}));
	}

	public Announcer<T> addListener(T listener) {
		this.listeners.add(listener);
		return this;
	}

	public Announcer<T> addListeners(Iterable<T> listeners) {
		for (T listener : listeners) {
			addListener(listener);
		}
		return this;
	}

	public Announcer<T> removeListener(T listener) {
		this.listeners.remove(listener);
		return this;
	}

	public Announcer<T> useExceptionHandler(ExceptionHandler handler) {
		this.exceptionHandler = handler;
		return this;
	}

	public T announce() {
		return this.proxy;
	}

	private void announce(Method m, Object[] args) {
		try {
			for (T listener : this.listeners) {
				invokeListener(listener, m, args);
			}
		}
		catch (IllegalAccessException e) {
			throw new IllegalArgumentException("could not invoke listener", e);
		}
	}

	private void invokeListener(T listener, Method method, Object[] args)
			throws IllegalAccessException {
		try {
			method.invoke(listener, args);
		}
		catch (InvocationTargetException e) {
			Throwable cause = e.getCause();

			if (cause instanceof Error) {
				throw (Error) cause;
			}
			this.exceptionHandler.onException(cause);
		}
	}

	public static <T> Announcer<T> to(Class<? extends T> listenerType) {
		return new Announcer<T>(listenerType);
	}
	
	public static <T> Announcer<T> to(Class<? extends T> listenerType, Iterable<T> listeners) {
		return to(listenerType).addListeners(listeners);
	}
}
