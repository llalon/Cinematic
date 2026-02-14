package de.llalon.cinematic.domain;

public class Tag extends DomainModel {

    private final String name;

    public Tag(ClientContext context, String name) {
        super(context);
        this.name = name;
    }

    public String name() {
        return name;
    }

    public Iterable<Movie> movies() {
        return ctx.getRadarrClient().getAllMovies().stream()
                .map(x -> new Movie(ctx, x))
                .toList();
    }

    public Iterable<Series> series() {
        return ctx.getSonarrClient().getAllSeries().stream()
                .map(x -> new Series(ctx, x))
                .toList();
    }

    public Iterable<Torrent> torrents() {
        return ctx.getQbittorrentClient().getTorrents(null, null, this.name).stream()
                .map(x -> new Torrent(ctx, x))
                .toList();
    }
}
