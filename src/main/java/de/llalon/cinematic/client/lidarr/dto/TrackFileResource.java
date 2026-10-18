package de.llalon.cinematic.client.lidarr.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/** Lidarr TrackFileResource API resource. */
@Data
@Builder
@AllArgsConstructor
public class TrackFileResource {
    private final Integer id;
    private final Integer artistId;
    private final Integer albumId;
    private final String path;
    private final Long size;
    private final LocalDateTime dateAdded;
    private final String sceneName;
    private final String releaseGroup;
    private final Object quality;
    private final Integer qualityWeight;
    private final List<Object> customFormats;
    private final Integer customFormatScore;
    private final Integer indexerFlags;
    private final LidarrMediaInfoResource mediaInfo;
    private final Boolean qualityCutoffNotMet;
    private final Object audioTags;
}
