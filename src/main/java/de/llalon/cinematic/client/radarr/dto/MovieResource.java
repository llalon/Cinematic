package de.llalon.cinematic.client.radarr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a movie in Radarr.
 */
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieResource {

    @JsonProperty("id")
    private final Integer id;

    @JsonProperty("title")
    private final String title;

    @JsonProperty("originalTitle")
    private final String originalTitle;

    @JsonProperty("sortTitle")
    private final String sortTitle;

    @JsonProperty("sizeOnDisk")
    private final Long sizeOnDisk;

    @JsonProperty("status")
    private final String status;

    @JsonProperty("overview")
    private final String overview;

    @JsonProperty("inCinemas")
    private final LocalDateTime inCinemas;

    @JsonProperty("physicalRelease")
    private final LocalDateTime physicalRelease;

    @JsonProperty("digitalRelease")
    private final LocalDateTime digitalRelease;

    @JsonProperty("releaseDate")
    private final LocalDateTime releaseDate;

    @JsonProperty("website")
    private final String website;

    @JsonProperty("year")
    private final Integer year;

    @JsonProperty("youTubeTrailerId")
    private final String youTubeTrailerId;

    @JsonProperty("studio")
    private final String studio;

    @JsonProperty("path")
    private final String path;

    @JsonProperty("qualityProfileId")
    private final Integer qualityProfileId;

    @JsonProperty("hasFile")
    private final Boolean hasFile;

    @JsonProperty("movieFileId")
    private final Integer movieFileId;

    @JsonProperty("monitored")
    private final Boolean monitored;

    @JsonProperty("minimumAvailability")
    private final String minimumAvailability;

    @JsonProperty("isAvailable")
    private final Boolean isAvailable;

    @JsonProperty("folderName")
    private final String folderName;

    @JsonProperty("runtime")
    private final Integer runtime;

    @JsonProperty("cleanTitle")
    private final String cleanTitle;

    @JsonProperty("imdbId")
    private final String imdbId;

    @JsonProperty("tmdbId")
    private final Integer tmdbId;

    @JsonProperty("titleSlug")
    private final String titleSlug;

    @JsonProperty("rootFolderPath")
    private final String rootFolderPath;

    @JsonProperty("folder")
    private final String folder;

    @JsonProperty("certification")
    private final String certification;

    @JsonProperty("genres")
    private final List<String> genres;

    @JsonProperty("keywords")
    private final List<String> keywords;

    @JsonProperty("tags")
    private final List<Integer> tags;

    @JsonProperty("added")
    private final LocalDateTime added;

    @JsonProperty("popularity")
    private final Float popularity;

    @JsonProperty("lastSearchTime")
    private final LocalDateTime lastSearchTime;

    @JsonProperty("movieFile")
    private final MovieFileResource movieFile;
}
