package uk.co.codera.lang.concurrent;

/**
 * Responsible for constructing the different kind of tasks used by the
 * {@link PriorityTaskExecutor}.
 * 
 * @author andystewart
 */
public class Tasks {

    private Tasks() {
        super();
    }

    public static SimpleTask.Builder aTask() {
        return SimpleTask.aTask();
    }

    public static CancellableTask.Builder aCancellableTask() {
        return CancellableTask.aTask();
    }

    public static CancellingTask.Builder aCancellingTask() {
        return CancellingTask.aTask();
    }

    public abstract static class AbstractTask implements Task {

        private final Command command;

        private AbstractTask(Builder<?> builder) {
            this.command = builder.command;
        }

        @Override
        public void execute() {
            this.command.execute();
        }

        public abstract static class Builder<T> {

            private Command command;

            @SuppressWarnings("unchecked")
            public T with(Command command) {
                this.command = command;
                return (T) this;
            }

            public abstract Task build();
        }
    }

    public abstract static class AbstractCorrelatedTask extends AbstractTask {

        private final Object correlationId;
        private final Comparable<?> sequence;

        private AbstractCorrelatedTask(Builder<?> builder) {
            super(builder);
            this.correlationId = builder.correlationId;
            this.sequence = builder.sequence;
        }

        public Object getCorrelationId() {
            return this.correlationId;
        }

        @SuppressWarnings("squid:S1452")
        public Comparable<?> getSequence() {
            return this.sequence;
        }

        public abstract static class Builder<T> extends AbstractTask.Builder<T> {

            private Object correlationId;
            private Comparable<?> sequence;

            @SuppressWarnings("unchecked")
            public T correlationId(Object correlationId) {
                this.correlationId = correlationId;
                return (T) this;
            }

            @SuppressWarnings("unchecked")
            public T sequence(Comparable<?> sequence) {
                this.sequence = sequence;
                return (T) this;
            }
        }
    }

    /**
     * <p>
     * This is the default kind of task. It has no affect on any tasks it may
     * overtake (if it does indeed overtake them) nor can it be affected by any
     * tasks that may overtake it. The executor will attempt to invoke the
     * command whatever.
     * </p>
     * 
     * @author andystewart
     */
    public static class SimpleTask extends AbstractTask {

        private SimpleTask(Builder builder) {
            super(builder);
        }

        private static Builder aTask() {
            return new Builder();
        }

        public static class Builder extends AbstractTask.Builder<Builder> {

            @Override
            public Task build() {
                return new SimpleTask(this);
            }
        }
    }

    /**
     * <p>
     * This kind of task can be cancelled by a {@link CancellingTask} that has
     * the same correlationId and a sequence that is considered later than that
     * on this task.
     * </p>
     * 
     * @author andystewart
     */
    public static class CancellableTask extends AbstractCorrelatedTask {

        private CancellableTask(Builder builder) {
            super(builder);
        }

        private static Builder aTask() {
            return new Builder();
        }

        public static class Builder extends AbstractCorrelatedTask.Builder<Builder> {

            @Override
            public Task build() {
                return new CancellableTask(this);
            }
        }
    }

    /**
     * <p>
     * This kind of task will cancel any {@link CancellableTask} that has the
     * same correlationId and an earlier sequence to this task.
     * </p>
     * 
     * @author andystewart
     */
    public static class CancellingTask extends AbstractCorrelatedTask {

        private CancellingTask(Builder builder) {
            super(builder);
        }

        private static Builder aTask() {
            return new Builder();
        }

        public static class Builder extends AbstractCorrelatedTask.Builder<Builder> {

            @Override
            public Task build() {
                return new CancellingTask(this);
            }
        }
    }
}