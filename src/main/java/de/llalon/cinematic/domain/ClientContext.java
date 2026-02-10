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
import java.util.function.Supplier;
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
        this.httpClient = httpClient == null ? new OkHttpClient() : httpClient;
        this.objectMapper = objectMapper == null
                ? JsonMapper.builder()
                        .addModule(new JavaTimeModule())
                        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                        .build()
                : objectMapper;

        // Radarr
        if (radarrClient != null) {
            this.radarrClient = radarrClient;
            this.radarrProperties = radarrProperties;
        } else {
            this.radarrProperties = radarrProperties != null
                    ? radarrProperties
                    : safeFromEnv(RadarrProperties::fromEnvironment, "Radarr");
            this.radarrClient = this.radarrProperties != null
                    ? new RadarrClient(this.httpClient, this.radarrProperties, this.objectMapper)
                    : null;
        }

        // Sonarr
        if (sonarrClient != null) {
            this.sonarrClient = sonarrClient;
            this.sonarrProperties = sonarrProperties;
        } else {
            this.sonarrProperties = sonarrProperties != null
                    ? sonarrProperties
                    : safeFromEnv(SonarrProperties::fromEnvironment, "Sonarr");
            this.sonarrClient = this.sonarrProperties != null
                    ? new SonarrClient(this.httpClient, this.sonarrProperties, this.objectMapper)
                    : null;
        }

        // QBittorrent
        if (qbittorrentClient != null) {
            this.qbittorrentClient = qbittorrentClient;
            this.qbittorrentProperties = qbittorrentProperties;
        } else {
            this.qbittorrentProperties = qbittorrentProperties != null
                    ? qbittorrentProperties
                    : safeFromEnv(QBittorrentProperties::fromEnvironment, "QBittorrent");
            this.qbittorrentClient = this.qbittorrentProperties != null
                    ? new QBittorrentClient(this.httpClient, this.qbittorrentProperties, this.objectMapper)
                    : null;
        }

        // Overseerr
        if (overseerrClient != null) {
            this.overseerrClient = overseerrClient;
            this.overseerrProperties = overseerrProperties;
        } else {
            this.overseerrProperties = overseerrProperties != null
                    ? overseerrProperties
                    : safeFromEnv(OverseerrProperties::fromEnvironment, "Overseerr");
            this.overseerrClient = this.overseerrProperties != null
                    ? new OverseerrClient(this.httpClient, this.overseerrProperties, this.objectMapper)
                    : null;
        }

        // Tautulli
        if (tautulliClient != null) {
            this.tautulliClient = tautulliClient;
            this.tautulliProperties = tautulliProperties;
        } else {
            this.tautulliProperties = tautulliProperties != null
                    ? tautulliProperties
                    : safeFromEnv(TautulliProperties::fromEnvironment, "Tautulli");
            this.tautulliClient = this.tautulliProperties != null
                    ? new TautulliClient(this.httpClient, this.tautulliProperties, this.objectMapper)
                    : null;
        }
    }

    private static <T> T safeFromEnv(Supplier<T> supplier, String name) {
        try {
            log.debug("Creating {} properties from environment variables", name);
            return supplier.get();
        } catch (IllegalArgumentException | NullPointerException exception) {
            log.warn("{} properties could not be initialized from environment variables", name, exception);
            return null;
        }
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
