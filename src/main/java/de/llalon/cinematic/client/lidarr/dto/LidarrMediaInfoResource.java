package de.llalon.cinematic.client.lidarr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/** Lidarr MediaInfoResource API resource. */
@Data
@Builder
@AllArgsConstructor
public class LidarrMediaInfoResource {
    private final Integer id;
    private final Double audioChannels;
    private final String audioBitRate;
    private final String audioCodec;
    private final String audioBits;
    private final String audioSampleRate;
}
