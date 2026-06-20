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

    protected final ClientContext ctx;

    protected DomainModel(@NonNull ClientContext ctx) {
        this.ctx = ctx;
    }

    @NonNull
    protected Stream<RadarrTag> radarrTags() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getRadarrClient().getAllTags().iterator(), getOrCreateCache(RADARR_TAG), "all"));
    }

    @NonNull
    protected Stream<SonarrTag> sonarrTags() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getSonarrClient().getAllTags().iterator(), getOrCreateCache(SONARR_TAG), "all"));
    }

    @NonNull
    protected Stream<String> qbittorrentTags() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getQbittorrentClient().getAllTags().iterator(), getOrCreateCache(QBITTORRENT_TAG), "all"));
    }

    @NonNull
    protected Stream<QBittorrentInfo> qbittorrentTorrents() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getQbittorrentClient().getTorrents().iterator(),
                getOrCreateCache(QBITTORRENT_TORRENT),
                "all"));
    }

    @NonNull
    protected Stream<MovieResource> radarrMovies() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getRadarrClient().getAllMovies().iterator(), getOrCreateCache(RADARR_MOVIE), "all"));
    }

    @NonNull
    protected Stream<MovieFileResource> radarrMovieFilesByMovie(@NonNull Integer movieId) {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getRadarrClient().getMovieFilesByMovie(movieId).iterator(),
                getOrCreateCache(RADARR_MOVIE),
                "files:movie:" + movieId));
    }

    @NonNull
    protected Stream<SeriesResource> sonarrSeries() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getSonarrClient().getAllSeries().iterator(), getOrCreateCache(SONARR_SERIE), "all"));
    }

    @NonNull
    protected SeriesResource sonarrSeriesById(@NonNull Integer seriesId) {
        return supplyWithCache(
                SONARR_SERIE, "series:" + seriesId, () -> ctx.getSonarrClient().getSeries(seriesId));
    }

    @NonNull
    protected Stream<EpisodeResource> sonarrEpisodesBySeries(@NonNull Integer seriesId) {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getSonarrClient().getEpisodesBySeries(seriesId).iterator(),
                getOrCreateCache(SONARR_EPISODE),
                "series:" + seriesId));
    }

    @NonNull
    protected EpisodeFileResource sonarrEpisodeFile(@NonNull Integer episodeFileId) {
        return supplyWithCache(SONARR_EPISODE_FILE, "episodeFile:" + episodeFileId, () -> ctx.getSonarrClient()
                .getEpisodeFile(episodeFileId));
    }

    @NonNull
    protected Stream<EpisodeFileResource> sonarrEpisodeFilesBySeries(@NonNull Integer seriesId) {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getSonarrClient().getEpisodeFilesBySeries(seriesId).iterator(),
                getOrCreateCache(SONARR_EPISODE_FILE),
                "files:series:" + seriesId));
    }

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

    @NonNull
    protected Stream<TautulliUser> tautulliUsers() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getTautulliClient().getUsers().iterator(), getOrCreateCache(TAUTULLI_USER), "all"));
    }

    @NonNull
    protected TautulliUser tautulliUserById(@NonNull Integer userId) {
        return supplyWithCache(
                TAUTULLI_USER, "user:" + userId, () -> ctx.getTautulliClient().getUser(userId));
    }

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

    @NonNull
    protected PlexMediaContainerWrapper<PlexMetadataContainer> plexSection(@NonNull String key, @NonNull String type) {
        return supplyWithCache(PLEX_SECTION, "section:" + key + ":" + type, () -> ctx.getPlexClient()
                .getSection(key, type, true));
    }

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

    protected void invalidateCache(@NonNull Caches cache, @NonNull String key) {
        getOrCreateCache(cache).remove(key);
    }

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
