package de.llalon.cinematic.client.radarr.dto;

import com.squareup.moshi.Json;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a movie in Radarr.
 */
@Data
@AllArgsConstructor
public class MovieResource {

    @Json(name = "id")
    private final Integer id;

    @Json(name = "title")
    private final String title;

    @Json(name = "originalTitle")
    private final String originalTitle;

    @Json(name = "sortTitle")
    private final String sortTitle;

    @Json(name = "sizeOnDisk")
    private final Long sizeOnDisk;

    @Json(name = "status")
    private final String status;

    @Json(name = "overview")
    private final String overview;

    @Json(name = "inCinemas")
    private final LocalDate inCinemas;

    @Json(name = "physicalRelease")
    private final LocalDate physicalRelease;

    @Json(name = "digitalRelease")
    private final LocalDate digitalRelease;

    @Json(name = "releaseDate")
    private final LocalDate releaseDate;

    @Json(name = "website")
    private final String website;

    @Json(name = "year")
    private final Integer year;

    @Json(name = "youTubeTrailerId")
    private final String youTubeTrailerId;

    @Json(name = "studio")
    private final String studio;

    @Json(name = "path")
    private final String path;

    @Json(name = "qualityProfileId")
    private final Integer qualityProfileId;

    @Json(name = "hasFile")
    private final Boolean hasFile;

    @Json(name = "movieFileId")
    private final Integer movieFileId;

    @Json(name = "monitored")
    private final Boolean monitored;

    @Json(name = "minimumAvailability")
    private final String minimumAvailability;

    @Json(name = "isAvailable")
    private final Boolean isAvailable;

    @Json(name = "folderName")
    private final String folderName;

    @Json(name = "runtime")
    private final Integer runtime;

    @Json(name = "cleanTitle")
    private final String cleanTitle;

    @Json(name = "imdbId")
    private final String imdbId;

    @Json(name = "tmdbId")
    private final Integer tmdbId;

    @Json(name = "titleSlug")
    private final String titleSlug;

    @Json(name = "rootFolderPath")
    private final String rootFolderPath;

    @Json(name = "folder")
    private final String folder;

    @Json(name = "certification")
    private final String certification;

    @Json(name = "genres")
    private final List<String> genres;

    @Json(name = "keywords")
    private final List<String> keywords;

    @Json(name = "tags")
    private final List<Integer> tags;

    @Json(name = "added")
    private final LocalDateTime added;

    @Json(name = "popularity")
    private final Float popularity;

    @Json(name = "lastSearchTime")
    private final LocalDateTime lastSearchTime;

    @Json(name = "movieFile")
    private final MovieFileResource movieFile;
}
