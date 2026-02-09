package de.llalon.cinematic.client.sonarr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * Represents a download queue item in Sonarr.
 *
 * Queue items track active downloads and their association with series/episodes.
 * The downloadId field is critical for correlating with download clients (e.g., qBittorrent torrent hash).
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueueResource {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("seriesId")
    private Integer seriesId;

    @JsonProperty("episodeId")
    private Integer episodeId;

    @JsonProperty("seasonNumber")
    private Integer seasonNumber;

    @JsonProperty("series")
    private SeriesResource series;

    @JsonProperty("episode")
    private EpisodeResource episode;

    @JsonProperty("size")
    private Double size;

    @JsonProperty("title")
    private String title;

    @JsonProperty("sizeleft")
    private Double sizeleft;

    @JsonProperty("estimatedCompletionTime")
    private LocalDateTime estimatedCompletionTime;

    @JsonProperty("added")
    private LocalDateTime added;

    @JsonProperty("status")
    private String status;

    @JsonProperty("trackedDownloadStatus")
    private String trackedDownloadStatus;

    @JsonProperty("trackedDownloadState")
    private String trackedDownloadState;

    @JsonProperty("errorMessage")
    private String errorMessage;

    /**
     * Download ID that identifies the download in the download client.
     * For qBittorrent, this is the torrent hash (uppercase).
     * This is the key field for correlating queue items with actual downloads.
     */
    @JsonProperty("downloadId")
    private String downloadId;

    @JsonProperty("protocol")
    private String protocol;

    @JsonProperty("downloadClient")
    private String downloadClient;

    @JsonProperty("indexer")
    private String indexer;

    @JsonProperty("outputPath")
    private String outputPath;
}
