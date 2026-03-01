package de.llalon.cinematic.domain;

import static de.llalon.cinematic.domain.DomainModel.Caches.*;

import de.llalon.cinematic.client.overseerr.dto.OverseerrUser;
import de.llalon.cinematic.client.radarr.dto.RadarrTag;
import de.llalon.cinematic.client.sonarr.dto.SonarrTag;
import de.llalon.cinematic.client.tautulli.dto.TautulliUser;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
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
                                super.radarrTags().map(RadarrTag::getLabel),
                                super.sonarrTags().map(SonarrTag::getLabel)),
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
            final Set<String> userEmails = new HashSet<>();

            return Stream.concat(
                            super.overseerrUsers().map(OverseerrUser::getEmail),
                            super.tautulliUsers().map(TautulliUser::getEmail))
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

    public void invalidateCache() {
        super.invalidateCache(Caches.values());
    }
}
