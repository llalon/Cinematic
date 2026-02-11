package de.llalon.cinematic.client.sonarr.dto;

import com.squareup.moshi.Json;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a TV series in Sonarr.
 */
@Data
@AllArgsConstructor
public class SeriesResource {

    @Json(name = "id")
    private final Integer id;

    @Json(name = "title")
    private final String title;

    @Json(name = "sortTitle")
    private final String sortTitle;

    @Json(name = "status")
    private final String status;

    @Json(name = "ended")
    private final Boolean ended;

    @Json(name = "overview")
    private final String overview;

    @Json(name = "network")
    private final String network;

    @Json(name = "airTime")
    private final String airTime;

    @Json(name = "nextAiring")
    private final LocalDateTime nextAiring;

    @Json(name = "previousAiring")
    private final LocalDateTime previousAiring;

    @Json(name = "year")
    private final Integer year;

    @Json(name = "path")
    private final String path;

    @Json(name = "qualityProfileId")
    private final Integer qualityProfileId;

    @Json(name = "seasonFolder")
    private final Boolean seasonFolder;

    @Json(name = "monitored")
    private final Boolean monitored;

    @Json(name = "runtime")
    private final Integer runtime;

    @Json(name = "tvdbId")
    private final Integer tvdbId;

    @Json(name = "tvRageId")
    private final Integer tvRageId;

    @Json(name = "tvMazeId")
    private final Integer tvMazeId;

    @Json(name = "tmdbId")
    private final Integer tmdbId;

    @Json(name = "firstAired")
    private final LocalDateTime firstAired;

    @Json(name = "lastAired")
    private final LocalDateTime lastAired;

    @Json(name = "seriesType")
    private final String seriesType;

    @Json(name = "cleanTitle")
    private final String cleanTitle;

    @Json(name = "imdbId")
    private final String imdbId;

    @Json(name = "titleSlug")
    private final String titleSlug;

    @Json(name = "certification")
    private final String certification;

    @Json(name = "genres")
    private final List<String> genres;

    @Json(name = "tags")
    private final List<Integer> tags;

    @Json(name = "added")
    private final LocalDateTime added;

    @Json(name = "ratings")
    private final Object ratings;

    @Json(name = "statistics")
    private final Object statistics;
}
