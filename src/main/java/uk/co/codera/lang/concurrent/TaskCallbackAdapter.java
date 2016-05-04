package uk.co.codera.lang.concurrent;

/**
 * <p>
 * Implementation of the {@link TaskCallback} where all the methods have a do
 * nothing implementation.
 * </p>
 * <p>
 * Rather than implement the interface directly clients should extend this class
 * and override behaviour as necessary. This will ensure that if methods are
 * added to the interface in the future client code will still compile as a do
 * nothing implementation will be added to this class.
 * </p>
 * 
 * @author andystewart
 */
public class TaskCallbackAdapter implements TaskCallback {

    @Override
    public <T extends Task> void onTaskExecuted(T task) {
        // do nothing
    }

    @Override
    public <T extends Task> void onTaskFailure(T task, RuntimeException e) {
        // do nothing
    }

    @Override
    public <T extends Task> void onTaskCancelled(T task) {
        // do nothing
    }
}