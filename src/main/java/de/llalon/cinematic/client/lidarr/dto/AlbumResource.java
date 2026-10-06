package de.llalon.cinematic.client.lidarr.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/** Lidarr AlbumResource API resource. */
@Data
@Builder
@AllArgsConstructor
public class AlbumResource {
    private final Integer id;
    private final String title;
    private final String disambiguation;
    private final String overview;
    private final Integer artistId;
    private final String foreignAlbumId;
    private final Boolean monitored;
    private final Boolean anyReleaseOk;
    private final Integer profileId;
    private final Integer duration;
    private final String albumType;
    private final List<String> secondaryTypes;
    private final Integer mediumCount;
    private final Object ratings;
    private final LocalDateTime releaseDate;
    private final List<AlbumReleaseResource> releases;
    private final List<String> genres;
    private final List<Object> media;
    private final ArtistResource artist;
    private final List<Object> images;
    private final List<Object> links;
    private final LocalDateTime lastSearchTime;
    private final Object statistics;
    private final Object addOptions;
    private final String remoteCover;
}
