package de.llalon.cinematic.client.sonarr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * Represents an episode file in Sonarr.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EpisodeFileResource {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("seriesId")
    private Integer seriesId;

    @JsonProperty("seasonNumber")
    private Integer seasonNumber;

    @JsonProperty("relativePath")
    private String relativePath;

    @JsonProperty("path")
    private String path;

    @JsonProperty("size")
    private Long size;

    @JsonProperty("dateAdded")
    private LocalDateTime dateAdded;

    @JsonProperty("sceneName")
    private String sceneName;

    @JsonProperty("releaseGroup")
    private String releaseGroup;

    @JsonProperty("quality")
    private Object quality;

    @JsonProperty("qualityCutoffNotMet")
    private Boolean qualityCutoffNotMet;

    @JsonProperty("mediaInfo")
    private Object mediaInfo;

    @JsonProperty("originalFilePath")
    private String originalFilePath;

    @JsonProperty("qualityWeight")
    private Integer qualityWeight;
}
