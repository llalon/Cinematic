package de.llalon.cinematic.domain;

import de.llalon.cinematic.util.collections.OffsetPagedIterable;
import java.util.ArrayList;
import java.util.List;

public class Library extends DomainModel {

    public ClientContext getContext() {
        return super.ctx;
    }

    public Library(ClientContext ctx) {
        super(ctx);
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
        return ctx.getQbittorrentClient().getTorrents().stream()
                .map(x -> new Torrent(ctx, x))
                .toList();
    }

    public Iterable<Tag> tags() {
        List<Tag> tags = new ArrayList<>();

        ctx.getQbittorrentClient().getAllTags().forEach(x -> tags.add(new Tag(ctx, x)));
        ctx.getRadarrClient().getAllTags().forEach(x -> tags.add(new Tag(ctx, x.getLabel())));
        ctx.getSonarrClient().getAllTags().forEach(x -> tags.add(new Tag(ctx, x.getLabel())));

        return tags;
    }

    public Iterable<Request> requests() {
        return new OffsetPagedIterable<>((take, skip) -> ctx.getOverseerrClient()
                .getAllRequests(take, skip, null, null, null)
                .getResults())
            .stream()
            .map(x -> new Request(ctx, x))
            .toList();
    }

    public Iterable<User> users() {
        return null;
    }
}
