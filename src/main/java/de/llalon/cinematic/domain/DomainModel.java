package de.llalon.cinematic.domain;

import static de.llalon.cinematic.domain.DomainModel.Caches.*;

import de.llalon.cinematic.client.plex.dto.PlexDirectory;
import de.llalon.cinematic.client.plex.dto.PlexMediaContainerWrapper;
import de.llalon.cinematic.client.plex.dto.PlexMetadataContainer;
import de.llalon.cinematic.client.qbittorrent.dto.QBittorrentInfo;
import de.llalon.cinematic.client.radarr.dto.MovieFileResource;
import de.llalon.cinematic.client.radarr.dto.MovieResource;
import de.llalon.cinematic.client.radarr.dto.RadarrQueue;
import de.llalon.cinematic.client.radarr.dto.RadarrTag;
import de.llalon.cinematic.client.seerr.dto.MediaRequest;
import de.llalon.cinematic.client.seerr.dto.SeerrUser;
import de.llalon.cinematic.client.sonarr.dto.EpisodeFileResource;
import de.llalon.cinematic.client.sonarr.dto.EpisodeResource;
import de.llalon.cinematic.client.sonarr.dto.SeriesResource;
import de.llalon.cinematic.client.sonarr.dto.SonarrQueue;
import de.llalon.cinematic.client.sonarr.dto.SonarrTag;
import de.llalon.cinematic.client.tautulli.dto.History;
import de.llalon.cinematic.client.tautulli.dto.TautulliUser;
import de.llalon.cinematic.util.collections.CachingIterable;
import de.llalon.cinematic.util.collections.OffsetPagedIterable;
import de.llalon.cinematic.util.collections.PagePagedIterable;
import de.llalon.cinematic.util.collections.StreamUtils;
import java.util.NoSuchElementException;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.cache.configuration.MutableConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;

@Slf4j
abstract class DomainModel {

    /**
     * Cache regions used by domain model helper methods.
     */
    protected enum Caches {
        SONARR_TAG,
        RADARR_TAG,
        QBITTORRENT_TAG,
        SONARR_SERIE,
        SONARR_EPISODE,
        SONARR_EPISODE_FILE,
        RADARR_MOVIE,
        SONARR_USER,
        RADARR_USER,
        QBITTORRENT_TORRENT,
        SONARR_QUEUE,
        RADARR_QUEUE,
        SEERR_REQUEST,
        SEERR_USER,
        TAUTULLI_HISTORY,
        TAUTULLI_USER,
        PLEX_SECTION;
    }

    /**
     * Shared clients and infrastructure used by this domain object.
     */
    protected final ClientContext ctx;

    /**
     * Creates a domain model backed by the given client context.
     *
     * @param ctx configured client context
     */
    protected DomainModel(@NonNull ClientContext ctx) {
        this.ctx = ctx;
    }

