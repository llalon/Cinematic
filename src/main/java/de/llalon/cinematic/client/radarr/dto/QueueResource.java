package de.llalon.cinematic.client.radarr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a download queue item in Radarr.
 *
 * Queue items track active downloads and their association with movies.
 * The downloadId field is critical for correlating with download clients (e.g., qBittorrent torrent hash).
 */
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueueResource {

    @JsonProperty("id")
    private final Integer id;

    @JsonProperty("movieId")
    private final Integer movieId;

    @JsonProperty("movie")
    private final MovieResource movie;

    @JsonProperty("size")
    private final Double size;

    @JsonProperty("title")
    private final String title;

    @JsonProperty("sizeleft")
    private final Double sizeleft;

    @JsonProperty("estimatedCompletionTime")
    private final LocalDateTime estimatedCompletionTime;

    @JsonProperty("added")
    private final LocalDateTime added;

    @JsonProperty("status")
    private final String status;

    @JsonProperty("trackedDownloadStatus")
    private final String trackedDownloadStatus;

    @JsonProperty("trackedDownloadState")
    private final String trackedDownloadState;

    @JsonProperty("errorMessage")
    private final String errorMessage;

    /**
     * Download ID that identifies the download in the download client.
     * For qBittorrent, this is the torrent hash (uppercase).
     * This is the key field for correlating queue items with actual downloads.
     */
    @JsonProperty("downloadId")
    private final String downloadId;

    @JsonProperty("protocol")
    private final String protocol;

    @JsonProperty("downloadClient")
    private final String downloadClient;

    @JsonProperty("indexer")
    private final String indexer;

    @JsonProperty("outputPath")
    private final String outputPath;
}
