package uk.co.codera.lang.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestTasks {

    private TestTasks() {
        super();
    }

    public static class AwaitableJob implements Runnable {
        private final CountDownLatch latch;

        public AwaitableJob() {
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

    public static class BlockingJob implements Runnable {
        private final CountDownLatch latch;

        public BlockingJob() {
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