    /**
     * Returns cached Radarr tags.
     *
     * @return stream of Radarr tags
     */
    @NonNull
    protected Stream<RadarrTag> radarrTags() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getRadarrClient().getAllTags().iterator(), getOrCreateCache(RADARR_TAG), "all"));
    }

    /**
     * Returns cached Sonarr tags.
     *
     * @return stream of Sonarr tags
     */
    @NonNull
    protected Stream<SonarrTag> sonarrTags() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getSonarrClient().getAllTags().iterator(), getOrCreateCache(SONARR_TAG), "all"));
    }

    /**
     * Returns cached qBittorrent tags.
     *
     * @return stream of qBittorrent tag names
     */
    @NonNull
    protected Stream<String> qbittorrentTags() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getQbittorrentClient().getAllTags().iterator(), getOrCreateCache(QBITTORRENT_TAG), "all"));
    }

    /**
     * Returns cached qBittorrent torrents.
     *
     * @return stream of qBittorrent torrent records
     */
    @NonNull
    protected Stream<QBittorrentInfo> qbittorrentTorrents() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getQbittorrentClient().getTorrents().iterator(),
                getOrCreateCache(QBITTORRENT_TORRENT),
                "all"));
    }

    /**
     * Returns cached Radarr movies.
     *
     * @return stream of Radarr movies
     */
    @NonNull
    protected Stream<MovieResource> radarrMovies() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getRadarrClient().getAllMovies().iterator(), getOrCreateCache(RADARR_MOVIE), "all"));
    }

    /**
     * Returns cached Radarr files for a movie.
     *
     * @param movieId Radarr movie ID
     * @return stream of movie files
     */
    @NonNull
    protected Stream<MovieFileResource> radarrMovieFilesByMovie(@NonNull Integer movieId) {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getRadarrClient().getMovieFilesByMovie(movieId).iterator(),
                getOrCreateCache(RADARR_MOVIE),
                "files:movie:" + movieId));
    }

    /**
     * Returns cached Sonarr series.
     *
     * @return stream of Sonarr series
     */
    @NonNull
    protected Stream<SeriesResource> sonarrSeries() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getSonarrClient().getAllSeries().iterator(), getOrCreateCache(SONARR_SERIE), "all"));
    }

    /**
     * Returns a cached Sonarr series by ID.
     *
     * @param seriesId Sonarr series ID
     * @return the matching series
     */
    @NonNull
    protected SeriesResource sonarrSeriesById(@NonNull Integer seriesId) {
        return supplyWithCache(
                SONARR_SERIE, "series:" + seriesId, () -> ctx.getSonarrClient().getSeries(seriesId));
    }

    /**
     * Returns cached Sonarr episodes for a series.
     *
     * @param seriesId Sonarr series ID
     * @return stream of episodes
     */
    @NonNull
    protected Stream<EpisodeResource> sonarrEpisodesBySeries(@NonNull Integer seriesId) {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getSonarrClient().getEpisodesBySeries(seriesId).iterator(),
                getOrCreateCache(SONARR_EPISODE),
                "series:" + seriesId));
    }

    /**
     * Returns a cached Sonarr episode file by ID.
     *
     * @param episodeFileId Sonarr episode file ID
     * @return the matching episode file
     */
    @NonNull
    protected EpisodeFileResource sonarrEpisodeFile(@NonNull Integer episodeFileId) {
        return supplyWithCache(SONARR_EPISODE_FILE, "episodeFile:" + episodeFileId, () -> ctx.getSonarrClient()
                .getEpisodeFile(episodeFileId));
    }

    /**
     * Returns cached Sonarr episode files for a series.
     *
     * @param seriesId Sonarr series ID
     * @return stream of episode files
     */
    @NonNull
    protected Stream<EpisodeFileResource> sonarrEpisodeFilesBySeries(@NonNull Integer seriesId) {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getSonarrClient().getEpisodeFilesBySeries(seriesId).iterator(),
                getOrCreateCache(SONARR_EPISODE_FILE),
                "files:series:" + seriesId));
    }

    /**
     * Returns cached Radarr queue entries.
     *
     * @return stream of Radarr queue entries
     */
    @NonNull
    protected Stream<RadarrQueue> radarrQueue() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> new PagePagedIterable<>((take, skip) -> ctx.getRadarrClient()
                                .getQueue(take, skip, false)
                                .getRecords())
                        .iterator(),
                getOrCreateCache(RADARR_QUEUE),
                "all"));
    }

    /**
     * Returns cached Sonarr queue entries.
     *
     * @return stream of Sonarr queue entries
     */
    @NonNull
    protected Stream<SonarrQueue> sonarrQueue() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> new PagePagedIterable<>((take, skip) -> ctx.getSonarrClient()
                                .getQueue(take, skip, false)
                                .getRecords())
                        .iterator(),
                getOrCreateCache(SONARR_QUEUE),
                "all"));
    }

    /**
     * Returns cached Seerr requests.
     *
     * @return stream of media requests
     */
    @NonNull
    protected Stream<MediaRequest> seerrRequests() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> new OffsetPagedIterable<>((take, skip) -> ctx.getSeerrClient()
                                .getAllRequests(take, skip, null, null, null)
                                .getResults())
                        .iterator(),
                getOrCreateCache(SEERR_REQUEST),
                "all"));
    }

    /**
     * Returns cached Seerr requests for a user.
     *
     * @param userId Seerr user ID
     * @return stream of media requests for the user
     */
    @NonNull
    protected Stream<MediaRequest> seerrRequestsByUser(@NonNull Integer userId) {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> new OffsetPagedIterable<>((take, skip) -> ctx.getSeerrClient()
                                .getAllRequests(take, skip, null, null, userId)
                                .getResults())
                        .iterator(),
                getOrCreateCache(SEERR_REQUEST),
                "user:" + userId));
    }

    /**
     * Returns cached Seerr users.
     *
     * @return stream of Seerr users
     */
    @NonNull
    protected Stream<SeerrUser> seerrUsers() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> new OffsetPagedIterable<>((take, skip) -> ctx.getSeerrClient()
                                .getAllUsers(take, skip, null)
                                .getResults())
                        .iterator(),
                getOrCreateCache(SEERR_USER),
                "all"));
    }

    /**
     * Returns cached Tautulli users.
     *
     * @return stream of Tautulli users
     */
    @NonNull
    protected Stream<TautulliUser> tautulliUsers() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getTautulliClient().getUsers().iterator(), getOrCreateCache(TAUTULLI_USER), "all"));
    }

    /**
     * Returns a cached Tautulli user by ID.
     *
     * @param userId Tautulli user ID
     * @return the matching Tautulli user
     */
    @NonNull
    protected TautulliUser tautulliUserById(@NonNull Integer userId) {
        return supplyWithCache(
                TAUTULLI_USER, "user:" + userId, () -> ctx.getTautulliClient().getUser(userId));
    }

    /**
     * Returns cached Plex library sections.
     *
     * @return stream of Plex library sections
     */
    @NonNull
    protected Stream<PlexDirectory> plexSections() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getPlexClient()
                        .getSections()
                        .getMediaContainer()
                        .getDirectories()
                        .iterator(),
                getOrCreateCache(PLEX_SECTION),
                "all"));
    }

    /**
     * Returns cached Plex metadata for a library section.
     *
     * @param key Plex library section key
     * @param type Plex media type identifier
     * @return Plex media container wrapper for the section
     */
    @NonNull
    protected PlexMediaContainerWrapper<PlexMetadataContainer> plexSection(@NonNull String key, @NonNull String type) {
        return supplyWithCache(PLEX_SECTION, "section:" + key + ":" + type, () -> ctx.getPlexClient()
                .getSection(key, type, true));
    }

    /**
     * Returns cached Tautulli history for a Plex rating key.
     *
     * @param ratingKey Plex rating key
     * @return stream of watch history entries
     */
    @NonNull
    protected Stream<History> tautulliHistoryByRatingKey(@NonNull String ratingKey) {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> new OffsetPagedIterable<>((take, skip) -> ctx.getTautulliClient()
                                .getHistoryByRatingKey(ratingKey, skip, take)
                                .getData())
                        .iterator(),
                getOrCreateCache(TAUTULLI_HISTORY),
                "ratingKey:" + ratingKey));
    }

    /**
     * Returns cached Tautulli history for a user.
     *
     * @param userId Tautulli user ID
     * @return stream of watch history entries
     */
    @NonNull
    protected Stream<History> tautulliHistoryByUser(int userId) {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> new OffsetPagedIterable<>((take, skip) -> ctx.getTautulliClient()
                                .getHistoryByUser(userId, skip, take)
                                .getData())
                        .iterator(),
                getOrCreateCache(TAUTULLI_HISTORY),
                "user:" + userId));
    }

    /**
     * Returns a cached value or loads and stores it with the supplied function.
     *
     * @param caches cache region to use
     * @param key cache key
     * @param supplier value supplier used on a cache miss
     * @param <T> cached value type
     * @return cached or newly loaded value
     */
    @NonNull
    protected <T> T supplyWithCache(@NonNull Caches caches, @NonNull String key, @NonNull Supplier<T> supplier) {
        final Cache<String, T> cache = getOrCreateCache(caches);

        final T existing = cache.get(key);
        if (existing != null) {
            return existing;
        }

        final T loaded = supplier.get();
        if (loaded == null) {
            throw new NoSuchElementException("Key not found: " + key);
        }

        final T prior = cache.getAndPut(key, loaded);

        return prior != null ? prior : loaded;
    }

    /**
     * Gets an existing cache region or creates it if needed.
     *
     * @param cache cache region
     * @param <T> cache key type
     * @param <V> cache value type
     * @return cache instance for the region
     */
    protected <T, V> Cache<T, V> getOrCreateCache(@NonNull Caches cache) {
        final CacheManager manager = ctx.getCacheManager();

        try {
            final Cache<T, V> existing = manager.getCache(cache.name());
            if (existing != null) {
                return existing;
            }
        } catch (NullPointerException exception) {
            log.trace("npe:", exception);
        }

        try {
            final MutableConfiguration<T, V> config = new MutableConfiguration<>();
            config.setStoreByValue(false);
            log.debug("Creating cache {}", cache);
            return manager.createCache(cache.name(), config);
        } catch (CacheException e) {
            log.debug("Cache creation error: {}", cache, e);
            return manager.getCache(cache.name());
        }
    }

    /**
     * Removes one entry from a cache region.
     *
     * @param cache cache region
     * @param key entry key
     */
    protected void invalidateCache(@NonNull Caches cache, @NonNull String key) {
        getOrCreateCache(cache).remove(key);
    }

    /**
     * Clears one or more cache regions.
     *
     * @param cache cache regions to clear
     */
    protected void invalidateCache(@NonNull Caches... cache) {
        for (Caches value : cache) {
            try {
                this.ctx.getCacheManager().getCache(value.name()).clear();
            } catch (NullPointerException e) {
                log.debug("Cache does not exist");
            } catch (CacheException e) {
                log.error("Error clearing cache: {}", value, e);
            }
        }
    }
}
