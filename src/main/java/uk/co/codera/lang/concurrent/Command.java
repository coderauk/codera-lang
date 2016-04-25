package uk.co.codera.lang.concurrent;

/**
 * <p>
 * A command is simply a request to do something. It is not guaranteed that the
 * command will either be executed or successful.
 * </p>
 * <p>
 * The mechanism to execute a command is to create a {@link Task} and schedule
 * it with the {@link PriorityTaskExecutor} for execution.
 * </p>
 * <p>
 * The task is reponsible for determining if the command should be run. The
 * command should only be concerned with the business logic that should be run
 * and not how or when it gets executed.
 * </p>
 * 
 * @author andystewart
 */
@FunctionalInterface
public interface Command {

    void execute();
}