package de.llalon.cinematic.client.plex.dto;

import com.squareup.moshi.Json;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a single media version (e.g. 1080p, 4K) within a Plex metadata item.
 *
 * <p>A Plex item can contain multiple {@code Media} entries when the user has added
 * several quality versions of the same title. Each media entry describes the video
 * and audio characteristics and contains one or more {@link PlexPart} file references.</p>
 */
@Data
@AllArgsConstructor
public class PlexMedia {

    @Json(name = "id")
    private final Integer id;

    @Json(name = "duration")
    private final Long duration;

    @Json(name = "bitrate")
    private final Integer bitrate;

    @Json(name = "width")
    private final Integer width;

    @Json(name = "height")
    private final Integer height;

    @Json(name = "videoResolution")
    private final String videoResolution;

    @Json(name = "videoCodec")
    private final String videoCodec;

    @Json(name = "audioCodec")
    private final String audioCodec;

    @Json(name = "audioChannels")
    private final Integer audioChannels;

    @Json(name = "container")
    private final String container;

    @Json(name = "Part")
    private final List<PlexPart> parts;
}
