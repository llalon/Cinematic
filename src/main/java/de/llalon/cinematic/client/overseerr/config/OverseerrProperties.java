package de.llalon.cinematic.client.overseerr.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Configuration properties for connecting to the Overseerr API.
 *
 * <p>Use {@link #fromEnvironment()} to load values from the
 * {@code OVERSEERR_URL} and {@code OVERSEERR_API_KEY} environment variables,
 * or supply values explicitly via the {@link lombok.Builder}.</p>
 */
@Data
@Builder
@AllArgsConstructor
public class OverseerrProperties {
    private final String url;
    private final String apiKey;

    /**
     * Loads Overseerr connection properties from the {@code OVERSEERR_URL} and
     * {@code OVERSEERR_API_KEY} environment variables.
     *
     * @return an {@code OverseerrProperties} instance populated from environment
     */
    public static OverseerrProperties fromEnvironment() {
        return OverseerrProperties.builder()
                .url(System.getenv("OVERSEERR_URL"))
                .apiKey(System.getenv("OVERSEERR_API_KEY"))
                .build();
    }
}
