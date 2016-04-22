package uk.co.codera.lang.concurrent;

import java.util.Comparator;
import java.util.concurrent.Executor;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>
 * An implementation of the {@link Executor} that is backed by a
 * {@link PriorityBlockingQueue}.
 * </p>
 * <p>
 * One drawback of the backing queue is jobs with equal priority are not
 * guaranteed to be run in any particular order. This implementation ensures
 * they are run in the order submitted by assigning a sequence number when the
 * job is offered for execution. This is then used in conjunction with the
 * priority to determine which task should be executed next.
 * </p>
 * 
 * @author andystewart
 */
public class SequencedPriorityExecutor implements Executor {

    public static final int INITIAL_QUEUE_CAPACITY = 1000;
    private static final int SINGLE_THREAD = 1;

    private final Executor underlyingExecutor;
    private final AtomicLong sequenceNumber;

    private SequencedPriorityExecutor(int numberThreads, Comparator<Runnable> comparator) {
        this.underlyingExecutor = threadPoolExecutor(numberThreads, comparator);
        this.sequenceNumber = new AtomicLong(0);
    }

    public static Executor singleThreadedExecutor(Comparator<Runnable> comparator) {
        return new SequencedPriorityExecutor(SINGLE_THREAD, comparator);
    }

    @Override
    public void execute(Runnable command) {
        SequencedRunnable sequencedCommand = new SequencedRunnable(command, this.sequenceNumber.getAndIncrement());
        this.underlyingExecutor.execute(sequencedCommand);
    }

    private ThreadPoolExecutor threadPoolExecutor(int numberThreads, Comparator<Runnable> comparator) {
        return new ThreadPoolExecutor(numberThreads, numberThreads, 0L, TimeUnit.MILLISECONDS,
                blockingQueue(comparator));
    }

    private PriorityBlockingQueue<Runnable> blockingQueue(Comparator<Runnable> comparator) {
        return new PriorityBlockingQueue<>(INITIAL_QUEUE_CAPACITY, new SequencedPriorityComparator(comparator));
    }

    private static class SequencedRunnable implements Runnable {
        private final Runnable underlyingRunnable;
        private final Long sequenceNumber;

        private SequencedRunnable(Runnable command, long sequenceNumber) {
            this.underlyingRunnable = command;
            this.sequenceNumber = Long.valueOf(sequenceNumber);
        }

        @SuppressWarnings("squid:S1217")
        @Override
        public void run() {
            this.underlyingRunnable.run();
        }

        public Long getSequenceNumber() {
            return sequenceNumber;
        }
        
        public Runnable getUnderlyingRunnable() {
        	return this.underlyingRunnable;
        }
    }

    private static class SequencedPriorityComparator implements Comparator<Runnable> {

        private final Comparator<Runnable> priorityComparator;

        private SequencedPriorityComparator(Comparator<Runnable> priorityComparator) {
            this.priorityComparator = priorityComparator;
        }

        @Override
        public int compare(Runnable o1, Runnable o2) {
        	SequencedRunnable s1 = (SequencedRunnable)o1;
        	SequencedRunnable s2 = (SequencedRunnable)o2;
            int priority = this.priorityComparator.compare(s1.getUnderlyingRunnable(), s2.getUnderlyingRunnable());
            if (priority == 0) {
                return s1.getSequenceNumber().compareTo(s2.getSequenceNumber());
            }
            return priority;
        }
    }
}