package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.qbittorrent.dto.TorrentInfo;
import de.llalon.cinematic.client.radarr.dto.RadarrQueue;
import de.llalon.cinematic.client.sonarr.dto.SonarrQueue;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Domain representation of a torrent managed by qBittorrent.
 *
 * <p>Provides navigation to the {@link Series} and {@link Movie} objects
 * associated with this torrent via the Sonarr/Radarr download queues.
 * Supports priority management and tag operations.</p>
 */
@Slf4j
public class Torrent extends DomainModel {

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
                .filter(sonarrQueue -> sonarrQueue.getDownloadId() != null
                        && sonarrQueue.getDownloadId().equalsIgnoreCase(this.torrentInfo.getHash()))
                .map(SonarrQueue::getSeriesId)
                .filter(Objects::nonNull)
                .distinct()
                .map(seriesId -> sonarrSeries()
                        .filter(s -> s.getId() != null && s.getId().equals(seriesId))
                        .findFirst()
                        .map(s -> new Series(ctx, s))
                        .orElseThrow(() -> new NoSuchElementException("Series not found: " + seriesId)))
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
                        && queueResource.getDownloadId().equalsIgnoreCase(this.torrentInfo.getHash()))
                .map(RadarrQueue::getMovieId)
                .filter(Objects::nonNull)
                .distinct()
                .map(movieId -> radarrMovies()
                        .filter(m -> m.getId() != null && m.getId().equals(movieId))
                        .findFirst()
                        .map(m -> new Movie(ctx, m))
                        .orElseThrow(() -> new NoSuchElementException("Movie not found: " + movieId)))
                .iterator();
    }

    /**
     * Returns the info-hash of this torrent.
     *
     * @return the torrent hash
     */
    public String getHash() {
        return this.torrentInfo.getHash();
    }

    /**
     * Returns the category assigned to this torrent in qBittorrent.
     *
     * @return the torrent category
     */
    public String getCategory() {
        return this.torrentInfo.getCategory();
    }

    /**
     * Returns the current download/seed state of this torrent.
     *
     * @return the torrent state
     */
    public String getState() {
        return this.torrentInfo.getState();
    }

    /**
     * Returns the display name of this torrent.
     *
     * @return the torrent name
     */
    public String getName() {
        return this.torrentInfo.getName();
    }

    /**
     * Returns the absolute content path for this torrent.
     *
     * @return the content path, or null if not available
     */
    public String getContentPath() {
        return this.torrentInfo.getContentPath();
    }

    /**
     * Returns the amount of data left to download (bytes).
     *
     * @return bytes left, or null if not available
     */
    public Long getAmountLeft() {
        return this.torrentInfo.getAmountLeft();
    }

    /**
     * Returns the torrent progress as fraction (0.0 - 1.0).
     *
     * @return progress, or null if not available
     */
    public Float getProgress() {
        return this.torrentInfo.getProgress();
    }

    /**
     * Returns the completion time as Unix epoch seconds, if available.
     *
     * @return completion time, or null if not available
     */
    public Long getCompletionOn() {
        return this.torrentInfo.getCompletionOn();
    }

    /**
     * Returns the announce URL of the tracker used by this torrent.
     *
     * @return the tracker URL
     */
    public String getTracker() {
        return this.torrentInfo.getTracker();
    }
}
