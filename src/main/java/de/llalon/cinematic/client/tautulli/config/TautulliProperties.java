package de.llalon.cinematic.client.tautulli.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Configuration properties for connecting to the Tautulli API.
 *
 * <p>Use {@link #fromEnvironment()} to load values from the
 * {@code TAUTULLI_URL} and {@code TAUTULLI_API_KEY} environment variables,
 * or supply values explicitly via the {@link lombok.Builder}.</p>
 */
@Data
@Builder
@AllArgsConstructor
public class TautulliProperties {
    private final String url;
    private final String apiKey;

    /**
     * Loads Tautulli connection properties from the {@code TAUTULLI_URL} and
     * {@code TAUTULLI_API_KEY} environment variables.
     *
     * @return a {@code TautulliProperties} instance populated from environment
     */
    public static TautulliProperties fromEnvironment() {
        return TautulliProperties.builder()
                .url(System.getenv("TAUTULLI_URL"))
                .apiKey(System.getenv("TAUTULLI_API_KEY"))
                .build();
    }
}
