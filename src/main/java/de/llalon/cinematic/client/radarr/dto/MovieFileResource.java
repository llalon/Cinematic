package de.llalon.cinematic.client.radarr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * Represents a movie file in Radarr.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieFileResource {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("movieId")
    private Integer movieId;

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

    @JsonProperty("edition")
    private String edition;

    @JsonProperty("qualityCutoffNotMet")
    private Boolean qualityCutoffNotMet;

    @JsonProperty("originalFilePath")
    private String originalFilePath;
}
