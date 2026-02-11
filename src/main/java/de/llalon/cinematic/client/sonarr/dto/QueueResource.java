package de.llalon.cinematic.client.sonarr.dto;

import com.squareup.moshi.Json;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a download queue item in Sonarr.
 *
 * Queue items track active downloads and their association with series/episodes.
 * The downloadId field is critical for correlating with download clients (e.g., qBittorrent torrent hash).
 */
@Data
@AllArgsConstructor
public class QueueResource {

    @Json(name = "id")
    private final Integer id;

    @Json(name = "seriesId")
    private final Integer seriesId;

    @Json(name = "episodeId")
    private final Integer episodeId;

    @Json(name = "seasonNumber")
    private final Integer seasonNumber;

    @Json(name = "series")
    private final SeriesResource series;

    @Json(name = "episode")
    private final EpisodeResource episode;

    @Json(name = "size")
    private final Double size;

    @Json(name = "title")
    private final String title;

    @Json(name = "sizeleft")
    private final Double sizeleft;

    @Json(name = "estimatedCompletionTime")
    private final LocalDateTime estimatedCompletionTime;

    @Json(name = "added")
    private final LocalDateTime added;

    @Json(name = "status")
    private final String status;

    @Json(name = "trackedDownloadStatus")
    private final String trackedDownloadStatus;

    @Json(name = "trackedDownloadState")
    private final String trackedDownloadState;

    @Json(name = "errorMessage")
    private final String errorMessage;

    /**
     * Download ID that identifies the download in the download client.
     * For qBittorrent, this is the torrent hash (uppercase).
     * This is the key field for correlating queue items with actual downloads.
     */
    @Json(name = "downloadId")
    private final String downloadId;

    @Json(name = "protocol")
    private final String protocol;

    @Json(name = "downloadClient")
    private final String downloadClient;

    @Json(name = "indexer")
    private final String indexer;

    @Json(name = "outputPath")
    private final String outputPath;
}
