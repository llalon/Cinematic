package de.llalon.cinematic;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import de.llalon.cinematic.util.collections.OffsetPagedIterable;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class UtilTests {

    @Test
    void testPagedIterableStreamEquivalence() {
        // Create a mock paged iterable with known data
        OffsetPagedIterable<Integer> iterable = new OffsetPagedIterable<>(
                (take, skip) -> {
                    if (skip >= 10) return List.of(); // End after 10 items
                    List<Integer> page = List.of(skip, skip + 1, skip + 2); // 3 items per page
                    return page.subList(0, Math.min(3, 10 - skip));
                },
                3);

        // Collect using iterator
        List<Integer> fromIterator = iterable.toList();

        // Collect using stream
        List<Integer> fromStream = iterable.stream().collect(Collectors.toList());

        // They should be equal
        Assertions.assertEquals(fromIterator, fromStream);
        Assertions.assertEquals(List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9), fromIterator);
    }

    @Test
    void testSpliteratorCharacteristics() {
        OffsetPagedIterable<Integer> iterable = new OffsetPagedIterable<>((take, skip) -> List.of(1, 2), 2);
        var spliterator = iterable.spliterator();
        assertTrue(spliterator.hasCharacteristics(java.util.Spliterator.ORDERED));
        assertTrue(spliterator.hasCharacteristics(java.util.Spliterator.NONNULL));
        assertFalse(spliterator.hasCharacteristics(java.util.Spliterator.SIZED));
    }

    @Test
    void testEmptyIterable() {
        OffsetPagedIterable<Integer> iterable = new OffsetPagedIterable<>((take, skip) -> List.of(), 1);
        assertTrue(iterable.toList().isEmpty());
        assertTrue(iterable.stream().collect(Collectors.toList()).isEmpty());
    }

    @Test
    void testNullPage() {
        OffsetPagedIterable<Integer> iterable = new OffsetPagedIterable<>((take, skip) -> null, 1);
        assertTrue(iterable.toList().isEmpty());
    }

    @Test
    void testMapDeprecated() {
        OffsetPagedIterable<Integer> iterable = new OffsetPagedIterable<>(
                (take, skip) -> {
                    if (skip >= 4) return List.of();
                    return List.of(skip, skip + 1);
                },
                2);

        var mapped = iterable.stream().map(x -> x * 2);
        List<Integer> result = mapped.toList();
        assertEquals(List.of(0, 2, 4, 6), result);
    }

    @Test
    void testLaziness() {
        AtomicInteger fetchCount = new AtomicInteger(0);
        OffsetPagedIterable<Integer> iterable = new OffsetPagedIterable<>(
                (take, skip) -> {
                    fetchCount.incrementAndGet();
                    return List.of(skip);
                },
                1);

        var iterator = iterable.iterator();
        assertEquals(0, fetchCount.get()); // Not fetched yet
        assertTrue(iterator.hasNext());
        assertEquals(1, fetchCount.get()); // Fetched first page
        assertEquals(0, iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next()); // Fetches second page
        assertEquals(2, fetchCount.get());
    }
}
