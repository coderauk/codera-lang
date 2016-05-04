package uk.co.codera.lang.concurrent;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import static uk.co.codera.lang.concurrent.Tasks.aCancellableTask;
import static uk.co.codera.lang.concurrent.Tasks.aCancellingTask;
import static uk.co.codera.lang.concurrent.Tasks.aTask;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import uk.co.codera.lang.concurrent.Tasks.AbstractTask;
import uk.co.codera.lang.concurrent.TestCommands.AwaitableCommand;
import uk.co.codera.lang.concurrent.TestCommands.BlockingCommand;

public class PriorityTaskExecutorTest {

    private static final long DEFAULT_TIMEOUT = 1000;

    private PriorityTaskExecutor taskExecutor;

    @Before
    public void before() {
        this.taskExecutor = PriorityTaskExecutor.aTaskExecutor().build();
    }

    @Test
    public void shouldExecuteSubmittedTask() {
        Command command = mock(Command.class);
        submit(aTask().with(command));
        verify(command, timeout(1000)).execute();
    }

    @Test
    public void shouldShouldCancelTaskItOverakesWithSameCorrelationId() {
        Command cancellableCommand = mock(Command.class);
        Command cancellingCommand = mock(Command.class);

        BlockingCommand blockingCommand = submitBlockingCommand();

        submit(aCancellableTask().with(cancellableCommand).correlationId("jeff").sequence(Long.valueOf(1)));
        submit(aCancellingTask().with(cancellingCommand).correlationId("jeff").sequence(Long.valueOf(2)));

        blockingCommand.release();

        waitForAllTasksToExecuteWithinDefaultTimeout();

        verify(cancellingCommand).execute();
        verify(cancellableCommand, never()).execute();
    }

    @Test
    public void shouldNotShouldCancelTaskItHasNotOverakenWithSameCorrelationId() {
        Command cancellableCommand = mock(Command.class);
        Command cancellingCommand = mock(Command.class);

        submit(aCancellingTask().with(cancellingCommand).correlationId("jeff").sequence(Long.valueOf(1)));
        submit(aCancellableTask().with(cancellableCommand).correlationId("jeff").sequence(Long.valueOf(2)));

        waitForAllTasksToExecuteWithinDefaultTimeout();

        verify(cancellingCommand).execute();
        verify(cancellableCommand).execute();
    }

    @Test
    public void shouldShouldCancelTaskItOverakesAndProcessSubsequentTaskWithSameCorrelationId() {
        Command cancellableCommand1 = mock(Command.class);
        Command cancellingCommand = mock(Command.class);
        Command cancellableCommand2 = mock(Command.class);

        BlockingCommand blockingCommand = submitBlockingCommand();

        submit(aCancellableTask().with(cancellableCommand1).correlationId("jeff").sequence(Long.valueOf(1)));
        submit(aCancellingTask().with(cancellingCommand).correlationId("jeff").sequence(Long.valueOf(2)));
        submit(aCancellableTask().with(cancellableCommand2).correlationId("jeff").sequence(Long.valueOf(3)));

        blockingCommand.release();

        waitForAllTasksToExecuteWithinDefaultTimeout();

        verify(cancellingCommand).execute();
        verify(cancellableCommand1, never()).execute();
        verify(cancellableCommand2).execute();
    }

    @Test
    public void shouldNotAllowNormalTasksToOvertakeCancellableJobsByDefault() {
        Command cancellableCommand = mock(Command.class);
        Command normalCommand = mock(Command.class);

        InOrder inOrder = inOrder(cancellableCommand, normalCommand);

        BlockingCommand blockingCommand = submitBlockingCommand();

        submit(aCancellableTask().with(cancellableCommand).correlationId("jeff").sequence(Long.valueOf(1)));
        submit(aTask().with(normalCommand));

        blockingCommand.release();

        waitForAllTasksToExecuteWithinDefaultTimeout();

        inOrder.verify(cancellableCommand).execute();
        inOrder.verify(normalCommand).execute();
    }

