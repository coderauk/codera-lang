package uk.co.codera.lang.concurrent;

public class Tasks {

    public static SimpleTask.Builder aTask() {
        return SimpleTask.aTask();
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
    public static class SimpleTask implements Task {

        private final Command command;

        private SimpleTask(Builder builder) {
            this.command = builder.command;
        }

        private static Builder aTask() {
            return new Builder();
        }

        @Override
        public void execute() {
            this.command.execute();
        }

        public static class Builder {

            private Command command;

            public Builder with(Command command) {
                this.command = command;
                return this;
            }

            public Task build() {
                return new SimpleTask(this);
            }
        }
    }
}