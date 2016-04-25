# codera-lang

Helper classes and utilities around core Java concepts such as io, collections, concurrency, etc.

## Concurrency

### PriorityTaskExecutor

The PriorityTaskExecutor has the concept of different types of task, these are the default, cancellable and cancelling. 

The executor is single threaded and will process tasks in the order in which they are submitted with higher priority tasks allowed to jump the queue. 

In the default configuration default and cancellable tasks are the same priority with only cancelling tasks a higher priority.

This is useful in a situation such as an ordering system whereby a "CancelOrder" message should be able to overtake the corresponding "PlaceOrder" message. 

Depending on the configuration of the executor default tasks can either be the same priority as cancellable tasks, overtake cancellable tasks or overtake all types of task.

Allowing default tasks to overtake is useful when they are required to report the status, e.g. what are the current orders? 

> Tasks can only overtake those that have not started processing. Once a lower priority task has begun execution it must complete before the executor asks for the next task.

### Usage

#### Constructing a PriorityTaskExecutor

Constructing a PriorityTaskExecutor with the default configuration whereby default and cancellable tasks have the same priority:

```java
PriorityTaskExecutor taskExecutor = PriorityTaskExecutor.aTaskExecutor().build();
```

Constructing a task executor where normal tasks can overtake cancellable tasks:

```java
PriorityTaskExecutor.aTaskExecutor().allowNormalTasksToOvertakeCancellableTasks().build();
```

Constructing a task executor where normal tasks can overtake both cancellable and cancelling tasks:

```java
PriorityTaskExecutor.aTaskExecutor().allowNormalTasksToOvertakeAllTasks().build();
```

#### Executing a Task

The `Tasks` class is reponsible for constructing a Task of the correct type. Each type of task must at a mininum have the `Command` it is to execute. This is a simple functional interface that has an `execute()` method on it.

Constructing a default task that will execute a "PlaceOrder" command:

```java
public void onMessage(PlaceOrderRequest request) {
  Command command = new PlaceOrderCommand(request);
  Task task = Tasks.aTask().with(command).build();
  this.taskExecutor.submit(task);
}
```

Cancellable and cancelling tasks must also supply the correlationId and sequence for the task. 

The construction is almost identical:

```java
Task placeOrderTask = Tasks.aCancellableTask()
  .with(placeOrderCommand)
  .correlationId(placeOrderRequest.getOrderId())
  .sequence(placeOrderRequest.getTimestamp())
  .build();

Task cancelOrderTask = Tasks.aCancellingTask()
  .with(cancelOrderCommand)
  .correlationId(cancelOrderRequest.getOrderId())
  .sequence(cancelOrderRequest.getTimestamp())
  .build();
```

The correlationId identifies those tasks that should be cancelled when tasks overtake. 

The sequence can be any object that is comparable, for instance a ```Long``` or a ```DateTime```.

When a cancelling task is executed the sequence is recorded against the correlationId. Any subsequent cancellable tasks are executed have their sequence checked. If the sequence is less than that recorded they are not executed. If they are after the cancelling task then the correlationId is cleared and the task executed.

> Note that you should make sure the sequence numbers are allocated correctly to make sure a cancellable task does not clear the status in error and tasks that should have been cancelled get executed by mistak.

## Misc

### Announcer

The Announcer is pretty much a direct copy of the class written by Nat Pryce (http://natpryce.com/articles/000710.html) with a few additions such as the ability to specify an ExceptionHandler.

#### Usage

```java
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

```java
this.announcer = Announcer.to(GitEventListener.class);
``` 

This creates an `Announcer` of type `GitEventListener`. It is this instance to which listeners can be added, removed and notified.

Notifying all the listeners is then as simple as:

```java
this.announcer.announce().onPush(event);
```

The `announce()` method returns an instance of the interface being proxied which will iterate over all the listeners invoking the appropriate method.

#### Error Handling

By default the announcer is created with a do nothing error handling policy. This means that any listener that fails will not affect execution of any other registered listeners. 

By do nothing we also mean do absolutely nothing, not even log. This is because we consider it bad form to force a particular logging framework onto users of this library. 

Therefore if you wish to log or notify when listeners fail then you should implement your own error handler and register it when building the announcer. 

A simple implementation of an ExceptionHandler might look like:

```java
public class LoggingExceptionHandler implements ExceptionHandler {

  private static final Logger logger = LoggerFactory.getLogger(LoggingExceptionHandler.class);
		
  @Override
  public void onException(Throwable e) {
    logger.error("Exception caught whilst invoking listener", e);
  }
}
```

It is then registered when building the `Announcer`:

```java
Announcer<Listener> announcer = 
	Announcer.to(Listener.class).useExceptionHandler(new LoggingExceptionHandler());
```
