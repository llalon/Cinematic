package de.llalon.cinematic.util.collections;

import java.util.List;

@FunctionalInterface
public interface PageFetcher<T> {
    List<T> fetch(int take, int skip);
}
