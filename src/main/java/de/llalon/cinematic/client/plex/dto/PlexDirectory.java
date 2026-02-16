package de.llalon.cinematic.client.plex.dto;

import com.squareup.moshi.Json;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a Plex library section (directory).
 */
@Data
@AllArgsConstructor
public class PlexDirectory {

    @Json(name = "key")
    private final String key;

    @Json(name = "title")
    private final String title;

    @Json(name = "type")
    private final String type;

    @Json(name = "uuid")
    private final String uuid;
}
