package de.llalon.cinematic.client.plex.dto;

import com.squareup.moshi.Json;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlexMediaItem {

    @Json(name = "ratingKey")
    private final String ratingKey;

    @Json(name = "key")
    private final String key;

    @Json(name = "guid")
    private final String guid;

    @Json(name = "type")
    private final String type;

    @Json(name = "title")
    private final String title;

    @Json(name = "year")
    private final Integer year;

    @Json(name = "librarySectionTitle")
    private final String librarySectionTitle;

    @Json(name = "Guid")
    private final List<PlexId> guids;

    @Json(name = "Media")
    private final List<PlexMedia> media;

    @Json(name = "index")
    private final Integer index;

    @Json(name = "parentIndex")
    private final Integer parentIndex;

    @Json(name = "parentRatingKey")
    private final String parentRatingKey;
}