    @Test
    public void shouldNotAllowNormalTasksToOvertakeCancellingJobsByDefault() {
        Command cancellingCommand = mock(Command.class);
        Command normalCommand = mock(Command.class);

        InOrder inOrder = inOrder(cancellingCommand, normalCommand);

        BlockingCommand blockingCommand = submitBlockingCommand();

        submit(aCancellingTask().with(cancellingCommand).correlationId("jeff").sequence(Long.valueOf(1)));
        submit(aTask().with(normalCommand));

        blockingCommand.release();

        waitForAllTasksToExecuteWithinDefaultTimeout();

        inOrder.verify(cancellingCommand).execute();
        inOrder.verify(normalCommand).execute();
    }

    @Test
    public void shouldAllowNormalTasksToOvertakeCancellableJobsIfSpecifiedAtExecutorBuildTime() {
        this.taskExecutor = PriorityTaskExecutor.aTaskExecutor().allowNormalTasksToOvertakeCancellableTasks().build();

        Command cancellableCommand = mock(Command.class);
        Command normalCommand = mock(Command.class);

        InOrder inOrder = inOrder(normalCommand, cancellableCommand);

        BlockingCommand blockingCommand = submitBlockingCommand();

        submit(aCancellableTask().with(cancellableCommand).correlationId("jeff").sequence(Long.valueOf(1)));
        submit(aTask().with(normalCommand));

        blockingCommand.release();

        waitForAllTasksToExecuteWithinDefaultTimeout();

        inOrder.verify(normalCommand).execute();
        inOrder.verify(cancellableCommand).execute();
    }

    @Test
    public void shouldNotAllowNormalTasksToOvertakeCancellingJobsIfOnlySpecifiedTheyCanOvertakeCancellableJobs() {
        this.taskExecutor = PriorityTaskExecutor.aTaskExecutor().allowNormalTasksToOvertakeCancellableTasks().build();

        Command cancellingCommand = mock(Command.class);
        Command normalCommand = mock(Command.class);

        InOrder inOrder = inOrder(cancellingCommand, normalCommand);

        BlockingCommand blockingCommand = submitBlockingCommand();

        submit(aCancellingTask().with(cancellingCommand).correlationId("jeff").sequence(Long.valueOf(1)));
        submit(aTask().with(normalCommand));

        blockingCommand.release();

        waitForAllTasksToExecuteWithinDefaultTimeout();

        inOrder.verify(cancellingCommand).execute();
        inOrder.verify(normalCommand).execute();
    }

    @Test
    public void shouldAllowNormalTasksToOvertakeCancellingJobsIfSpecifiedAtExecutorBuildTime() {
        this.taskExecutor = PriorityTaskExecutor.aTaskExecutor().allowNormalTasksToOvertakeAllTasks().build();

        Command cancellingCommand = mock(Command.class);
        Command normalCommand = mock(Command.class);

        InOrder inOrder = inOrder(normalCommand, cancellingCommand);

        BlockingCommand blockingCommand = submitBlockingCommand();

        submit(aCancellingTask().with(cancellingCommand).correlationId("jeff").sequence(Long.valueOf(1)));
        submit(aTask().with(normalCommand));

        blockingCommand.release();

        waitForAllTasksToExecuteWithinDefaultTimeout();

        inOrder.verify(normalCommand).execute();
        inOrder.verify(cancellingCommand).execute();
    }

    private void waitForAllTasksToExecuteWithinDefaultTimeout() {
        waitForAllTasksToExecuteWithinTimeout(DEFAULT_TIMEOUT);
    }

    private void waitForAllTasksToExecuteWithinTimeout(long timeout) {
        AwaitableCommand awaitableCommand = submitAwaitableCommand();
        assertThat(awaitableCommand.waitForCommandToCompleteWithinTimeout(timeout), is(true));
    }

    private AwaitableCommand submitAwaitableCommand() {
        AwaitableCommand awaitableCommand = new AwaitableCommand();
        submit(Tasks.aTask().with(awaitableCommand));
        return awaitableCommand;
    }

    private BlockingCommand submitBlockingCommand() {
        BlockingCommand blockingCommand = new BlockingCommand();
        submit(Tasks.aTask().with(blockingCommand));
        return blockingCommand;
    }

    private void submit(AbstractTask.Builder<?> task) {
        this.taskExecutor.submit(task.build());
    }
}