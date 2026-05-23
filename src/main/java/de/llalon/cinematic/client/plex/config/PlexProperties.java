package de.llalon.cinematic.client.plex.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Configuration properties for connecting to the Plex Media Server API.
 *
 * <p>Use {@link #fromEnvironment()} to load values from the
 * {@code PLEX_URL} and {@code PLEX_API_KEY} environment variables,
 * or supply values explicitly via the {@link lombok.Builder}.</p>
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlexProperties {
    private String url;
    private String token;

    /**
     * Loads Plex connection properties from the {@code PLEX_URL} and
     * {@code PLEX_API_KEY} environment variables.
     *
     * @return a {@code PlexProperties} instance populated from environment
     */
    public static PlexProperties fromEnvironment() {
        return PlexProperties.builder()
                .url(System.getenv("PLEX_URL"))
                .token(System.getenv("PLEX_API_KEY"))
                .build();
    }
}
