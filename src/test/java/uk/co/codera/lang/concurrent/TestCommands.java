package uk.co.codera.lang.concurrent;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class TestCommands {

    private TestCommands() {
        super();
    }

    public static class AwaitableCommand implements Command {
        private final CountDownLatch latch;

        public AwaitableCommand() {
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

    public static class BlockingCommand implements Command {

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