package de.llalon.cinematic.util.collections;

import java.util.List;
import lombok.RequiredArgsConstructor;

/**
 * A {@link AbstractPagedIterable} that pages through results using 1-based page-number pagination.
 *
 * <p>The {@link de.llalon.cinematic.util.collections.PageFetcher} is called with
 * {@code (pageNumber, pageSize)} where {@code pageNumber} starts at 1. Iteration stops
 * when the fetcher returns {@code null}, an empty list, or a page with fewer items
 * than the requested page size (indicating the last page has been reached).</p>
 *
 * @param <T> the element type
 */
@RequiredArgsConstructor
public class PagePagedIterable<T> extends AbstractPagedIterable<T> {

    private final PageFetcher<T> fetcher;
    private boolean lastPageFetched = false; // track if last page returned < pageSize

    /**
     * Constructs a new {@code PagePagedIterable} with the specified page size.
     *
     * @param fetcher supplies pages given a 1-based page number and page size
     * @param pageSize number of elements to request per page
     */
    public PagePagedIterable(PageFetcher<T> fetcher, int pageSize) {
        super(pageSize);
        this.fetcher = fetcher;
    }

    @Override
    protected List<T> fetchPage(int pageIndex, int pageSize) {
        if (lastPageFetched) {
            return List.of(); // stop iteration
        }

        List<T> page = fetcher.fetch(pageIndex + 1, pageSize); // 1-based API
        if (page == null || page.isEmpty() || page.size() < pageSize) {
            lastPageFetched = true; // mark finished after this page
        }
        return page;
    }

    @Override
    protected int initialPageIndex() {
        return 0;
    }

    @Override
    protected int nextPageIndex(int currentPageIndex, int lastPageSize) {
        // just increment normally; stopping is handled in fetchPage()
        return currentPageIndex + 1;
    }
}
