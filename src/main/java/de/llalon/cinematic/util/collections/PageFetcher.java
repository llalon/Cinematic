package de.llalon.cinematic.util.collections;

import java.util.List;

@FunctionalInterface
public interface PageFetcher<T> {
    /**
     * Fetches a page of items.
     * <p>
     * The interpretation of parameters depends on the paging strategy:
     * - For offset-based paging (OffsetPagedIterable): take = page size, skip = offset.
     * - For page-based paging (PagePagedIterable): take = page number (1-based), skip = page size.
     * </p>
     * @param take the "take" parameter (page size or page number)
     * @param skip the "skip" parameter (offset or page size)
     * @return the list of items for the page, or empty/null if no more items
     */
    List<T> fetch(int take, int skip);
}
