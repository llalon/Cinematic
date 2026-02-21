package de.llalon.cinematic.util.collections;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public enum StreamUtils {
    INSTANCE;

    public static <T> List<T> iterateToList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }

    public static <T> Iterator<T> emptyIterator() {
        final List<T> list = new ArrayList<T>(0);
        return list.stream().iterator();
    }
}
