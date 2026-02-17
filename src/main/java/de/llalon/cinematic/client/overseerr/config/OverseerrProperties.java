package de.llalon.cinematic.client.overseerr.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class OverseerrProperties {
    private final String url;
    private final String apiKey;

    public static OverseerrProperties fromEnvironment() {
        return OverseerrProperties.builder()
                .url(System.getenv("OVERSEERR_URL"))
                .apiKey(System.getenv("OVERSEERR_API_KEY"))
                .build();
    }
}
