package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.radarr.dto.TagResource;
import de.llalon.cinematic.util.collections.OffsetPagedIterable;
import java.util.*;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public class Library extends DomainModel {

    /**
     * Creates a new Library instance with the given configured client context.
     *
     * @param ctx the client context
     */
    public Library(@NotNull ClientContext ctx) {
        super(ctx);
    }

    /**
     * Creates a new Library instance with a default client context created from environment variables.
     */
    public Library() {
        super(ClientContext.builder().build());
    }

    /**
     * Returns the client context used by this library.
     *
     * @return the client context
     */
    @NotNull
    public ClientContext getContext() {
        return super.ctx;
    }

    /**
     * Returns an iterable of all movies in the library.
     *
     * @return an iterable of Movie objects
     */
    @NotNull
    public Iterable<Movie> movies() {
        return () -> super.radarrMovies().map(movie -> new Movie(ctx, movie)).iterator();
    }

    /**
     * Returns an iterable of all series in the library.
     *
     * @return an iterable of Series objects
     */
    @NotNull
    public Iterable<Series> series() {
        return () -> super.sonarrSeries().map(series -> new Series(ctx, series)).iterator();
    }

    /**
     * Returns an iterable of all torrents in the library.
     *
     * @return an iterable of Torrent objects
     */
    @NotNull
    public Iterable<Torrent> torrents() {
        return () -> super.qbittorrentTorrents()
                .map(torrent -> new Torrent(ctx, torrent))
                .iterator();
    }

    /**
     * Returns an iterable of all tags from various clients (QBittorrent, Radarr, Sonarr).
     *
     * @return an iterable of Tag objects
     */
    @NotNull
    public Iterable<Tag> tags() {
        return () -> Stream.concat(
                        Stream.concat(
                                super.radarrTags().map(TagResource::getLabel),
                                super.sonarrTags().map(de.llalon.cinematic.client.sonarr.dto.TagResource::getLabel)),
                        super.qbittorrentTags())
                .map(tag -> new Tag(ctx, tag))
                .iterator();
    }

    /**
     * Returns an iterable of all requests from Overseerr.
     *
     * @return an iterable of Request objects
     */
    @NotNull
    public Iterable<Request> requests() {
        return () -> super.overseerrRequests()
                .map(request -> new Request(ctx, request))
                .iterator();
    }

    /**
     * Returns an iterable of all users, deduplicated by email, from Tautulli and Overseerr.
     *
     * @return an iterable of User objects
     */
    @NotNull
    public Iterable<User> users() {
        return () -> {
            // ToDo: This could be optimized to create user instances from each type of user tautulli/overseerr.
            // This way that information won't have to be fetched later if required.
            final Set<String> userEmails = new HashSet<>();

            final Stream<String> tautulliEmails = ctx.getTautulliClient().getUsers().stream()
                    .map(de.llalon.cinematic.client.tautulli.dto.User::getEmail);
            final Stream<String> overseerrEmails = new OffsetPagedIterable<>((take, skip) -> ctx.getOverseerrClient()
                            .getAllUsers(take, skip, null)
                            .getResults())
                    .stream().map(de.llalon.cinematic.client.overseerr.dto.User::getEmail);
            return Stream.concat(tautulliEmails, overseerrEmails)
                    .filter(email -> {
                        if (email == null || email.isEmpty()) {
                            return false;
                        }

                        boolean duplicate = userEmails.contains(email);
                        userEmails.add(email);

                        return !duplicate;
                    })
                    .map(email -> new User(ctx, email))
                    .iterator();
        };
    }
}
