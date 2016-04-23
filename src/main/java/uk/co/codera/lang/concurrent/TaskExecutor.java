package uk.co.codera.lang.concurrent;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;

import uk.co.codera.lang.concurrent.Tasks.CancellableTask;
import uk.co.codera.lang.concurrent.Tasks.CancellingTask;

public class TaskExecutor {

    private final Executor executor;
    private final ConcurrentMap<Object, Comparable<?>> cancelledTasks;
    private final RunPolicyFactory runPolicies;

    public TaskExecutor(Executor executor) {
        this.executor = executor;
        this.cancelledTasks = new ConcurrentHashMap<>();
        this.runPolicies = new RunPolicyFactory(this.cancelledTasks);
    }

    public void submit(Task task) {
        this.executor.execute(new TaskRunner(runPolicyFor(task), task));
    }

    @SuppressWarnings("unchecked")
    private RunPolicy<Task> runPolicyFor(Task task) {
        return (RunPolicy<Task>) this.runPolicies.policyFor(task);
    }

    private static class TaskRunner implements Runnable {

        private final RunPolicy<Task> runPolicy;
        private final Task task;

        private TaskRunner(RunPolicy<Task> runPolicy, Task task) {
            this.runPolicy = runPolicy;
            this.task = task;
        }

        @Override
        public void run() {
            if (this.runPolicy.shouldRun(this.task)) {
                this.task.execute();
            }
        }
    }

    private class RunPolicyFactory {

        private final ConcurrentMap<Object, Comparable<?>> cancelledTasks;

        private RunPolicyFactory(ConcurrentMap<Object, Comparable<?>> cancelledTasks) {
            this.cancelledTasks = cancelledTasks;
        }

        private RunPolicy<?> policyFor(Task task) {
            Class<? extends Task> clazz = task.getClass();
            if (clazz == CancellableTask.class) {
                return new CheckNotCancelled(this.cancelledTasks);
            } else if (clazz == CancellingTask.class) {
                return new RecordsCancellation(this.cancelledTasks);
            }
            return new AlwaysRun();
        }
    }

    @FunctionalInterface
    private interface RunPolicy<T extends Task> {
        boolean shouldRun(T task);
    }

    private class AlwaysRun implements RunPolicy<Task> {
        @Override
        public boolean shouldRun(Task task) {
            return true;
        }
    }

    private class RecordsCancellation implements RunPolicy<CancellingTask> {

        private final ConcurrentMap<Object, Comparable<?>> cancelledTasks;

        public RecordsCancellation(ConcurrentMap<Object, Comparable<?>> cancelledTasks) {
            this.cancelledTasks = cancelledTasks;
        }

        @Override
        public boolean shouldRun(CancellingTask task) {
            this.cancelledTasks.put(task.getCorrelationId(), task.getSequence());
            return true;
        }
    }

    private class CheckNotCancelled implements RunPolicy<CancellableTask> {

        private final ConcurrentMap<Object, Comparable<?>> cancelledTasks;

        public CheckNotCancelled(ConcurrentMap<Object, Comparable<?>> cancelledTasks) {
            this.cancelledTasks = cancelledTasks;
        }

        @Override
        public boolean shouldRun(CancellableTask task) {
            Object correlationId = task.getCorrelationId();
            if (this.cancelledTasks.containsKey(correlationId)) {
                boolean reachedCancelledSequence = currentTaskSequenceExceededCancelledSequence(task, correlationId);
                if (reachedCancelledSequence) {
                    this.cancelledTasks.remove(correlationId);
                }
                return reachedCancelledSequence;
            }
            return false;
        }

        @SuppressWarnings("unchecked")
        private boolean currentTaskSequenceExceededCancelledSequence(CancellableTask task, Object correlationId) {
            Comparable<?> cancellingSequence = this.cancelledTasks.get(correlationId);
            return ((Comparable<Object>) task.getSequence()).compareTo((Comparable<Object>) cancellingSequence) > 0;
        }
    }
}