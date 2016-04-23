package uk.co.codera.lang.concurrent;

import java.util.concurrent.Executor;

public class TaskExecutor {

    private final Executor executor;

    public TaskExecutor(Executor executor) {
        this.executor = executor;
    }

    public void submit(Task task) {
        this.executor.execute(new TaskRunner(task));
    }

    private static class TaskRunner implements Runnable {

        private final Task task;

        private TaskRunner(Task task) {
            this.task = task;
        }

        @Override
        public void run() {
            this.task.execute();
        }
    }
}