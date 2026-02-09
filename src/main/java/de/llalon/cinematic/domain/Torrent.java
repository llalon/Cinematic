package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.qbittorrent.dto.TorrentInfo;
import de.llalon.cinematic.client.radarr.dto.MovieResource;
import de.llalon.cinematic.client.radarr.dto.QueueResource;
import de.llalon.cinematic.client.radarr.dto.QueueResourcePagingResource;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

/**
 * Rich domain model representing a Torrent composed from qBittorrent data.
 *
 * Provides convenience method to resolve the Radarr Movie associated with
 * a given torrent by consulting Radarr's download queue.
 */
@RequiredArgsConstructor
public class Torrent {

    @Delegate
    private final TorrentInfo torrentInfo;

    public static List<Torrent> fetchAll() {
        return ClientContextHolder.getQbittorrentClient().getTorrents().stream()
                .map(Torrent::new)
                .toList();
    }

    /**
     * Attempt to find the Radarr movie associated with this torrent by scanning
     * Radarr's queue and correlating the downloadId field with the torrent hash.
     *
     * Uses injected RadarrClient; returns null if not available.
     *
     * @return Movie domain object if found, otherwise null
     */
    public Movie getMovie() {
        // Radarr returns a paginated queue; fetch it and scan for matching downloadId
        QueueResourcePagingResource page = ClientContextHolder.getRadarrClient().getQueue();
        if (page == null || page.getRecords() == null) {
            return null;
        }

        String target = this.getHash().toUpperCase();
        for (QueueResource q : page.getRecords()) {
            if (q == null || q.getDownloadId() == null) continue;
            if (target.equals(q.getDownloadId().toUpperCase())) {
                MovieResource mr = q.getMovie();
                if (mr == null && q.getMovieId() != null) {
                    mr = ClientContextHolder.getRadarrClient().getMovie(q.getMovieId());
                }
                if (mr != null) {
                    return new Movie(mr);
                }
            }
        }

        return null;
    }

    public Series getSeries() {
        // Radarr returns a paginated queue; fetch it and scan for matching downloadId
        var page = ClientContextHolder.getSonarrClient().getQueue();
        if (page == null || page.getRecords() == null) {
            return null;
        }

        String target = this.getHash().toUpperCase();
        for (var q : page.getRecords()) {
            if (q == null || q.getDownloadId() == null) continue;
            if (target.equals(q.getDownloadId().toUpperCase())) {
                var mr = q.getSeries();
                if (mr == null && q.getSeriesId() != null) {
                    mr = ClientContextHolder.getSonarrClient().getSeries(q.getSeriesId());
                }
                if (mr != null) {
                    return new Series(mr);
                }
            }
        }

        return null;
    }
}
