package de.llalon.cinematic.util.collections;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public enum StreamUtils {
    INSTANCE;

    public static <T> List<T> iterateToList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }
}
