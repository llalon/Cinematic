package de.llalon.cinematic.client.lidarr.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/** Lidarr TagResource API resource. */
@Data
@Builder
@AllArgsConstructor
public class LidarrTag {
    private final Integer id;
    private final String label;
}
