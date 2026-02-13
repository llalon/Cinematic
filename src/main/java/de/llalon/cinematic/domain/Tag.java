package de.llalon.cinematic.domain;

public class Tag {

    private final ClientContext context;
    private final String name;

    public Tag(ClientContext context, String name) {
        this.context = context;
        this.name = name;
    }

    public String name() {
        return name;
    }

    public Iterable<Movie> movies() {
        return context.getRadarrClient().getAllMovies().stream()
                .map(x -> new Movie(context, x))
                .toList();
    }

    public Iterable<Series> series() {
        return null;
    }

    public Iterable<Torrent> torrents() {
        return null;
    }
}
