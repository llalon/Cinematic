package de.llalon.cinematic;

import de.llalon.cinematic.util.collections.OffsetPagedIterable;
import java.util.List;
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
}
