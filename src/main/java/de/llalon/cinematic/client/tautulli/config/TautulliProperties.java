package de.llalon.cinematic.client.tautulli.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class TautulliProperties {
    private final String url;
    private final String apiKey;

    public static TautulliProperties fromEnvironment() {
        return TautulliProperties.builder()
                .url(System.getenv("TAUTULLI_URL"))
                .apiKey(System.getenv("TAUTULLI_API_KEY"))
                .build();
    }
}
