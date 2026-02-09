package de.llalon.cinematic.client.radarr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * Represents a movie in Radarr.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieResource {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("title")
    private String title;

    @JsonProperty("originalTitle")
    private String originalTitle;

    @JsonProperty("sortTitle")
    private String sortTitle;

    @JsonProperty("sizeOnDisk")
    private Long sizeOnDisk;

    @JsonProperty("status")
    private String status;

    @JsonProperty("overview")
    private String overview;

    @JsonProperty("inCinemas")
    private LocalDateTime inCinemas;

    @JsonProperty("physicalRelease")
    private LocalDateTime physicalRelease;

    @JsonProperty("digitalRelease")
    private LocalDateTime digitalRelease;

    @JsonProperty("releaseDate")
    private LocalDateTime releaseDate;

    @JsonProperty("website")
    private String website;

    @JsonProperty("year")
    private Integer year;

    @JsonProperty("youTubeTrailerId")
    private String youTubeTrailerId;

    @JsonProperty("studio")
    private String studio;

    @JsonProperty("path")
    private String path;

    @JsonProperty("qualityProfileId")
    private Integer qualityProfileId;

    @JsonProperty("hasFile")
    private Boolean hasFile;

    @JsonProperty("movieFileId")
    private Integer movieFileId;

    @JsonProperty("monitored")
    private Boolean monitored;

    @JsonProperty("minimumAvailability")
    private String minimumAvailability;

    @JsonProperty("isAvailable")
    private Boolean isAvailable;

    @JsonProperty("folderName")
    private String folderName;

    @JsonProperty("runtime")
    private Integer runtime;

    @JsonProperty("cleanTitle")
    private String cleanTitle;

    @JsonProperty("imdbId")
    private String imdbId;

    @JsonProperty("tmdbId")
    private Integer tmdbId;

    @JsonProperty("titleSlug")
    private String titleSlug;

    @JsonProperty("rootFolderPath")
    private String rootFolderPath;

    @JsonProperty("folder")
    private String folder;

    @JsonProperty("certification")
    private String certification;

    @JsonProperty("genres")
    private List<String> genres;

    @JsonProperty("keywords")
    private List<String> keywords;

    @JsonProperty("tags")
    private List<Integer> tags;

    @JsonProperty("added")
    private LocalDateTime added;

    @JsonProperty("popularity")
    private Float popularity;

    @JsonProperty("lastSearchTime")
    private LocalDateTime lastSearchTime;

    @JsonProperty("movieFile")
    private MovieFileResource movieFile;
}
