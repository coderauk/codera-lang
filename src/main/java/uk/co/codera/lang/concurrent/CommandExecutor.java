package uk.co.codera.lang.concurrent;

/**
 * <p>
 * Indicates that this task wraps a command and will execute the command when it
 * is invoked.
 * </p>
 * 
 * @author andystewart
 */
public interface CommandExecutor extends Task {

    /**
     * Returns the underlying command the task will execute when invoked.
     * 
     * @return the underlying command.
     */
    Command getCommand();
}