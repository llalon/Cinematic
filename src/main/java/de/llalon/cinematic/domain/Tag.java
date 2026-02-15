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
        return () -> {
            // find radarr tag id for this label then lazily filter movies
            final Integer tagId = ctx.getRadarrClient().getAllTags().stream()
                .filter(t -> t.getLabel().equals(this.name))
                .findFirst()
                .map(t -> t.getId())
                .orElse(null);

            return ctx.getRadarrClient().getAllMovies().stream()
                .filter(m -> tagId == null || m.getTags().contains(tagId))
                .map(x -> new Movie(ctx, x))
                .iterator();
        };
    }

    public Iterable<Series> series() {
        return () -> {
            final Integer tagId = ctx.getSonarrClient().getAllTags().stream()
                .filter(t -> t.getLabel().equals(this.name))
                .findFirst()
                .map(t -> t.getId())
                .orElse(null);

            return ctx.getSonarrClient().getAllSeries().stream()
                .filter(s -> tagId == null || s.getTags().contains(tagId))
                .map(x -> new Series(ctx, x))
                .iterator();
        };
    }

    public Iterable<Torrent> torrents() {
        return () -> ctx.getQbittorrentClient().getTorrents(null, null, this.name).stream()
            .map(x -> new Torrent(ctx, x))
            .iterator();
    }
}
