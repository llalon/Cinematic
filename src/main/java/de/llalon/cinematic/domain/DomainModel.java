package de.llalon.cinematic.domain;

import static de.llalon.cinematic.domain.ClientContext.Caches.*;

import de.llalon.cinematic.client.overseerr.dto.MediaRequest;
import de.llalon.cinematic.client.qbittorrent.dto.TorrentInfo;
import de.llalon.cinematic.domain.ClientContext.Caches;
import de.llalon.cinematic.util.collections.CachingIterable;
import de.llalon.cinematic.util.collections.OffsetPagedIterable;
import de.llalon.cinematic.util.collections.PagePagedIterable;
import de.llalon.cinematic.util.collections.StreamUtils;
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

    public Stream<MediaRequest> overseerrRequests() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                new OffsetPagedIterable<>((take, skip) -> ctx.getOverseerrClient()
                        .getAllRequests(take, skip, null, null, null)
                        .getResults()),
                getOrCreateCache(OVERSEERR_REQUESTS),
                "all"));
    }

    public Stream<TorrentInfo> qbittorrentTorrents() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                ctx.getQbittorrentClient().getTorrents(), getOrCreateCache(QBITTORRENT_TORRENT), "all"));
    }

    public Stream<de.llalon.cinematic.client.sonarr.dto.QueueResource> sonarrQueue() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                new PagePagedIterable<>((take, skip) ->
                        ctx.getSonarrClient().getQueue(take, skip, false).getRecords()),
                getOrCreateCache(SONARR_QUEUE),
                "all"));
    }

    public Stream<de.llalon.cinematic.client.radarr.dto.QueueResource> radarrQueue() {
        return StreamUtils.streamIterator(new CachingIterable<>(
                new PagePagedIterable<>((take, skip) ->
                        ctx.getRadarrClient().getQueue(take, skip, false).getRecords()),
                getOrCreateCache(RADARR_QUEUE),
                "all"));
    }

    public void invalidate(@NotNull Caches cache) {
        ctx.getCacheManager().getCache(cache.name()).clear();
    }

    protected <T, V> Cache<T, V> getOrCreateCache(@NotNull Caches cache) {
        final CacheManager manager = ctx.getCacheManager();

        try {
            final Cache<T, V> existing = manager.getCache(cache.name());
            if (existing != null) {
                return existing;
            }
        } catch (NullPointerException ignored) {
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
