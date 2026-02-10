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
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

@Slf4j
@Getter
@Builder
public final class ClientContext {

    public ClientContext(
            OkHttpClient httpClient,
            ObjectMapper objectMapper,
            RadarrProperties radarrProperties,
            SonarrProperties sonarrProperties,
            QBittorrentProperties qbittorrentProperties,
            OverseerrProperties overseerrProperties,
            TautulliProperties tautulliProperties,
            RadarrClient radarrClient,
            SonarrClient sonarrClient,
            QBittorrentClient qbittorrentClient,
            OverseerrClient overseerrClient,
            TautulliClient tautulliClient) {
        this.objectMapper = objectMapper == null
                ? JsonMapper.builder()
                        .addModule(new JavaTimeModule())
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .build()
                : objectMapper;

        this.httpClient = httpClient == null ? new OkHttpClient() : httpClient;

        this.radarrProperties = radarrProperties == null ? RadarrProperties.fromEnvironment() : radarrProperties;
        this.sonarrProperties = sonarrProperties == null ? SonarrProperties.fromEnvironment() : sonarrProperties;
        this.qbittorrentProperties =
                qbittorrentProperties == null ? QBittorrentProperties.fromEnvironment() : qbittorrentProperties;
        this.overseerrProperties =
                overseerrProperties == null ? OverseerrProperties.fromEnvironment() : overseerrProperties;
        this.tautulliProperties =
                tautulliProperties == null ? TautulliProperties.fromEnvironment() : tautulliProperties;

        this.radarrClient = radarrClient == null
                ? new RadarrClient(this.httpClient, this.radarrProperties, this.objectMapper)
                : radarrClient;
        this.sonarrClient = sonarrClient == null
                ? new SonarrClient(this.httpClient, this.sonarrProperties, this.objectMapper)
                : sonarrClient;
        this.qbittorrentClient = qbittorrentClient == null
                ? new QBittorrentClient(this.httpClient, this.qbittorrentProperties, this.objectMapper)
                : qbittorrentClient;
        this.overseerrClient = overseerrClient == null
                ? new OverseerrClient(this.httpClient, this.overseerrProperties, this.objectMapper)
                : overseerrClient;
        this.tautulliClient = tautulliClient == null
                ? new TautulliClient(this.httpClient, this.tautulliProperties, this.objectMapper)
                : tautulliClient;
    }

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

    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private final RadarrProperties radarrProperties;
    private final SonarrProperties sonarrProperties;
    private final QBittorrentProperties qbittorrentProperties;
    private final OverseerrProperties overseerrProperties;
    private final TautulliProperties tautulliProperties;

    private final RadarrClient radarrClient;
    private final SonarrClient sonarrClient;
    private final QBittorrentClient qbittorrentClient;
    private final OverseerrClient overseerrClient;
    private final TautulliClient tautulliClient;
}
