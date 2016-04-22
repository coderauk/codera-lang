package uk.co.codera.lang.concurrent;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

public class SequencedPriorityExecutorTest {

    private static final long DEFAULT_JOB_TIMEOUT = 1000L;

    private Executor executor;

    @Before
    public void before() {
        Comparator<Runnable> comparator = ClassPriorityComparator.<Runnable> aClassPriorityComparator().build();
        this.executor = SequencedPriorityExecutor.singleThreadedExecutor(comparator);
    }

    @Test
    public void shouldExecuteJob() {
        Runnable job = mock(Runnable.class);
        execute(job);
        verifyJobRunWithinDefaultTimeout(job);
    }

    @Test
    public void shouldExecuteJobsInOrderSubmitted() {
        Runnable job1 = mock(Runnable.class);
        Runnable job2 = mock(Runnable.class);
        InOrder order = inOrder(job1, job2);

        execute(job1, job2);

        waitForAllJobsToCompleteWithinDefaultTimeout();
        verifyJobsRunInOrder(order, job1, job2);
    }

    @Test
    public void shouldExecuteJobsOfSamePriorityInOrderSubmitted() {
        Runnable[] jobs = mockMany(HighPriorityJob.class, 100);
        InOrder order = inOrder((Object[]) jobs);

        execute(jobs);

        waitForAllJobsToCompleteWithinDefaultTimeout();
        verifyJobsRunInOrder(order, jobs);
    }

    private <R extends Runnable> Runnable[] mockMany(Class<R> clazz, int number) {
        Runnable[] jobs = new Runnable[number];
        for (int i = 0, max = number; i < max; i++) {
            jobs[i] = mock(clazz);
        }
        return jobs;
    }

    private void waitForAllJobsToCompleteWithinDefaultTimeout() {
        waitForAllJobsToComplete(DEFAULT_JOB_TIMEOUT);
    }

    private void waitForAllJobsToComplete(long timeout) {
        AwaitableJob command = new AwaitableJob();
        execute(command);
        assertThat(command.waitForJobToCompleteWithinTimeout(timeout), is(true));
    }

    private void execute(Runnable... commands) {
        for (Runnable command : commands) {
            execute(command);
        }
    }

    private void execute(Runnable command) {
        this.executor.execute(command);
    }

    private void verifyJobsRunInOrder(InOrder order, Runnable... jobs) {
        for (Runnable job : jobs) {
            order.verify(job).run();
        }
    }

    private void verifyJobRunWithinDefaultTimeout(Runnable job) {
        verifyJobRunWithinTimeout(job, DEFAULT_JOB_TIMEOUT);
    }

    private void verifyJobRunWithinTimeout(Runnable job, long timeout) {
        verify(job, timeout(timeout)).run();
    }

    private interface HighPriorityJob extends Runnable {

    }

    private static class AwaitableJob implements Runnable {
        private final CountDownLatch latch;

        private AwaitableJob() {
            this.latch = new CountDownLatch(1);
        }

        @Override
        public void run() {
            this.latch.countDown();
        }

        public boolean waitForJobToCompleteWithinTimeout(long timeout) {
            try {
                return this.latch.await(timeout, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                return false;
            }
        }
    }

    private static class BlockingJob implements Runnable {
        private final CountDownLatch latch;

        private BlockingJob() {
            this.latch = new CountDownLatch(1);
        }

        @Override
        public void run() {
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