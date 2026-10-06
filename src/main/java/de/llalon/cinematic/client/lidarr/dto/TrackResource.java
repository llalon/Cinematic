package de.llalon.cinematic.client.lidarr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/** Lidarr TrackResource API resource. */
@Data
@Builder
@AllArgsConstructor
public class TrackResource {
    private final Integer id;
    private final Integer artistId;
    private final String foreignTrackId;
    private final String foreignRecordingId;
    private final Integer trackFileId;
    private final Integer albumId;
    private final Boolean explicit;
    private final Integer absoluteTrackNumber;
    private final String trackNumber;
    private final String title;
    private final Integer duration;
    private final TrackFileResource trackFile;
    private final Integer mediumNumber;
    private final Boolean hasFile;
    private final ArtistResource artist;
    private final Object ratings;
}
