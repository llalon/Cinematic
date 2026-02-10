package de.llalon.cinematic.domain;

import static de.llalon.cinematic.domain.ClientContext.getInstance;

import de.llalon.cinematic.client.qbittorrent.dto.TorrentInfo;
import de.llalon.cinematic.client.radarr.dto.MovieResource;
import de.llalon.cinematic.client.radarr.dto.QueueResource;
import de.llalon.cinematic.client.radarr.dto.QueueResourcePagingResource;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class Torrent {

    @Delegate
    private final TorrentInfo torrentInfo;

    public static List<Torrent> fetchAll() {
        return getInstance().getQbittorrentClient().getTorrents().stream()
                .map(Torrent::new)
                .toList();
    }

    public Movie fetchMovie() {
        // Radarr returns a paginated queue; fetch it and scan for matching downloadId
        QueueResourcePagingResource page = getInstance().getRadarrClient().getQueue();
        if (page == null || page.getRecords() == null) {
            return null;
        }

        String target = this.getHash().toUpperCase();
        for (QueueResource q : page.getRecords()) {
            if (q == null || q.getDownloadId() == null) continue;
            if (target.equals(q.getDownloadId().toUpperCase())) {
                MovieResource mr = q.getMovie();
                if (mr == null && q.getMovieId() != null) {
                    mr = getInstance().getRadarrClient().getMovie(q.getMovieId());
                }
                if (mr != null) {
                    return new Movie(mr);
                }
            }
        }

        return null;
    }

    public Series fetchSeries() {
        // Radarr returns a paginated queue; fetch it and scan for matching downloadId
        var page = getInstance().getSonarrClient().getQueue();
        if (page == null || page.getRecords() == null) {
            return null;
        }

        String target = this.getHash().toUpperCase();
        for (var q : page.getRecords()) {
            if (q == null || q.getDownloadId() == null) continue;
            if (target.equals(q.getDownloadId().toUpperCase())) {
                var mr = q.getSeries();
                if (mr == null && q.getSeriesId() != null) {
                    mr = getInstance().getSonarrClient().getSeries(q.getSeriesId());
                }
                if (mr != null) {
                    return new Series(mr);
                }
            }
        }

        return null;
    }
}
