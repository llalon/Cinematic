package de.llalon.cinematic.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import de.llalon.cinematic.client.overseerr.OverseerrClient;
import de.llalon.cinematic.client.overseerr.config.OverseerrProperties;
import de.llalon.cinematic.client.qbittorrent.QBittorrentClient;
import de.llalon.cinematic.client.qbittorrent.config.QBittorrentProperties;
import de.llalon.cinematic.client.radarr.RadarrClient;
import de.llalon.cinematic.client.radarr.config.RadarrProperties;
import de.llalon.cinematic.client.sonarr.SonarrClient;
import de.llalon.cinematic.client.sonarr.config.SonarrProperties;
import de.llalon.cinematic.client.tautulli.TautulliClient;
import de.llalon.cinematic.client.tautulli.config.TautulliProperties;
import lombok.Builder;
import lombok.Getter;
import okhttp3.OkHttpClient;

@Getter
@Builder
public final class ClientContext {

    private static ClientContext INSTANCE;

    public static ClientContext getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("Context not initialized");
        }
        return INSTANCE;
    }

    public void register() {
        INSTANCE = this;
    }

    @Builder.Default
    private final OkHttpClient httpClient = defaultOkHttpClient();

    @Builder.Default
    private final ObjectMapper objectMapper = defaultObjectMapper();

    @Builder.Default
    private final RadarrProperties radarrProperties = RadarrProperties.fromEnvironment();

    @Builder.Default
    private final SonarrProperties sonarrProperties = SonarrProperties.fromEnvironment();

    @Builder.Default
    private final QBittorrentProperties qbittorrentProperties = QBittorrentProperties.fromEnvironment();

    @Builder.Default
    private final OverseerrProperties overseerrProperties = OverseerrProperties.fromEnvironment();

    @Builder.Default
    private final TautulliProperties tautulliProperties = TautulliProperties.fromEnvironment();

    private final RadarrClient radarrClient;
    private final SonarrClient sonarrClient;
    private final QBittorrentClient qbittorrentClient;
    private final OverseerrClient overseerrClient;
    private final TautulliClient tautulliClient;

    private static ObjectMapper defaultObjectMapper() {
        return JsonMapper.builder()
                .addModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();
    }

    private static OkHttpClient defaultOkHttpClient() {
        return new OkHttpClient();
    }
}
