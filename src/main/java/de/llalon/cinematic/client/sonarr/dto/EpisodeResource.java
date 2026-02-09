package de.llalon.cinematic.client.sonarr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Data;

/**
 * Represents an episode in Sonarr.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EpisodeResource {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("seriesId")
    private Integer seriesId;

    @JsonProperty("tvdbId")
    private Integer tvdbId;

    @JsonProperty("episodeFileId")
    private Integer episodeFileId;

    @JsonProperty("seasonNumber")
    private Integer seasonNumber;

    @JsonProperty("episodeNumber")
    private Integer episodeNumber;

    @JsonProperty("title")
    private String title;

    @JsonProperty("airDate")
    private String airDate;

    @JsonProperty("airDateUtc")
    private LocalDateTime airDateUtc;

    @JsonProperty("lastSearchTime")
    private LocalDateTime lastSearchTime;

    @JsonProperty("runtime")
    private Integer runtime;

    @JsonProperty("overview")
    private String overview;

    @JsonProperty("episodeFile")
    private EpisodeFileResource episodeFile;

    @JsonProperty("hasFile")
    private Boolean hasFile;

    @JsonProperty("monitored")
    private Boolean monitored;

    @JsonProperty("absoluteEpisodeNumber")
    private Integer absoluteEpisodeNumber;

    @JsonProperty("sceneAbsoluteEpisodeNumber")
    private Integer sceneAbsoluteEpisodeNumber;

    @JsonProperty("sceneEpisodeNumber")
    private Integer sceneEpisodeNumber;

    @JsonProperty("sceneSeasonNumber")
    private Integer sceneSeasonNumber;

    @JsonProperty("unverifiedSceneNumbering")
    private Boolean unverifiedSceneNumbering;

    @JsonProperty("endTime")
    private LocalDateTime endTime;

    @JsonProperty("grabDate")
    private LocalDateTime grabDate;

    @JsonProperty("series")
    private SeriesResource series;
}
