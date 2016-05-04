package uk.co.codera.lang.concurrent;

import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.Executor;

import uk.co.codera.lang.concurrent.Tasks.CancellableTask;
import uk.co.codera.lang.concurrent.Tasks.CancellingTask;

/**
 * <p>
 * A task executor that will process tasks in priority order. It allows tasks
 * that overtake to cancel other tasks in the queue.
 * </p>
 * <p>
 * Currently the implementation only allows for serial execution of tasks, i.e.
 * it is single threaded. This is to prevent tasks with the same correlation id
 * being processed concurrently. In future an executor that allows multiple
 * threads whilst keeping tasks with the same correlation id in order may be
 * provided.
 * </p>
 * 
 * @author andystewart
 */
public class PriorityTaskExecutor {

    private final Executor executor;
    private final ConcurrentMap<Object, Comparable<?>> cancelledTasks;
    private final RunPolicyFactory runPolicies;
    private final TaskCallback callback;

    private PriorityTaskExecutor(Builder builder) {
        this.executor = SequencedPriorityExecutor.singleThreadedExecutor(new PriorityTaskComparator(
                builder.normalTaskBehaviour));
        this.cancelledTasks = new ConcurrentHashMap<>();
        this.runPolicies = new RunPolicyFactory(this.cancelledTasks);
        this.callback = builder.callback;
    }

    public static Builder aTaskExecutor() {
        return new Builder();
    }

    public void submit(Task task) {
        TaskRunner taskRunner = new TaskRunner(runPolicyFor(task), task, this.callback);
        this.executor.execute(taskRunner);
    }

    @SuppressWarnings("unchecked")
    private RunPolicy<Task> runPolicyFor(Task task) {
        return (RunPolicy<Task>) this.runPolicies.policyFor(task);
    }

    public static class Builder {

        private NormalTaskBehaviour normalTaskBehaviour = NormalTaskBehaviour.DOES_NOT_OVERTAKE;
        private TaskCallback callback = new TaskCallbackAdapter();

        private Builder() {
            super();
        }

        public Builder allowNormalTasksToOvertakeCancellableTasks() {
            return normalTaskBehaviour(NormalTaskBehaviour.OVERTAKE_CANCELLABLE);
        }

        public Builder allowNormalTasksToOvertakeAllTasks() {
            return normalTaskBehaviour(NormalTaskBehaviour.OVERTAKE_ALL);
        }

        public Builder with(TaskCallback callback) {
            this.callback = callback;
            return this;
        }

        private Builder normalTaskBehaviour(NormalTaskBehaviour behaviour) {
            this.normalTaskBehaviour = behaviour;
            return this;
        }

        public PriorityTaskExecutor build() {
            return new PriorityTaskExecutor(this);
        }
    }

    private enum NormalTaskBehaviour {
        DOES_NOT_OVERTAKE, OVERTAKE_CANCELLABLE, OVERTAKE_ALL;
    }

    private static class PriorityTaskComparator implements Comparator<Runnable> {

        private final NormalTaskBehaviour normalTaskBehaviour;

        public PriorityTaskComparator(NormalTaskBehaviour normalTaskBehaviour) {
            this.normalTaskBehaviour = normalTaskBehaviour;
        }

        @Override
        public int compare(Runnable o1, Runnable o2) {
            return priority(o2).compareTo(priority(o1));
        }

        private Integer priority(Runnable o) {
            Class<?> clazz = ((TaskRunner) o).task.getClass();

            if (clazz == CancellableTask.class) {
                return 0;
            } else if (clazz == CancellingTask.class) {
                return 1;
            }
            return this.normalTaskBehaviour.ordinal();
        }
    }

    private static class TaskRunner implements Runnable {

        private final RunPolicy<Task> runPolicy;
        private final Task task;
        private final TaskCallback callback;

        private TaskRunner(RunPolicy<Task> runPolicy, Task task, TaskCallback callback) {
            this.runPolicy = runPolicy;
            this.task = task;
            this.callback = callback;
        }

        @Override
        public void run() {
            if (this.runPolicy.shouldRun(this.task)) {
                try {
                    this.task.execute();
                    this.callback.onTaskExecuted(this.task);
                } catch (RuntimeException e) {
                    this.callback.onTaskFailure(this.task, e);
                }
            } else {
                this.callback.onTaskCancelled(this.task);
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
            return true;
        }

        @SuppressWarnings("unchecked")
        private boolean currentTaskSequenceExceededCancelledSequence(CancellableTask task, Object correlationId) {
            Comparable<?> cancellingSequence = this.cancelledTasks.get(correlationId);
            return ((Comparable<Object>) task.getSequence()).compareTo((Comparable<Object>) cancellingSequence) > 0;
        }
    }
}