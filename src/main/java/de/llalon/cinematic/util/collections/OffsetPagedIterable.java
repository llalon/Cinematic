package de.llalon.cinematic.util.collections;

import java.util.*;
import lombok.RequiredArgsConstructor;

/**
 * A {@link AbstractPagedIterable} that pages through results using offset-based pagination.
 *
 * <p>The {@link de.llalon.cinematic.util.collections.PageFetcher} is called with
 * {@code (pageSize, offset)} where {@code offset} increases by {@code pageSize} after each page.
 * Iteration stops when the fetcher returns {@code null} or an empty list.</p>
 *
 * @param <T> the element type
 */
@RequiredArgsConstructor
public class OffsetPagedIterable<T> extends AbstractPagedIterable<T> {

    private final PageFetcher<T> fetcher;

    /**
     * Constructs a new {@code OffsetPagedIterable} with the specified page size.
     *
     * @param fetcher supplies pages given a page size and an offset
     * @param pageSize number of elements to request per page
     */
    public OffsetPagedIterable(PageFetcher<T> fetcher, int pageSize) {
        super(pageSize);
        this.fetcher = fetcher;
    }

    @Override
    protected List<T> fetchPage(int pageIndex, int pageSize) {
        return fetcher.fetch(pageSize, pageIndex); // here pageIndex acts as skip
    }

    @Override
    protected int nextPageIndex(int currentPageIndex, int lastPageSize) {
        return currentPageIndex + lastPageSize; // skip += pageSize
    }
}
