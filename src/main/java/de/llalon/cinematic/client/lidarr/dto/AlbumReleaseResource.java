package de.llalon.cinematic.client.lidarr.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/** Edition of a Lidarr album, including its MusicBrainz release ID. */
@Data
@Builder
@AllArgsConstructor
public class AlbumReleaseResource {
    private final Integer id;
    private final Integer albumId;
    private final String foreignReleaseId;
    private final String title;
    private final String status;
    private final Integer duration;
    private final Integer trackCount;
    private final List<Object> media;
    private final Integer mediumCount;
    private final String disambiguation;
    private final List<String> country;
    private final List<String> label;
    private final String format;
    private final Boolean monitored;
}
