package de.llalon.cinematic.client.sonarr.dto;

import com.squareup.moshi.Json;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a tag in Sonarr.
 */
@Data
@AllArgsConstructor
public class TagResource {

    @Json(name = "id")
    private final Integer id;

    @Json(name = "label")
    private final String label;
}
