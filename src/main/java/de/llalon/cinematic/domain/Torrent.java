package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.qbittorrent.dto.TorrentInfo;
import java.util.List;
import java.util.Objects;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Torrent extends DomainModel {

    @Delegate
    @NotNull
    private final TorrentInfo torrentInfo;

    Torrent(ClientContext ctx, @NotNull TorrentInfo torrentInfo) {
        super(ctx);
        this.torrentInfo = torrentInfo;
    }

    /**
     * Sets the priority of the torrent to the top. Only works if queueing is enabled.
     */
    public void setTopPriority() {
        this.ctx.getQbittorrentClient().setTopPriority(List.of(this.torrentInfo.getHash()));
    }

    /**
     * Sets the priority of the torrent to the bottom. Only works if queueing is enabled.
     */
    public void setBottomPriority() {
        this.ctx.getQbittorrentClient().setBottomPriority(List.of(this.torrentInfo.getHash()));
    }

    /**
     * Adds a tag to this torrent by name, creating it in qBittorrent if it does not already exist.
     *
     * @param tag the tag name to add; ignored if null or blank
     */
    public void addTag(@Nullable String tag) {
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
    public void addTag(@NotNull Tag tag) {
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
    @NotNull
    public Iterable<Series> series() {
        return () -> sonarrQueue()
                .filter(queueResource -> queueResource.getDownloadId() != null
                        && queueResource.getDownloadId().equalsIgnoreCase(getHash()))
                .map(de.llalon.cinematic.client.sonarr.dto.QueueResource::getSeriesId)
                .filter(Objects::nonNull)
                .distinct()
                .map(seriesId -> sonarrSeries()
                        .filter(s -> s.getId() != null && s.getId().equals(seriesId))
                        .findFirst()
                        .map(s -> new Series(ctx, s))
                        .orElseThrow(() -> new java.util.NoSuchElementException("Series not found: " + seriesId)))
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
    @NotNull
    public Iterable<Movie> movies() {
        return () -> radarrQueue()
                .filter(queueResource -> queueResource.getDownloadId() != null
                        && queueResource.getDownloadId().equalsIgnoreCase(getHash()))
                .map(de.llalon.cinematic.client.radarr.dto.QueueResource::getMovieId)
                .filter(Objects::nonNull)
                .distinct()
                .map(movieId -> radarrMovies()
                        .filter(m -> m.getId() != null && m.getId().equals(movieId))
                        .findFirst()
                        .map(m -> new Movie(ctx, m))
                        .orElseThrow(() -> new java.util.NoSuchElementException("Movie not found: " + movieId)))
                .iterator();
    }
}
