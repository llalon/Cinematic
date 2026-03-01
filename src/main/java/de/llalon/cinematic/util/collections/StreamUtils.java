package de.llalon.cinematic.util.collections;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Utility methods for converting between {@link Iterable}, {@link java.util.Iterator},
 * and {@link java.util.stream.Stream}.
 *
 * <p>This enum acts as a namespace for static helpers; it is not intended to be
 * instantiated or used as an enum value.</p>
 */
public enum StreamUtils {
    INSTANCE;

    /**
     * Eagerly collects all elements of the given {@link Iterable} into an unmodifiable {@link java.util.List}.
     *
     * @param <T> element type
     * @param iterable the source iterable
     * @return a new list containing all elements
     */
    public static <T> List<T> iterateToList(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
    }

    /**
     * Returns an empty {@link java.util.Iterator}.
     *
     * @param <T> element type
     * @return an iterator with no elements
     */
    public static <T> Iterator<T> emptyIterator() {
        final List<T> list = new ArrayList<T>(0);
        return list.stream().iterator();
    }

    /**
     * Wraps an {@link Iterable} in a sequential {@link java.util.stream.Stream}.
     *
     * @param <T> element type
     * @param iterable the source iterable
     * @return a stream backed by the iterable's spliterator
     */
    public static <T> Stream<T> streamIterator(Iterable<T> iterable) {
        return StreamSupport.stream(iterable.spliterator(), false);
    }
}
