package de.llalon.cinematic.client.lidarr.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/** Lidarr QueueResource API resource. */
@Data
@Builder
@AllArgsConstructor
public class LidarrQueue {
    private final Integer id;
    private final Integer artistId;
    private final Integer albumId;
    private final ArtistResource artist;
    private final AlbumResource album;
    private final Object quality;
    private final List<Object> customFormats;
    private final Integer customFormatScore;
    private final Double size;
    private final String title;
    private final Double sizeleft;
    private final String timeleft;
    private final LocalDateTime estimatedCompletionTime;
    private final LocalDateTime added;
    private final String status;
    private final String trackedDownloadStatus;
    private final String trackedDownloadState;
    private final List<Object> statusMessages;
    private final String errorMessage;
    private final String downloadId;
    private final String protocol;
    private final String downloadClient;
    private final Boolean downloadClientHasPostImportCategory;
    private final String indexer;
    private final String outputPath;
    private final Integer trackFileCount;
    private final Integer trackHasFileCount;
    private final Boolean downloadForced;
}
