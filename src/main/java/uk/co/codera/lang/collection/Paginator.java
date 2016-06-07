package uk.co.codera.lang.collection;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Paginator {

    public static <T> List<T> paginate(List<T> original, int size, int offset) {
        return original.stream().skip(offset * size).limit(size).collect(Collectors.toCollection(ArrayList::new));
    }
}