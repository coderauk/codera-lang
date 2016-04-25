package uk.co.codera.lang.test;

import uk.co.codera.lang.Announcer;
import uk.co.codera.lang.Announcer.ExceptionHandler;

public class AnnouncerExample {

	public interface Listener {
		
	}
	
	public class LoggingExceptionHandler implements ExceptionHandler {

		@Override
		public void onException(Throwable e) {
			logger.error("Exception caught whilst invoking listener", e);
		}
	}
	
	private AnnouncerExample() {
		Announcer<Listener> announcer = Announcer.to(Listener.class).useExceptionHandler(new LoggingExceptionHandler());
		
	}
	
}
