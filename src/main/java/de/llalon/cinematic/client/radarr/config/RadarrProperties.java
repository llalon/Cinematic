package de.llalon.cinematic.client.radarr.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class RadarrProperties {
    private final String url;
    private final String apiKey;

    public static RadarrProperties fromEnvironment() {
        return RadarrProperties.builder()
                .url(System.getenv("RADARR_URL"))
                .apiKey(System.getenv("RADARR_API_KEY"))
                .build();
    }
}
