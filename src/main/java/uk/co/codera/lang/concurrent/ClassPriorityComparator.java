package uk.co.codera.lang.concurrent;

import java.util.Comparator;

public class ClassPriorityComparator<T> implements Comparator<T> {

    private ClassPriorityComparator(Builder<T> builder) {

    }

    @Override
    public int compare(T o1, T o2) {
        return 0;
    }

    public static <T> Builder<T> aClassPriorityComparator() {
        return new Builder<>();
    }

    public static class Builder<T> {

        private Builder() {
            super();
        }

        public ClassPriorityComparator<T> build() {
            return new ClassPriorityComparator<T>(this);
        }
    }
}