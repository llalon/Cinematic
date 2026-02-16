package de.llalon.cinematic.client.plex.dto;

import com.squareup.moshi.Json;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlexMediaContainerWrapper {

    @Json(name = "MediaContainer")
    private final PlexMetadataContainer mediaContainer;
}
