package de.llalon.cinematic.client.sonarr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a TV series in Sonarr.
 */
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeriesResource {

    @JsonProperty("id")
    private final Integer id;

    @JsonProperty("title")
    private final String title;

    @JsonProperty("sortTitle")
    private final String sortTitle;

    @JsonProperty("status")
    private final String status;

    @JsonProperty("ended")
    private final Boolean ended;

    @JsonProperty("overview")
    private final String overview;

    @JsonProperty("network")
    private final String network;

    @JsonProperty("airTime")
    private final String airTime;

    @JsonProperty("nextAiring")
    private final LocalDateTime nextAiring;

    @JsonProperty("previousAiring")
    private final LocalDateTime previousAiring;

    @JsonProperty("year")
    private final Integer year;

    @JsonProperty("path")
    private final String path;

    @JsonProperty("qualityProfileId")
    private final Integer qualityProfileId;

    @JsonProperty("seasonFolder")
    private final Boolean seasonFolder;

    @JsonProperty("monitored")
    private final Boolean monitored;

    @JsonProperty("runtime")
    private final Integer runtime;

    @JsonProperty("tvdbId")
    private final Integer tvdbId;

    @JsonProperty("tvRageId")
    private final Integer tvRageId;

    @JsonProperty("tvMazeId")
    private final Integer tvMazeId;

    @JsonProperty("tmdbId")
    private final Integer tmdbId;

    @JsonProperty("firstAired")
    private final LocalDateTime firstAired;

    @JsonProperty("lastAired")
    private final LocalDateTime lastAired;

    @JsonProperty("seriesType")
    private final String seriesType;

    @JsonProperty("cleanTitle")
    private final String cleanTitle;

    @JsonProperty("imdbId")
    private final String imdbId;

    @JsonProperty("titleSlug")
    private final String titleSlug;

    @JsonProperty("certification")
    private final String certification;

    @JsonProperty("genres")
    private final List<String> genres;

    @JsonProperty("tags")
    private final List<Integer> tags;

    @JsonProperty("added")
    private final LocalDateTime added;

    @JsonProperty("ratings")
    private final Object ratings;

    @JsonProperty("statistics")
    private final Object statistics;
}
