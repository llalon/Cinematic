package de.llalon.cinematic.client.sonarr.dto;

import com.squareup.moshi.Json;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents an episode in Sonarr.
 */
@Data
@AllArgsConstructor
public class EpisodeResource {

    @Json(name = "id")
    private final Integer id;

    @Json(name = "seriesId")
    private final Integer seriesId;

    @Json(name = "tvdbId")
    private final Integer tvdbId;

    @Json(name = "episodeFileId")
    private final Integer episodeFileId;

    @Json(name = "seasonNumber")
    private final Integer seasonNumber;

    @Json(name = "episodeNumber")
    private final Integer episodeNumber;

    @Json(name = "title")
    private final String title;

    @Json(name = "airDate")
    private final String airDate;

    @Json(name = "airDateUtc")
    private final LocalDateTime airDateUtc;

    @Json(name = "lastSearchTime")
    private final LocalDateTime lastSearchTime;

    @Json(name = "runtime")
    private final Integer runtime;

    @Json(name = "overview")
    private final String overview;

    @Json(name = "episodeFile")
    private final EpisodeFileResource episodeFile;

    @Json(name = "hasFile")
    private final Boolean hasFile;

    @Json(name = "monitored")
    private final Boolean monitored;

    @Json(name = "absoluteEpisodeNumber")
    private final Integer absoluteEpisodeNumber;

    @Json(name = "sceneAbsoluteEpisodeNumber")
    private final Integer sceneAbsoluteEpisodeNumber;

    @Json(name = "sceneEpisodeNumber")
    private final Integer sceneEpisodeNumber;

    @Json(name = "sceneSeasonNumber")
    private final Integer sceneSeasonNumber;

    @Json(name = "unverifiedSceneNumbering")
    private final Boolean unverifiedSceneNumbering;

    @Json(name = "endTime")
    private final LocalDateTime endTime;

    @Json(name = "grabDate")
    private final LocalDateTime grabDate;

    @Json(name = "series")
    private final SeriesResource series;
}
