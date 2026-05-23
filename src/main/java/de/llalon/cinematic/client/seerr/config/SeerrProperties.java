package de.llalon.cinematic.client.seerr.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Configuration properties for connecting to the Seerr API.
 *
 * <p>Use {@link #fromEnvironment()} to load values from the
 * {@code SEERR_URL} and {@code SEERR_API_KEY} environment variables,
 * or supply values explicitly via the {@link lombok.Builder}.</p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeerrProperties {
    private String url;
    private String apiKey;

    /**
     * Loads Seerr connection properties from the {@code SEERR_URL} and
     * {@code SEERR_API_KEY} environment variables.
     *
     * @return an {@code SeerrProperties} instance populated from environment
     */
    public static SeerrProperties fromEnvironment() {
        return SeerrProperties.builder()
                .url(System.getenv("SEERR_URL"))
                .apiKey(System.getenv("SEERR_API_KEY"))
                .build();
    }
}
