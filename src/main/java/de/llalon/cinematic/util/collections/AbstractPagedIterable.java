package de.llalon.cinematic.util.collections;

import java.util.*;
import java.util.function.Function;

public abstract class AbstractPagedIterable<T> implements Iterable<T> {

    private static final int DEFAULT_PAGE_SIZE = 500;

    protected final int pageSize;

    protected AbstractPagedIterable(int pageSize) {
        this.pageSize = pageSize;
    }

    protected AbstractPagedIterable() {
        this.pageSize = DEFAULT_PAGE_SIZE;
    }

    protected abstract List<T> fetchPage(int pageIndex, int pageSize);

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int pageIndex = initialPageIndex();
            private Iterator<T> current = Collections.emptyIterator();
            private boolean finished = false;

            @Override
            public boolean hasNext() {
                if (finished) return false;

                if (current.hasNext()) return true;

                List<T> page = fetchPage(pageIndex, pageSize);
                if (page == null || page.isEmpty()) {
                    finished = true;
                    return false;
                }

                pageIndex = nextPageIndex(pageIndex, page.size());
                current = page.iterator();
                return current.hasNext();
            }

            @Override
            public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                return current.next();
            }
        };
    }

    protected int initialPageIndex() {
        return 0;
    } // override for 1-based page numbering

    protected int nextPageIndex(int currentPageIndex, int lastPageSize) {
        return currentPageIndex + 1;
    } // override for offset style

    public <R> AbstractPagedIterable<R> map(Function<T, R> mapper) {
        return new AbstractPagedIterable<>(pageSize) {
            @Override
            protected List<R> fetchPage(int pageIndex, int pageSize) {
                List<T> original = AbstractPagedIterable.this.fetchPage(pageIndex, pageSize);
                if (original == null || original.isEmpty()) return Collections.emptyList();
                List<R> mapped = new ArrayList<>(original.size());
                for (T item : original) mapped.add(mapper.apply(item));
                return mapped;
            }

            @Override
            protected int initialPageIndex() {
                return AbstractPagedIterable.this.initialPageIndex();
            }

            @Override
            protected int nextPageIndex(int currentPageIndex, int lastPageSize) {
                return AbstractPagedIterable.this.nextPageIndex(currentPageIndex, lastPageSize);
            }
        };
    }

    public List<T> toList() {
        List<T> result = new ArrayList<>();
        this.iterator().forEachRemaining(result::add);
        return Collections.unmodifiableList(result);
    }
}
