package de.llalon.cinematic.client.plex.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PlexProperties {
    private final String url;
    private final String token;

    public static PlexProperties fromEnvironment() {
        return PlexProperties.builder()
                .url(System.getenv("PLEX_URL"))
                .token(System.getenv("PLEX_TOKEN"))
                .build();
    }
}
