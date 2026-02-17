package de.llalon.cinematic.util.collections;

import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PagePagedIterable<T> extends AbstractPagedIterable<T> {

    private final PageFetcher<T> fetcher;
    private boolean lastPageFetched = false; // track if last page returned < pageSize

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
