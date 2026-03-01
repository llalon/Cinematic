package de.llalon.cinematic.util.collections;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * Abstract base class for lazy page-by-page iteration over paginated API endpoints.
 *
 * <p>Subclasses implement {@link #fetchPage(int, int)} to retrieve individual pages and
 * may override {@link #initialPageIndex()} and {@link #nextPageIndex(int, int)} to adapt
 * to different paging schemes (offset-based or page-number-based).</p>
 *
 * <p>Iteration stops when {@link #fetchPage(int, int)} returns {@code null} or an empty list.
 * The default page size is 500 items per request.</p>
 *
 * @param <T> the element type
 */
public abstract class AbstractPagedIterable<T> implements Iterable<T> {

    private static final int DEFAULT_PAGE_SIZE = 500;

    protected final int pageSize;

    /**
     * Constructs a new iterable using the specified page size.
     *
     * @param pageSize number of elements to request per page
     */
    protected AbstractPagedIterable(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * Constructs a new iterable using the default page size (500).
     */
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

    /**
     * Streams all elements of this iterable sequentially.
     *
     * @return a sequential stream backed by this iterable's spliterator
     */
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

    /**
     * Eagerly collects all elements into an unmodifiable list.
     *
     * @return an unmodifiable list of all elements
     */
    public List<T> toList() {
        List<T> result = new ArrayList<>();
        this.iterator().forEachRemaining(result::add);
        return Collections.unmodifiableList(result);
    }
}
