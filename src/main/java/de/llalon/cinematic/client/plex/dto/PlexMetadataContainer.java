package de.llalon.cinematic.client.plex.dto;

import com.squareup.moshi.Json;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Contains the metadata entries returned for a Plex library section.
 */
@Data
@AllArgsConstructor
public class PlexMetadataContainer {

    @Json(name = "size")
    private final Integer size;

    @Json(name = "Metadata")
    private final List<PlexMediaItem> metadata;
}
