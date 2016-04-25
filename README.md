# codera-lang

Helper classes and utilities around core Java concepts such as io, collections, concurrency, etc.

## Concurrency

### PriorityTaskExecutor

## Misc

### Announcer

The Announcer is pretty much a direct copy of the class written by Nat Pryce (http://natpryce.com/articles/000710.html) with a few additions such as the ability to specify an ExceptionHandler.

#### Usage

```
public class GitEventBroadcaster implements GitEventListener {

    private final Announcer<GitEventListener> announcer;

    public GitEventBroadcaster() {
        this.announcer = Announcer.to(GitEventListener.class);
    }

    public void registerListener(GitEventListener listener) {
        this.announcer.addListener(listener);
    }

    @Override
    public void onPush(GitPushEvent event) {
        this.announcer.announce().onPush(event);
    }

    public int numberSubscribers() {
        return this.announcer.numberListeners();
    }
}
```

The above example shows pretty much all the functionality of the announcer.

The announcer is instantiated using the following syntax:

```
this.announcer = Announcer.to(GitEventListener.class);
``` 

This creates an `Announcer` of type `GitEventListener`. It is this instance to which listeners can be added, removed and notified.

Notifying all the listeners is then as simple as:

```
this.announcer.announce().onPush(event);
```

The `announce()` method returns an instance of the interface being proxied which will iterate over all the listeners invoking the appropriate method.

#### Error Handling

By default the announcer is created with a do nothing error handling policy. This means that any listener that fails will not affect execution of any other registered listeners. 

By do nothing we also mean do absolutely nothing, not even log. This is because we consider it bad form to force a particular logging framework onto users of this library. 

Therefore if you wish to log or notify when listeners fail then you should implement your own error handler and register it when building the announcer. 

A simple implementation of an ExceptionHandler might look like:

```
public class LoggingExceptionHandler implements ExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(LoggingExceptionHandler.class);
		
	@Override
	public void onException(Throwable e) {
		logger.error("Exception caught whilst invoking listener", e);
	}
}
```

It is then registered when building the `Announcer`:

```
Announcer<Listener> announcer = Announcer.to(Listener.class).useExceptionHandler(new LoggingExceptionHandler());
```
