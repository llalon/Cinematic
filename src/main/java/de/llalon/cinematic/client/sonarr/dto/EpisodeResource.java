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
    private final Integer id;

    @JsonProperty("seriesId")
    private final Integer seriesId;

    @JsonProperty("tvdbId")
    private final Integer tvdbId;

    @JsonProperty("episodeFileId")
    private final Integer episodeFileId;

    @JsonProperty("seasonNumber")
    private final Integer seasonNumber;

    @JsonProperty("episodeNumber")
    private final Integer episodeNumber;

    @JsonProperty("title")
    private final String title;

    @JsonProperty("airDate")
    private final String airDate;

    @JsonProperty("airDateUtc")
    private final LocalDateTime airDateUtc;

    @JsonProperty("lastSearchTime")
    private final LocalDateTime lastSearchTime;

    @JsonProperty("runtime")
    private final Integer runtime;

    @JsonProperty("overview")
    private final String overview;

    @JsonProperty("episodeFile")
    private final EpisodeFileResource episodeFile;

    @JsonProperty("hasFile")
    private final Boolean hasFile;

    @JsonProperty("monitored")
    private final Boolean monitored;

    @JsonProperty("absoluteEpisodeNumber")
    private final Integer absoluteEpisodeNumber;

    @JsonProperty("sceneAbsoluteEpisodeNumber")
    private final Integer sceneAbsoluteEpisodeNumber;

    @JsonProperty("sceneEpisodeNumber")
    private final Integer sceneEpisodeNumber;

    @JsonProperty("sceneSeasonNumber")
    private final Integer sceneSeasonNumber;

    @JsonProperty("unverifiedSceneNumbering")
    private final Boolean unverifiedSceneNumbering;

    @JsonProperty("endTime")
    private final LocalDateTime endTime;

    @JsonProperty("grabDate")
    private final LocalDateTime grabDate;

    @JsonProperty("series")
    private final SeriesResource series;
}
