package de.llalon.cinematic.util.collections;

import java.util.*;
import javax.cache.Cache;
import org.jetbrains.annotations.NotNull;

public class CachingIterable<T> implements Iterable<T> {

    private final Iterable<T> delegate;
    private final Cache<String, List<T>> cache;
    private final String cacheKey;

    public CachingIterable(
            @NotNull Iterable<T> delegate, @NotNull Cache<String, List<T>> cache, @NotNull String cacheKey) {

        this.delegate = delegate;
        this.cache = cache;
        this.cacheKey = cacheKey;
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {

        List<T> cached = cache.get(cacheKey);
        if (cached != null) {
            return cached.iterator();
        }

        Iterator<T> delegateIterator = delegate.iterator();
        List<T> buffer = new ArrayList<>();

        return new Iterator<>() {

            private T nextItem;
            private boolean computed = false;
            private boolean finished = false;

            private void computeNext() {
                if (finished) {
                    return;
                }

                if (delegateIterator.hasNext()) {
                    nextItem = delegateIterator.next();
                    buffer.add(nextItem);
                } else {
                    finished = true;

                    // Cache only after full iteration
                    cache.put(cacheKey, new ArrayList<>(buffer));
                    nextItem = null;
                }

                computed = true;
            }

            @Override
            public boolean hasNext() {
                if (!computed) {
                    computeNext();
                }
                return nextItem != null;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                T result = nextItem;
                computed = false;
                nextItem = null;
                return result;
            }
        };
    }
}
