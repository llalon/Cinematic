package de.llalon.cinematic.util.collections;

import java.util.*;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OffsetPagedIterable<T> extends AbstractPagedIterable<T> {

    private final PageFetcher<T> fetcher;

    @Override
    protected List<T> fetchPage(int pageIndex, int pageSize) {
        return fetcher.fetch(pageSize, pageIndex); // here pageIndex acts as skip
    }

    @Override
    protected int nextPageIndex(int currentPageIndex, int lastPageSize) {
        return currentPageIndex + lastPageSize; // skip += pageSize
    }
}
