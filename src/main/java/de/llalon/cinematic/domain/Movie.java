package de.llalon.cinematic.domain;

import static de.llalon.cinematic.domain.ClientContext.getInstance;

import de.llalon.cinematic.client.qbittorrent.dto.TorrentInfo;
import de.llalon.cinematic.client.radarr.dto.MovieResource;
import de.llalon.cinematic.client.radarr.dto.QueueResource;
import de.llalon.cinematic.client.radarr.dto.TagResource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

/**
 * Rich domain model representing a Movie composed from Radarr data.
 *
 * Provides convenience methods that use the available clients to enrich
 * the model with related data (e.g. associated torrents).
 */
@RequiredArgsConstructor
public class Movie {

    @Delegate
    private final MovieResource movieResource;

    public static List<Movie> fetchAll() {
        return getInstance().getRadarrClient().getAllMovies().stream()
                .map(Movie::new)
                .toList();
    }

    public static Movie fetchOne(String id) {
        return new Movie(getInstance().getRadarrClient().getMovie(Integer.parseInt(id)));
    }

    public List<TagResource> fetchTags() {
        return movieResource.getTags().stream()
                .map(t -> getInstance().getRadarrClient().getTag(t))
                .toList();
    }

    public List<Torrent> fetchTorrents() {
        List<QueueResource> queueItems = getInstance().getRadarrClient().getQueueForMovie(this.getId());
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
        for (QueueResource q : queueItems) {
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
