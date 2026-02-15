package de.llalon.cinematic.domain;

import de.llalon.cinematic.util.collections.OffsetPagedIterable;
import java.util.stream.Stream;

public class Library extends DomainModel {

    public ClientContext getContext() {
        return super.ctx;
    }

    public Library(ClientContext ctx) {
        super(ctx);
    }

    public Iterable<Movie> movies() {
        return () -> ctx.getRadarrClient().getAllMovies().stream()
                .map(x -> new Movie(ctx, x))
                .iterator();
    }

    public Iterable<Series> series() {
        return () -> ctx.getSonarrClient().getAllSeries().stream()
                .map(x -> new Series(ctx, x))
                .iterator();
    }

    public Iterable<Torrent> torrents() {
        return () -> ctx.getQbittorrentClient().getTorrents().stream()
                .map(x -> new Torrent(ctx, x))
                .iterator();
    }

    public Iterable<Tag> tags() {
        return () -> {
            Stream<Tag> s1 = ctx.getQbittorrentClient().getAllTags().stream().map(x -> new Tag(ctx, x));
            Stream<Tag> s2 = ctx.getRadarrClient().getAllTags().stream().map(x -> new Tag(ctx, x.getLabel()));
            Stream<Tag> s3 = ctx.getSonarrClient().getAllTags().stream().map(x -> new Tag(ctx, x.getLabel()));
            return Stream.concat(Stream.concat(s1, s2), s3).iterator();
        };
    }

    public Iterable<Request> requests() {
        return () -> new OffsetPagedIterable<>((take, skip) -> ctx.getOverseerrClient()
                        .getAllRequests(take, skip, null, null, null)
                        .getResults())
                .stream().map(x -> new Request(ctx, x)).iterator();
    }
}
