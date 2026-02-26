package de.llalon.cinematic.client.radarr.dto;

import com.squareup.moshi.Json;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a tag in Radarr.
 */
@Data
@AllArgsConstructor
public class RadarrTag {

    @Json(name = "id")
    private final Integer id;

    @Json(name = "label")
    private final String label;
}
