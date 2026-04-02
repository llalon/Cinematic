package de.llalon.cinematic.client.plex.dto;

import com.squareup.moshi.Json;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a single file part within a Plex media version.
 */
@Data
@AllArgsConstructor
public class PlexPart {

    @Json(name = "id")
    private final Integer id;

    @Json(name = "key")
    private final String key;

    @Json(name = "file")
    private final String file;

    @Json(name = "size")
    private final Long size;

    @Json(name = "container")
    private final String container;
}
