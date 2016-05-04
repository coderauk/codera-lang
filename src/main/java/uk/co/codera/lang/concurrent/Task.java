package uk.co.codera.lang.concurrent;

/**
 * <p>
 * Tasks are the mechanism used to execute commands {@link Command} using the
 * {@link PriorityTaskExecutor}.
 * </p>
 * <p>
 * The TaskExecutor is responsible for determining if the task should be run
 * based on it's type and what other tasks it has executed.
 * </p>
 * 
 * @author andystewart
 */
@FunctionalInterface
public interface Task {

    void execute();
}