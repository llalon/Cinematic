package de.llalon.cinematic.client.radarr.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Configuration properties for connecting to the Radarr API.
 *
 * <p>Use {@link #fromEnvironment()} to load values from the
 * {@code RADARR_URL} and {@code RADARR_API_KEY} environment variables,
 * or supply values explicitly via the {@link lombok.Builder}.</p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RadarrProperties {
    private String url;
    private String apiKey;

    /**
     * Loads Radarr connection properties from the {@code RADARR_URL} and
     * {@code RADARR_API_KEY} environment variables.
     *
     * @return a {@code RadarrProperties} instance populated from environment
     */
    public static RadarrProperties fromEnvironment() {
        return RadarrProperties.builder()
                .url(System.getenv("RADARR_URL"))
                .apiKey(System.getenv("RADARR_API_KEY"))
                .build();
    }
}
