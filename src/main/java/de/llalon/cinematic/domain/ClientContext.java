package de.llalon.cinematic.domain;

import com.squareup.moshi.Moshi;
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
import de.llalon.cinematic.util.LenientDateTimeAdapter;
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
            Moshi moshi,
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
        this.moshi = moshi == null
                ? new Moshi.Builder().add(new LenientDateTimeAdapter()).build()
                : moshi;

        // Radarr
        if (radarrClient != null) {
            this.radarrClient = radarrClient;
            this.radarrProperties = radarrProperties;
        } else {
            this.radarrProperties = radarrProperties != null
                    ? radarrProperties
                    : safeFromEnv(RadarrProperties::fromEnvironment, "Radarr");
            this.radarrClient = this.radarrProperties != null
                    ? new RadarrClient(this.httpClient, this.radarrProperties, this.moshi)
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
                    ? new SonarrClient(this.httpClient, this.sonarrProperties, this.moshi)
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
                    ? new QBittorrentClient(this.httpClient, this.qbittorrentProperties, this.moshi)
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
                    ? new OverseerrClient(this.httpClient, this.overseerrProperties, this.moshi)
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
                    ? new TautulliClient(this.httpClient, this.tautulliProperties, this.moshi)
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

    private final OkHttpClient httpClient;
    private final Moshi moshi;
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
