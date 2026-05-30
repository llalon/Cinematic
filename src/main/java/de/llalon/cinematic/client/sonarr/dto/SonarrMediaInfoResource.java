package de.llalon.cinematic.client.sonarr.dto;

import com.squareup.moshi.Json;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Technical media information reported by Sonarr for imported episode files.
 */
@Data
@AllArgsConstructor
public class SonarrMediaInfoResource {

    @Json(name = "id")
    private final Integer id;

    @Json(name = "audioBitrate")
    private final Long audioBitrate;

    @Json(name = "audioChannels")
    private final Double audioChannels;

    @Json(name = "audioCodec")
    private final String audioCodec;

    @Json(name = "audioLanguages")
    private final String audioLanguages;

    @Json(name = "audioStreamCount")
    private final Integer audioStreamCount;

    @Json(name = "videoBitDepth")
    private final Integer videoBitDepth;

    @Json(name = "videoBitrate")
    private final Long videoBitrate;

    @Json(name = "videoCodec")
    private final String videoCodec;

    @Json(name = "videoFps")
    private final Double videoFps;

    @Json(name = "videoDynamicRange")
    private final String videoDynamicRange;

    @Json(name = "videoDynamicRangeType")
    private final String videoDynamicRangeType;

    @Json(name = "resolution")
    private final String resolution;

    @Json(name = "runTime")
    private final String runTime;

    @Json(name = "scanType")
    private final String scanType;

    @Json(name = "subtitles")
    private final String subtitles;
}
