package de.llalon.cinematic.client.seerr.dto;

import com.squareup.moshi.Json;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a TV season in Seerr requests.
 */
@Data
@AllArgsConstructor
public class Season {
    private final Integer id;

    @Json(name = "seasonNumber")
    private final Integer seasonNumber;

    private final Integer status;
}
