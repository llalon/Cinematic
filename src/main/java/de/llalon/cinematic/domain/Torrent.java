package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.qbittorrent.dto.QBittorrentInfo;
import de.llalon.cinematic.client.radarr.dto.RadarrQueue;
import de.llalon.cinematic.client.sonarr.dto.SonarrQueue;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Stream;
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
    private final QBittorrentInfo qbittorrentInfo;

    Torrent(ClientContext ctx, @NotNull QBittorrentInfo qbittorrentInfo) {
        super(ctx);
        this.qbittorrentInfo = qbittorrentInfo;
    }

    /**
     * Sets the priority of the torrent to the top. Only works if queueing is enabled.
     */
    public void setTopPriority() {
        this.ctx.getQbittorrentClient().setTopPriority(List.of(this.qbittorrentInfo.getHash()));
    }

    /**
     * Sets the priority of the torrent to the bottom. Only works if queueing is enabled.
     */
    public void setBottomPriority() {
        this.ctx.getQbittorrentClient().setBottomPriority(List.of(this.qbittorrentInfo.getHash()));
    }

    /**
     * Adds a tag to this torrent by name, creating it in qBittorrent if it does not already exist.
     *
     * @param tag the tag name to add; ignored if null or blank
     */
    public void addTag(@Nullable String tag) {
        if (tag != null && !tag.isBlank()) {
            final String torrentHash = qbittorrentInfo.getHash();
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
                        && sonarrQueue.getDownloadId().equalsIgnoreCase(this.QBittorrentInfo.getHash()))
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
                        && queueResource.getDownloadId().equalsIgnoreCase(this.QBittorrentInfo.getHash()))
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
        return this.QBittorrentInfo.getHash();
    }

    /**
     * Returns the category assigned to this torrent in qBittorrent.
     *
     * @return the torrent category
     */
    public String getCategory() {
        return this.QBittorrentInfo.getCategory();
    }

    /**
     * Returns the current download/seed state of this torrent.
     *
     * @return the torrent state
     */
    public String getState() {
        return this.QBittorrentInfo.getState();
    }

    /**
     * Returns the display name of this torrent.
     *
     * @return the torrent name
     */
    public String getName() {
        return this.QBittorrentInfo.getName();
    }

    /**
     * Returns the absolute content path for this torrent.
     *
     * @return the content path, or null if not available
     */
    public String getContentPath() {
        return this.QBittorrentInfo.getContentPath();
    }

    /**
     * Returns the amount of data left to download (bytes).
     *
     * @return bytes left, or null if not available
     */
    public Long getAmountLeft() {
        return this.QBittorrentInfo.getAmountLeft();
    }

    /**
     * Returns the torrent progress as fraction (0.0 - 1.0).
     *
     * @return progress, or null if not available
     */
    public Float getProgress() {
        return this.QBittorrentInfo.getProgress();
    }

    /**
     * Returns the completion time as Unix epoch seconds, if available.
     *
     * @return completion time, or null if not available
     */
    public Long getCompletionOn() {
        return this.QBittorrentInfo.getCompletionOn();
    }

    /**
     * Returns the announce URL of the tracker used by this torrent.
     *
     * @return the tracker URL
     */
    public String getTracker() {
        return this.QBittorrentInfo.getTracker();
    }

    /**
     * @return true if the torrent is completed
     */
    public boolean isCompleted() {
        final Long amountLeft = this.QBittorrentInfo.getAmountLeft();
        final Long completionOn = this.QBittorrentInfo.getCompletionOn();
        final Long completed = this.QBittorrentInfo.getCompleted();

        if (amountLeft == null || completionOn == null || completed == null) {
            return false;
        }

        return amountLeft.longValue() == 0L
                && completionOn.longValue() > 0L
                && completed.longValue() > 0L;
    }

    /**
     * Returns the files contained in this torrent.
     *
     * @return an iterable of {@link TorrentFile} objects
     */
    @NotNull
    public Iterable<TorrentFile> files() {
        return () -> ctx.getQbittorrentClient().getTorrentFiles(this.QBittorrentInfo.getHash()).stream()
                .map(f -> new TorrentFile(ctx, f))
                .iterator();
    }

    /**
     * Deletes this torrent from qBittorrent.
     *
     * @param deleteFiles if {@code true}, also removes the downloaded files from disk
     */
    public void remove(boolean deleteFiles) {
        log.debug("Deleting torrent: {}, deleteFiles: {}", this.QBittorrentInfo.getHash(), deleteFiles);
        ctx.getQbittorrentClient().deleteTorrents(List.of(this.QBittorrentInfo.getHash()), deleteFiles);
    }

    /**
     * Blacklists this torrent in the Sonarr/Radarr download queues.
     *
     * <p>Finds all Sonarr/Radarr queue items whose {@code downloadId} matches this torrent's hash
     * and removes them with the blocklist flag set to {@code true}. The torrent is not
     * removed from the download client — handle that separately via {@link #remove}.
     * This will not trigger a replacement search ({@code skipRedownload=true}).</p>
     *
     * <p>Logs a warning if no matching queue items are found.</p>
     */
    public void blacklist() {
        final String hash = this.QBittorrentInfo.getHash();

        log.debug("Blacklisting torrent: {}, hash: {}", this.QBittorrentInfo.getHash(), hash);

        final Stream<Integer> sonarrResults = sonarrQueue()
                .filter(q -> q.getDownloadId() != null && q.getDownloadId().equalsIgnoreCase(hash))
                .map(q -> {
                    log.debug("Blacklisting Sonarr queue item id={} for torrent {}", q.getId(), hash);
                    ctx.getSonarrClient().deleteQueueItem(q.getId(), true, false, true);
                    return 1;
                });

        final Stream<Integer> radarrResults = radarrQueue()
                .filter(q -> q.getDownloadId() != null && q.getDownloadId().equalsIgnoreCase(hash))
                .map(q -> {
                    log.debug("Blacklisting Radarr queue item id={} for torrent {}", q.getId(), hash);
                    ctx.getRadarrClient().deleteQueueItem(q.getId(), true, false, true);
                    return 1;
                });

        final long count = Stream.concat(sonarrResults, radarrResults).count();

        if (count == 0) {
            log.warn("No queue items found for torrent hash: {}", hash);
        }
    }
}
