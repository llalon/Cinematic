package de.llalon.cinematic.client.sonarr.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SonarrProperties {
    private final String url;
    private final String apiKey;

    public static SonarrProperties fromEnvironment() {
        return SonarrProperties.builder()
                .url(System.getenv("SONARR_URL"))
                .apiKey(System.getenv("SONARR_API_KEY"))
                .build();
    }
}
