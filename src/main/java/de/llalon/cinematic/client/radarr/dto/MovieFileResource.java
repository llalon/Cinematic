package de.llalon.cinematic.client.radarr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a movie file in Radarr.
 */
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieFileResource {

    @JsonProperty("id")
    private final Integer id;

    @JsonProperty("movieId")
    private final Integer movieId;

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

    @JsonProperty("edition")
    private final String edition;

    @JsonProperty("qualityCutoffNotMet")
    private final Boolean qualityCutoffNotMet;

    @JsonProperty("originalFilePath")
    private final String originalFilePath;
}
