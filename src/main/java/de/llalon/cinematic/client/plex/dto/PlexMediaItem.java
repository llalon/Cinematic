package de.llalon.cinematic.client.plex.dto;

import com.squareup.moshi.Json;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a single item returned from the Plex library API.
 */
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

    /** Plex rating key of the parent item (artist for albums, album for tracks). */
    @Json(name = "parentRatingKey")
    private final String parentRatingKey;

    /** Creates a media item without parent metadata, preserving the original constructor. */
    public PlexMediaItem(
            String ratingKey,
            String key,
            String guid,
            String type,
            String title,
            Integer year,
            String librarySectionTitle,
            List<PlexId> guids) {
        this(ratingKey, key, guid, type, title, year, librarySectionTitle, guids, null);
    }
}
