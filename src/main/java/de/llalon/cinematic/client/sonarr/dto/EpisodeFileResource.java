package de.llalon.cinematic.client.sonarr.dto;

import com.squareup.moshi.Json;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents an episode file in Sonarr.
 */
@Data
@AllArgsConstructor
public class EpisodeFileResource {

    @Json(name = "id")
    private final Integer id;

    @Json(name = "seriesId")
    private final Integer seriesId;

    @Json(name = "seasonNumber")
    private final Integer seasonNumber;

    @Json(name = "relativePath")
    private final String relativePath;

    @Json(name = "path")
    private final String path;

    @Json(name = "size")
    private final Long size;

    @Json(name = "dateAdded")
    private final LocalDateTime dateAdded;

    @Json(name = "sceneName")
    private final String sceneName;

    @Json(name = "releaseGroup")
    private final String releaseGroup;

    @Json(name = "quality")
    private final Object quality;

    @Json(name = "qualityCutoffNotMet")
    private final Boolean qualityCutoffNotMet;

    @Json(name = "mediaInfo")
    private final Object mediaInfo;

    @Json(name = "originalFilePath")
    private final String originalFilePath;

    @Json(name = "qualityWeight")
    private final Integer qualityWeight;
}
