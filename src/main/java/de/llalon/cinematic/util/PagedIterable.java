package de.llalon.cinematic.util;

import java.util.*;
import java.util.function.Function;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PagedIterable<T> implements Iterable<T> {

    private static final int DEFAULT_PAGE_SIZE = 500;

    @FunctionalInterface
    public interface PageFetcher<T> {
        List<T> fetch(int take, int skip);
    }

    public PagedIterable(PageFetcher<T> pageFetcher) {
        this.pageSize = DEFAULT_PAGE_SIZE;
        this.fetcher = pageFetcher;
    }

    private final int pageSize;
    private final PageFetcher<T> fetcher;

    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {

            private int skip = 0;
            private Iterator<T> current = Collections.emptyIterator();
            private boolean finished = false;

            @Override
            public boolean hasNext() {
                if (finished) return false;

                if (current.hasNext()) return true;

                List<T> page = fetcher.fetch(pageSize, skip);

                if (page == null || page.isEmpty()) {
                    finished = true;
                    return false;
                }

                skip += page.size();
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

    public <R> PagedIterable<R> map(Function<T, R> mapper) {
        return new PagedIterable<>(pageSize, (take, skip) -> {
            List<T> original = fetcher.fetch(take, skip);
            if (original == null || original.isEmpty()) {
                return Collections.emptyList();
            }

            List<R> mapped = new ArrayList<>(original.size());
            for (T item : original) {
                mapped.add(mapper.apply(item));
            }
            return mapped;
        });
    }

    public <R> PagedIterable<R> flatMap(Function<T, ? extends Collection<? extends R>> mapper) {
        return new PagedIterable<>(pageSize, (take, skip) -> {
            List<R> result = new ArrayList<>(take);
            int localSkip = skip;
            int remaining = take;

            while (remaining > 0) {

                List<T> page = fetcher.fetch(pageSize, localSkip);

                if (page == null || page.isEmpty()) {
                    break;
                }

                localSkip += page.size();

                for (T item : page) {
                    Collection<? extends R> mapped = mapper.apply(item);

                    if (mapped == null || mapped.isEmpty()) {
                        continue;
                    }

                    for (R r : mapped) {
                        if (skip > 0) {
                            skip--;
                            continue;
                        }

                        result.add(r);
                        remaining--;

                        if (remaining == 0) {
                            break;
                        }
                    }

                    if (remaining == 0) {
                        break;
                    }
                }
            }

            return result;
        });
    }
}
