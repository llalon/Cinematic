package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.qbittorrent.dto.TorrentInfo;
import de.llalon.cinematic.util.collections.PagePagedIterable;
import java.util.List;
import java.util.Objects;
import lombok.experimental.Delegate;

public class Torrent extends DomainModel {

    @Delegate
    private final TorrentInfo torrentInfo;

    Torrent(ClientContext ctx, TorrentInfo torrentInfo) {
        super(ctx);
        this.torrentInfo = torrentInfo;
    }

    /**
     * Adds a tag to this torrent by name, creating it in qBittorrent if it does not already exist.
     *
     * @param tag the tag name to add; ignored if null or blank
     */
    public void addTag(String tag) {
        if (tag != null && !tag.isBlank()) {
            final String torrentHash = torrentInfo.getHash();
            // Ensure the tag exists, then attach it to this torrent
            ctx.getQbittorrentClient().createTags(List.of(tag));
            ctx.getQbittorrentClient().addTorrentTags(List.of(torrentHash), List.of(tag));
        }
    }

    /**
     * Adds a tag to this torrent.
     *
     * @param tag the Tag domain object whose name will be applied
     */
    public void addTag(Tag tag) {
        addTag(tag.getName());
    }

    /**
     * Returns the series associated with this torrent.
     *
     * <p>Correlates this torrent's hash against the Sonarr download queue via the {@code downloadId}
     * field, then resolves each distinct series.
     *
     * @return an iterable of Series objects
     */
    public Iterable<Series> series() {
        return () -> new PagePagedIterable<>((take, skip) ->
                        ctx.getSonarrClient().getQueue(take, skip, false).getRecords())
                .stream()
                        .filter(queueResource -> queueResource.getDownloadId() != null
                                && queueResource.getDownloadId().equalsIgnoreCase(getHash()))
                        .map(de.llalon.cinematic.client.sonarr.dto.QueueResource::getSeriesId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .map(seriesId -> new Series(ctx, ctx.getSonarrClient().getSeries(seriesId)))
                        .iterator();
    }

    /**
     * Returns the movies associated with this torrent.
     *
     * <p>Correlates this torrent's hash against the Radarr download queue via the {@code downloadId}
     * field, then resolves each distinct movie.
     *
     * @return an iterable of Movie objects
     */
    public Iterable<Movie> movies() {
        return () -> new PagePagedIterable<>((take, skip) ->
                        ctx.getRadarrClient().getQueue(take, skip, false).getRecords())
                .stream()
                        .filter(queueResource -> queueResource.getDownloadId() != null
                                && queueResource.getDownloadId().equalsIgnoreCase(getHash()))
                        .map(de.llalon.cinematic.client.radarr.dto.QueueResource::getMovieId)
                        .filter(Objects::nonNull)
                        .distinct()
                        .map(movieId -> new Movie(ctx, ctx.getRadarrClient().getMovie(movieId)))
                        .iterator();
    }
}
