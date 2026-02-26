package de.llalon.cinematic.domain;

import static de.llalon.cinematic.domain.ClientContext.Caches.*;

import de.llalon.cinematic.client.overseerr.dto.MediaRequest;
import de.llalon.cinematic.client.overseerr.dto.OverseerrUser;
import de.llalon.cinematic.client.plex.dto.PlexDirectory;
import de.llalon.cinematic.client.plex.dto.PlexMediaContainerWrapper;
import de.llalon.cinematic.client.plex.dto.PlexMetadataContainer;
import de.llalon.cinematic.client.qbittorrent.dto.TorrentInfo;
import de.llalon.cinematic.client.radarr.dto.MovieResource;
import de.llalon.cinematic.client.radarr.dto.RadarrQueue;
import de.llalon.cinematic.client.radarr.dto.RadarrTag;
import de.llalon.cinematic.client.sonarr.dto.SeriesResource;
import de.llalon.cinematic.client.sonarr.dto.SonarrQueue;
import de.llalon.cinematic.client.sonarr.dto.SonarrTag;
import de.llalon.cinematic.client.tautulli.dto.History;
import de.llalon.cinematic.client.tautulli.dto.TautulliUser;
import de.llalon.cinematic.domain.ClientContext.Caches;
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
import org.jetbrains.annotations.NotNull;

@Slf4j
public abstract class DomainModel {

    protected final ClientContext ctx;

    protected DomainModel(@NotNull ClientContext ctx) {
        this.ctx = ctx;
    }

    @NotNull
    protected Stream<RadarrTag> radarrTags() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getRadarrClient().getAllTags().iterator(), getOrCreateCache(RADARR_TAG), "all"));
    }

    @NotNull
    protected Stream<SonarrTag> sonarrTags() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getSonarrClient().getAllTags().iterator(), getOrCreateCache(SONARR_TAG), "all"));
    }

    @NotNull
    protected Stream<String> qbittorrentTags() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getQbittorrentClient().getAllTags().iterator(), getOrCreateCache(QBITTORRENT_TAG), "all"));
    }

    @NotNull
    protected Stream<TorrentInfo> qbittorrentTorrents() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getQbittorrentClient().getTorrents().iterator(),
                getOrCreateCache(QBITTORRENT_TORRENT),
                "all"));
    }

    @NotNull
    protected Stream<MovieResource> radarrMovies() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getRadarrClient().getAllMovies().iterator(), getOrCreateCache(RADARR_MOVIE), "all"));
    }

    @NotNull
    protected Stream<SeriesResource> sonarrSeries() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getSonarrClient().getAllSeries().iterator(), getOrCreateCache(SONARR_SERIE), "all"));
    }

    @NotNull
    protected Stream<RadarrQueue> radarrQueue() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> new PagePagedIterable<>((take, skip) -> ctx.getRadarrClient()
                                .getQueue(take, skip, false)
                                .getRecords())
                        .iterator(),
                getOrCreateCache(RADARR_QUEUE),
                "all"));
    }

    @NotNull
    protected Stream<SonarrQueue> sonarrQueue() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> new PagePagedIterable<>((take, skip) -> ctx.getSonarrClient()
                                .getQueue(take, skip, false)
                                .getRecords())
                        .iterator(),
                getOrCreateCache(SONARR_QUEUE),
                "all"));
    }

    @NotNull
    protected Stream<MediaRequest> overseerrRequests() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> new OffsetPagedIterable<>((take, skip) -> ctx.getOverseerrClient()
                                .getAllRequests(take, skip, null, null, null)
                                .getResults())
                        .iterator(),
                getOrCreateCache(OVERSEERR_REQUEST),
                "all"));
    }

    @NotNull
    protected Stream<MediaRequest> overseerrRequestsByUser(@NotNull Integer userId) {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> new OffsetPagedIterable<>((take, skip) -> ctx.getOverseerrClient()
                                .getAllRequests(take, skip, null, null, userId)
                                .getResults())
                        .iterator(),
                getOrCreateCache(OVERSEERR_REQUEST),
                "user:" + userId));
    }

    @NotNull
    protected Stream<OverseerrUser> overseerrUsers() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> new OffsetPagedIterable<>((take, skip) -> ctx.getOverseerrClient()
                                .getAllUsers(take, skip, null)
                                .getResults())
                        .iterator(),
                getOrCreateCache(OVERSEERR_USER),
                "all"));
    }

    @NotNull
    protected Stream<TautulliUser> tautulliUsers() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> ctx.getTautulliClient().getUsers().iterator(), getOrCreateCache(TAUTULLI_USER), "all"));
    }

    @NotNull
    protected TautulliUser tautulliUserById(@NotNull Integer userId) {
        return supplyWithCache(
                TAUTULLI_USER, "user:" + userId, () -> ctx.getTautulliClient().getUser(userId));
    }

    @NotNull
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

    @NotNull
    protected PlexMediaContainerWrapper<PlexMetadataContainer> plexSection(@NotNull String key, @NotNull String type) {
        return supplyWithCache(PLEX_SECTION, "section:" + key + ":" + type, () -> ctx.getPlexClient()
                .getSection(key, type, true));
    }

    @NotNull
    protected Stream<History> tautulliHistoryByRatingKey(@NotNull String ratingKey) {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> new OffsetPagedIterable<>((take, skip) -> ctx.getTautulliClient()
                                .getHistoryByRatingKey(ratingKey, skip, take)
                                .getData())
                        .iterator(),
                getOrCreateCache(TAUTULLI_HISTORY),
                "ratingKey:" + ratingKey));
    }

    @NotNull
    protected Stream<History> tautulliHistoryByUser(int userId) {
        return StreamUtils.streamIterator(new CachingIterable<>(
                () -> new OffsetPagedIterable<>((take, skip) -> ctx.getTautulliClient()
                                .getHistoryByUser(userId, skip, take)
                                .getData())
                        .iterator(),
                getOrCreateCache(TAUTULLI_HISTORY),
                "user:" + userId));
    }

    @NotNull
    protected <T> T supplyWithCache(@NotNull Caches caches, @NotNull String key, @NotNull Supplier<T> supplier) {
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

    protected void invalidateCache(@NotNull Caches cache, @NotNull String key) {
        getOrCreateCache(cache).remove(key);
    }

    protected void invalidateCache(@NotNull Caches cache) {
        getOrCreateCache(cache).clear();
    }

    protected <T, V> Cache<T, V> getOrCreateCache(@NotNull Caches cache) {
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
}
