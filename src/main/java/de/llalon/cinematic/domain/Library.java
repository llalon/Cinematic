package de.llalon.cinematic.domain;

import de.llalon.cinematic.util.collections.OffsetPagedIterable;
import java.util.stream.Stream;

public class Library extends DomainModel {

    /**
     * Returns the client context used by this library.
     *
     * @return the client context
     */
    public ClientContext getContext() {
        return super.ctx;
    }

    /**
     * Creates a new Library instance with the given client context.
     *
     * @param ctx the client context
     */
    public Library(ClientContext ctx) {
        super(ctx);
    }

    /**
     * Returns an iterable of all movies in the library.
     *
     * @return an iterable of Movie objects
     */
    public Iterable<Movie> movies() {
        return () -> ctx.getRadarrClient().getAllMovies().stream()
                .map(x -> new Movie(ctx, x))
                .iterator();
    }

    /**
     * Returns an iterable of all series in the library.
     *
     * @return an iterable of Series objects
     */
    public Iterable<Series> series() {
        return () -> ctx.getSonarrClient().getAllSeries().stream()
                .map(x -> new Series(ctx, x))
                .iterator();
    }

    /**
     * Returns an iterable of all torrents in the library.
     *
     * @return an iterable of Torrent objects
     */
    public Iterable<Torrent> torrents() {
        return () -> ctx.getQbittorrentClient().getTorrents().stream()
                .map(x -> new Torrent(ctx, x))
                .iterator();
    }

    /**
     * Returns an iterable of all tags from various clients (QBittorrent, Radarr, Sonarr).
     *
     * @return an iterable of Tag objects
     */
    public Iterable<Tag> tags() {
        return () -> {
            Stream<Tag> s1 = ctx.getQbittorrentClient().getAllTags().stream().map(x -> new Tag(ctx, x));
            Stream<Tag> s2 = ctx.getRadarrClient().getAllTags().stream().map(x -> new Tag(ctx, x.getLabel()));
            Stream<Tag> s3 = ctx.getSonarrClient().getAllTags().stream().map(x -> new Tag(ctx, x.getLabel()));
            return Stream.concat(Stream.concat(s1, s2), s3).iterator();
        };
    }

    /**
     * Returns an iterable of all requests from Overseerr.
     *
     * @return an iterable of Request objects
     */
    public Iterable<Request> requests() {
        return () -> new OffsetPagedIterable<>((take, skip) -> ctx.getOverseerrClient()
                        .getAllRequests(take, skip, null, null, null)
                        .getResults())
                .stream().map(x -> new Request(ctx, x)).iterator();
    }
}
