package de.llalon.cinematic.domain;

import static de.llalon.cinematic.domain.ClientContext.getInstance;

import de.llalon.cinematic.client.qbittorrent.dto.TorrentInfo;
import de.llalon.cinematic.client.sonarr.dto.SeriesResource;
import de.llalon.cinematic.client.sonarr.dto.TagResource;
import java.util.*;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

/**
 * Rich domain model representing a Series composed from Sonarr data.
 *
 * Provides convenience methods that use the available clients to enrich
 * the model with related data (e.g. associated torrents).
 */
@RequiredArgsConstructor
public class Series {

    @Delegate
    private final SeriesResource seriesResource;

    public static List<Series> fetchAll() {
        return getInstance().getSonarrClient().getAllSeries().stream()
                .map(Series::new)
                .toList();
    }

    public boolean hasTag(String tag) {
        if (tag == null || tag.isBlank()) {
            return false;
        }
        return this.fetchTags().stream().anyMatch(x -> x.getLabel().equals(tag));
    }

    public List<TagResource> fetchTags() {
        return seriesResource.getTags().stream()
                .map(t -> getInstance().getSonarrClient().getTag(t))
                .toList();
    }

    /**
     * Fetch torrents associated with this movie by consulting
     * 's queue
     * and correlating download IDs with qBittorrent torrents.
     *
     * This method performs on-demand API calls using injected clients.
     *
     * @return list of associated Torrent domain objects (empty if none)
     */
    public List<Torrent> fetchTorrents() {
        List<de.llalon.cinematic.client.sonarr.dto.QueueResource> queueItems =
                getInstance().getSonarrClient().getQueueForSeries(this.getId());
        if (queueItems == null || queueItems.isEmpty()) {
            return Collections.emptyList();
        }

        // Build a lookup of qBittorrent torrents by hash for fast correlation
        List<TorrentInfo> allTorrents = getInstance().getQbittorrentClient().getTorrents();
        Map<String, TorrentInfo> byHash = new HashMap<>();
        if (allTorrents != null) {
            for (TorrentInfo t : allTorrents) {
                if (t != null && t.getHash() != null) {
                    byHash.put(t.getHash().toUpperCase(), t);
                }
            }
        }

        List<Torrent> result = new ArrayList<>();
        for (var q : queueItems) {
            if (q == null || q.getDownloadId() == null) {
                continue;
            }

            String downloadId = q.getDownloadId().toUpperCase();
            TorrentInfo info = byHash.get(downloadId);
            if (info != null) {
                Torrent t = new Torrent(info);
                result.add(t);
            }
        }

        return result;
    }
}
