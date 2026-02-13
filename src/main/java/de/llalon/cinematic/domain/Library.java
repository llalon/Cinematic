package de.llalon.cinematic.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Library {
    @Getter
    private final ClientContext context;

    public Iterable<Movie> movies() {
        return null;
    }

    public Iterable<Series> series() {
        return null;
    }

    public Iterable<Torrent> torrents() {
        return null;
    }

    public Iterable<Tag> tags() {
        return null;
    }

    public Iterable<Request> requests() {
        return null;
    }

    public Iterable<User> users() {
        return null;
    }
}
