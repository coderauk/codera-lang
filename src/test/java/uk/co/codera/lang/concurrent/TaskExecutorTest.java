package uk.co.codera.lang.concurrent;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.util.Comparator;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;

import uk.co.codera.lang.concurrent.Tasks.AbstractTask;

public class TaskExecutorTest {

    private static final long DEFAULT_TIMEOUT = 1000;

    private TaskExecutor taskExecutor;

    @Before
    public void before() {
        Comparator<Runnable> comparator = new Comparator<Runnable>() {
            @Override
            public int compare(Runnable o1, Runnable o2) {
                return priority(o2).compareTo(priority(o1));
            }

            private Integer priority(Runnable o) {
                return 0;
            }
        };
        this.taskExecutor = new TaskExecutor(SequencedPriorityExecutor.singleThreadedExecutor(comparator));
    }

    @Test
    public void shouldExecuteSubmittedTask() {
        Command command = mock(Command.class);
        submit(Tasks.aTask().with(command));
        verify(command, timeout(1000)).execute();
    }

    @Test
    public void shouldShouldCancelTaskItOverakesWithSameCorrelationId() {
        Command cancellableCommand = mock(Command.class);
        Command cancellingCommand = mock(Command.class);

        BlockingCommand blockingCommand = submitBlockingCommand();

        submit(Tasks.aCancellableTask().with(cancellableCommand).correlationId("jeff").sequence(Long.valueOf(1)));
        submit(Tasks.aCancellingTask().with(cancellingCommand).correlationId("jeff").sequence(Long.valueOf(2)));

        blockingCommand.release();

        waitForAllTasksToExecuteWithinDefaultTimeout();

        verify(cancellingCommand).execute();
        verify(cancellableCommand, never()).execute();
    }

    @Test
    public void shouldNotShouldCancelTaskItHasNotOverakenWithSameCorrelationId() {
        Command cancellableCommand = mock(Command.class);
        Command cancellingCommand = mock(Command.class);

        submit(Tasks.aCancellingTask().with(cancellingCommand).correlationId("jeff").sequence(Long.valueOf(1)));
        submit(Tasks.aCancellableTask().with(cancellableCommand).correlationId("jeff").sequence(Long.valueOf(2)));

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

        submit(Tasks.aCancellableTask().with(cancellableCommand1).correlationId("jeff").sequence(Long.valueOf(1)));
        submit(Tasks.aCancellingTask().with(cancellingCommand).correlationId("jeff").sequence(Long.valueOf(2)));
        submit(Tasks.aCancellableTask().with(cancellableCommand2).correlationId("jeff").sequence(Long.valueOf(3)));

        blockingCommand.release();

        waitForAllTasksToExecuteWithinDefaultTimeout();

        verify(cancellingCommand).execute();
        verify(cancellableCommand1, never()).execute();
        verify(cancellableCommand2).execute();
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

    private static class AwaitableCommand implements Command {
        private final CountDownLatch latch;

        private AwaitableCommand() {
            this.latch = new CountDownLatch(1);
        }

        @Override
        public void execute() {
            this.latch.countDown();
        }

        public boolean waitForCommandToCompleteWithinTimeout(long timeout) {
            try {
                return this.latch.await(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                return false;
            }
        }
    }

    private class BlockingCommand implements Command {

        private final CountDownLatch latch;

        public BlockingCommand() {
            this.latch = new CountDownLatch(1);
        }

        @Override
        public void execute() {
            try {
                this.latch.await();
            } catch (InterruptedException e) {
            }
        }

        public void release() {
            this.latch.countDown();
        }
    }
}