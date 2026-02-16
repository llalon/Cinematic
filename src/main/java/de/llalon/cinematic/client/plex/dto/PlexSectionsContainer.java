package de.llalon.cinematic.client.plex.dto;

import com.squareup.moshi.Json;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Container for Plex library sections response.
 */
@Data
@AllArgsConstructor
public class PlexSectionsContainer {

    @Json(name = "size")
    private final Integer size;

    @Json(name = "Directory")
    private final List<PlexDirectory> directories;
}
