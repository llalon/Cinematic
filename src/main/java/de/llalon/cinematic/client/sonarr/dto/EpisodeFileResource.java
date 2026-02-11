package de.llalon.cinematic.client.sonarr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents an episode file in Sonarr.
 */
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class EpisodeFileResource {

    @JsonProperty("id")
    private final Integer id;

    @JsonProperty("seriesId")
    private final Integer seriesId;

    @JsonProperty("seasonNumber")
    private final Integer seasonNumber;

    @JsonProperty("relativePath")
    private final String relativePath;

    @JsonProperty("path")
    private final String path;

    @JsonProperty("size")
    private final Long size;

    @JsonProperty("dateAdded")
    private final LocalDateTime dateAdded;

    @JsonProperty("sceneName")
    private final String sceneName;

    @JsonProperty("releaseGroup")
    private final String releaseGroup;

    @JsonProperty("quality")
    private final Object quality;

    @JsonProperty("qualityCutoffNotMet")
    private final Boolean qualityCutoffNotMet;

    @JsonProperty("mediaInfo")
    private final Object mediaInfo;

    @JsonProperty("originalFilePath")
    private final String originalFilePath;

    @JsonProperty("qualityWeight")
    private final Integer qualityWeight;
}
