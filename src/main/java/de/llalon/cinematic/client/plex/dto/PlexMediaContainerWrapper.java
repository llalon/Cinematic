package de.llalon.cinematic.client.plex.dto;

import com.squareup.moshi.Json;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Wraps a Plex {@code MediaContainer} response payload.
 *
 * @param <T> the wrapped container type
 */
@Data
@AllArgsConstructor
public class PlexMediaContainerWrapper<T> {

    @Json(name = "MediaContainer")
    private final T mediaContainer;
}
