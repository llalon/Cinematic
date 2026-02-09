package de.llalon.cinematic.client.sonarr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * Represents a TV series in Sonarr.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SeriesResource {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("sortTitle")
    private String sortTitle;

    @JsonProperty("status")
    private String status;

    @JsonProperty("ended")
    private Boolean ended;

    @JsonProperty("overview")
    private String overview;

    @JsonProperty("network")
    private String network;

    @JsonProperty("airTime")
    private String airTime;

    @JsonProperty("nextAiring")
    private LocalDateTime nextAiring;

    @JsonProperty("previousAiring")
    private LocalDateTime previousAiring;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("path")
    private String path;

    @JsonProperty("qualityProfileId")
    private Integer qualityProfileId;

    @JsonProperty("seasonFolder")
    private Boolean seasonFolder;

    @JsonProperty("monitored")
    private Boolean monitored;

    @JsonProperty("runtime")
    private Integer runtime;

    @JsonProperty("tvdbId")
    private Integer tvdbId;

    @JsonProperty("tvRageId")
    private Integer tvRageId;

    @JsonProperty("tvMazeId")
    private Integer tvMazeId;

    @JsonProperty("tmdbId")
    private Integer tmdbId;

    @JsonProperty("firstAired")
    private LocalDateTime firstAired;

    @JsonProperty("lastAired")
    private LocalDateTime lastAired;

    @JsonProperty("seriesType")
    private String seriesType;

    @JsonProperty("cleanTitle")
    private String cleanTitle;

    @JsonProperty("imdbId")
    private String imdbId;

    @JsonProperty("titleSlug")
    private String titleSlug;

    @JsonProperty("certification")
    private String certification;

    @JsonProperty("genres")
    private List<String> genres;

    @JsonProperty("tags")
    private List<Integer> tags;

    @JsonProperty("added")
    private LocalDateTime added;

    @JsonProperty("ratings")
    private Object ratings;

    @JsonProperty("statistics")
    private Object statistics;
}
