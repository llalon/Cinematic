package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.lidarr.dto.LidarrTag;
import de.llalon.cinematic.client.radarr.dto.RadarrTag;
import de.llalon.cinematic.client.seerr.dto.SeerrUser;
import de.llalon.cinematic.client.sonarr.dto.SonarrTag;
import de.llalon.cinematic.client.tautulli.dto.TautulliUser;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

/**
 * Entry point for traversing media, users, requests, torrents, and tags across configured services.
 */
@Slf4j
public class Library extends DomainModel {

    /**
     * Creates a new Library instance with the given configured client context.
     *
     * @param ctx the client context
     */
    public Library(@NonNull ClientContext ctx) {
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
    @NonNull
    public ClientContext getContext() {
        return super.ctx;
    }

    /** Returns all Lidarr artists, fetched lazily and cached. */
    @NonNull
    public Iterable<Artist> artists() {
        return () -> lidarrArtists().map(resource -> new Artist(ctx, resource)).iterator();
    }

    /** Returns all Lidarr albums, fetched lazily and cached. */
    @NonNull
    public Iterable<Album> albums() {
        return () -> lidarrAlbums().map(resource -> new Album(ctx, resource)).iterator();
    }

    /** Returns all Lidarr tracks, fetched lazily and cached. */
    @NonNull
    public Iterable<Track> tracks() {
        return () -> lidarrAlbums()
                .flatMap(album -> lidarrTracksByAlbum(album.getId()))
                .map(resource -> new Track(ctx, resource))
                .iterator();
    }

    /**
     * Returns an iterable of all movies in the library.
     *
     * @return an iterable of Movie objects
     */
    @NonNull
    public Iterable<Movie> movies() {
        return () -> super.radarrMovies().map(movie -> new Movie(ctx, movie)).iterator();
    }

    /**
     * Returns an iterable of all series in the library.
     *
     * @return an iterable of Series objects
     */
    @NonNull
    public Iterable<Series> series() {
        return () -> super.sonarrSeries().map(series -> new Series(ctx, series)).iterator();
    }

    /**
     * Returns an iterable of all episodes in the library.
     *
     * @return an iterable of Episode objects
     */
    @NonNull
    public Iterable<Episode> episodes() {
        return () -> super.sonarrSeries()
                .flatMap(series -> super.sonarrEpisodesBySeries(series.getId()))
                .map(episode -> new Episode(ctx, episode))
                .iterator();
    }

    /**
     * Returns an iterable of all torrents in the library.
     *
     * @return an iterable of Torrent objects
     */
    @NonNull
    public Iterable<Torrent> torrents() {
        return () -> super.qbittorrentTorrents()
                .map(torrent -> new Torrent(ctx, torrent))
                .iterator();
    }

    /**
     * Returns an iterable of all tags from various clients (QBittorrent, Radarr, Sonarr, Lidarr).
     *
     * @return an iterable of Tag objects
     */
    @NonNull
    public Iterable<Tag> tags() {
        return () -> Stream.of(
                        configuredStream(
                                ctx::getLidarrClient, () -> lidarrTags().map(LidarrTag::getLabel)),
                        configuredStream(
                                ctx::getRadarrClient, () -> radarrTags().map(RadarrTag::getLabel)),
                        configuredStream(
                                ctx::getSonarrClient, () -> sonarrTags().map(SonarrTag::getLabel)),
                        configuredStream(ctx::getQbittorrentClient, this::qbittorrentTags))
                .flatMap(stream -> stream)
                .map(tag -> new Tag(ctx, tag))
                .iterator();
    }

    /**
     * Returns an iterable of all requests from Seerr.
     *
     * @return an iterable of Request objects
     */
    @NonNull
    public Iterable<Request> requests() {
        return () ->
                super.seerrRequests().map(request -> new Request(ctx, request)).iterator();
    }

    /**
     * Returns an iterable of all users, deduplicated by email, from Tautulli and Seerr.
     *
     * @return an iterable of User objects
     */
    @NonNull
    public Iterable<User> users() {
        return () -> {
            final Set<String> userEmails = new HashSet<>();

            return Stream.concat(
                            super.seerrUsers().map(SeerrUser::getEmail),
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

    /**
     * Invalidates all caches
     */
    public void invalidateCache() {
        super.invalidateCache(Caches.values());
    }
}
