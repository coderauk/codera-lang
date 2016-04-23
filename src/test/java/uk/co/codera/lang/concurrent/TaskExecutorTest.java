package uk.co.codera.lang.concurrent;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.util.Comparator;

import org.junit.Before;
import org.junit.Test;

import uk.co.codera.lang.concurrent.Tasks.SimpleTask;

public class TaskExecutorTest {

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

    private void submit(SimpleTask.Builder task) {
        this.taskExecutor.submit(task.build());
    }
}