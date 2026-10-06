package de.llalon.cinematic.client.lidarr.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Configuration properties for connecting to the Lidarr API.
 *
 * <p>Use {@link #fromEnvironment()} to load values from the
 * {@code LIDARR_URL} and {@code LIDARR_API_KEY} environment variables,
 * or supply values explicitly via the {@link lombok.Builder}.</p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LidarrProperties {
    private String url;
    private String apiKey;

    /**
     * Loads Lidarr connection properties from the {@code LIDARR_URL} and
     * {@code LIDARR_API_KEY} environment variables.
     *
     * @return a {@code LidarrProperties} instance populated from environment
     */
    public static LidarrProperties fromEnvironment() {
        return LidarrProperties.builder()
                .url(System.getenv("LIDARR_URL"))
                .apiKey(System.getenv("LIDARR_API_KEY"))
                .build();
    }
}
