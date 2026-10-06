package de.llalon.cinematic.client.lidarr.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/** Lidarr ArtistResource API resource. */
@Data
@Builder
@AllArgsConstructor
public class ArtistResource {
    private final Integer id;
    private final String status;
    private final Boolean ended;
    private final String artistName;
    private final String foreignArtistId;
    private final String mbId;
    private final Integer tadbId;
    private final Integer discogsId;
    private final String allMusicId;
    private final String overview;
    private final String artistType;
    private final String disambiguation;
    private final List<Object> links;
    private final AlbumResource nextAlbum;
    private final AlbumResource lastAlbum;
    private final List<Object> images;
    private final List<Object> members;
    private final String remotePoster;
    private final String path;
    private final Integer qualityProfileId;
    private final Integer metadataProfileId;
    private final Boolean monitored;
    private final String monitorNewItems;
    private final String rootFolderPath;
    private final String folder;
    private final List<String> genres;
    private final String cleanName;
    private final String sortName;
    private final List<Integer> tags;
    private final LocalDateTime added;
    private final Object addOptions;
    private final Object ratings;
    private final Object statistics;
}
