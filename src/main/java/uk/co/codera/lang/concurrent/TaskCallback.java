package uk.co.codera.lang.concurrent;

/**
 * <p>
 * Callback interface used to notify clients about various aspects of the task
 * lifecycle such as whether a task has been executed or cancelled.
 * </p>
 * 
 * @author andystewart
 */
public interface TaskCallback {

    /**
     * Invoked when the task has successfully been executed. If the task throws
     * an exception this method will not be invoked, instead the
     * {@link #onTaskFailure(Task, RuntimeException)} method will be invoked.
     * 
     * @param task
     *            the task which was executed
     */
    <T extends Task> void onTaskExecuted(T task);

    /**
     * If an exception is thrown during the execution of the task then this
     * method will be invoked.
     * 
     * @param task
     *            the task which failed
     * @param e
     *            the exception the task failed with
     */
    <T extends Task> void onTaskFailure(T task, RuntimeException e);

    /**
     * If the task is cancelled because another has overtaken it then this
     * method will be invoked.
     * 
     * @param task
     *            the task which has been cancelled and will no longer be
     *            executed.
     */
    <T extends Task> void onTaskCancelled(T task);
}