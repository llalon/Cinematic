package de.llalon.cinematic.client.sonarr.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Configuration properties for connecting to the Sonarr API.
 *
 * <p>Use {@link #fromEnvironment()} to load values from the
 * {@code SONARR_URL} and {@code SONARR_API_KEY} environment variables,
 * or supply values explicitly via the {@link lombok.Builder}.</p>
 */
@Data
@Builder
@AllArgsConstructor
public class SonarrProperties {
    private final String url;
    private final String apiKey;

    /**
     * Loads Sonarr connection properties from the {@code SONARR_URL} and
     * {@code SONARR_API_KEY} environment variables.
     *
     * @return a {@code SonarrProperties} instance populated from environment
     */
    public static SonarrProperties fromEnvironment() {
        return SonarrProperties.builder()
                .url(System.getenv("SONARR_URL"))
                .apiKey(System.getenv("SONARR_API_KEY"))
                .build();
    }
}
