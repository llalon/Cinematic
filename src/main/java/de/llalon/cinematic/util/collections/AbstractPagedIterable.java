package de.llalon.cinematic.util.collections;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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

    @Override
    public Spliterator<T> spliterator() {
        return new Spliterator<T>() {
            private int pageIndex = initialPageIndex();
            private Iterator<T> current = Collections.emptyIterator();
            private boolean finished = false;

            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                if (finished) return false;

                if (!current.hasNext()) {
                    List<T> page = fetchPage(pageIndex, pageSize);
                    if (page == null || page.isEmpty()) {
                        finished = true;
                        return false;
                    }
                    pageIndex = nextPageIndex(pageIndex, page.size());
                    current = page.iterator();
                }

                if (current.hasNext()) {
                    action.accept(current.next());
                    return true;
                }
                return false;
            }

            @Override
            public Spliterator<T> trySplit() {
                return null; // Sequential paging, not splittable
            }

            @Override
            public long estimateSize() {
                return Long.MAX_VALUE; // Unknown total size
            }

            @Override
            public int characteristics() {
                return Spliterator.ORDERED | Spliterator.NONNULL;
            }
        };
    }

    public Stream<T> stream() {
        return StreamSupport.stream(spliterator(), false);
    }

    protected int initialPageIndex() {
        return 0;
    } // override for 1-based page numbering

    protected int nextPageIndex(int currentPageIndex, int lastPageSize) {
        return currentPageIndex + 1;
    } // override for offset style

    // Deprecated `map` removed in favor of using the standard Stream API via `stream().map(...)`.

    public List<T> toList() {
        List<T> result = new ArrayList<>();
        this.iterator().forEachRemaining(result::add);
        return Collections.unmodifiableList(result);
    }
}
