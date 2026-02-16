package de.llalon.cinematic.domain;

import com.squareup.moshi.Moshi;
import de.llalon.cinematic.client.overseerr.OverseerrClient;
import de.llalon.cinematic.client.overseerr.config.OverseerrProperties;
import de.llalon.cinematic.client.plex.PlexClient;
import de.llalon.cinematic.client.plex.config.PlexProperties;
import de.llalon.cinematic.client.qbittorrent.QBittorrentClient;
import de.llalon.cinematic.client.qbittorrent.config.QBittorrentProperties;
import de.llalon.cinematic.client.radarr.RadarrClient;
import de.llalon.cinematic.client.radarr.config.RadarrProperties;
import de.llalon.cinematic.client.sonarr.SonarrClient;
import de.llalon.cinematic.client.sonarr.config.SonarrProperties;
import de.llalon.cinematic.client.tautulli.TautulliClient;
import de.llalon.cinematic.client.tautulli.config.TautulliProperties;
import de.llalon.cinematic.util.LenientDateTimeAdapter;
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
            PlexProperties plexProperties,
            RadarrProperties radarrProperties,
            SonarrProperties sonarrProperties,
            QBittorrentProperties qbittorrentProperties,
            OverseerrProperties overseerrProperties,
            TautulliProperties tautulliProperties,
            PlexClient plexClient,
            RadarrClient radarrClient,
            SonarrClient sonarrClient,
            QBittorrentClient qbittorrentClient,
            OverseerrClient overseerrClient,
            TautulliClient tautulliClient) {
        this.httpClient = httpClient == null ? new OkHttpClient() : httpClient;
        this.moshi = moshi == null
                ? new Moshi.Builder().add(new LenientDateTimeAdapter()).build()
                : moshi;

        // Plex
        if (radarrClient != null) {
            this.plexClient = plexClient;
            this.plexProperties = plexProperties;
        } else {
            this.plexProperties = plexProperties != null ? plexProperties : PlexProperties.fromEnvironment();
            this.plexClient = this.plexProperties.getUrl() != null
                    ? new PlexClient(this.httpClient, this.plexProperties, this.moshi)
                    : null;
        }

        // Radarr
        if (radarrClient != null) {
            this.radarrClient = radarrClient;
            this.radarrProperties = radarrProperties;
        } else {
            this.radarrProperties = radarrProperties != null ? radarrProperties : RadarrProperties.fromEnvironment();
            this.radarrClient = this.radarrProperties.getUrl() != null
                    ? new RadarrClient(this.httpClient, this.radarrProperties, this.moshi)
                    : null;
        }

        // Sonarr
        if (sonarrClient != null) {
            this.sonarrClient = sonarrClient;
            this.sonarrProperties = sonarrProperties;
        } else {
            this.sonarrProperties = sonarrProperties != null ? sonarrProperties : SonarrProperties.fromEnvironment();
            this.sonarrClient = this.sonarrProperties.getUrl() != null
                    ? new SonarrClient(this.httpClient, this.sonarrProperties, this.moshi)
                    : null;
        }

        // QBittorrent
        if (qbittorrentClient != null) {
            this.qbittorrentClient = qbittorrentClient;
            this.qbittorrentProperties = qbittorrentProperties;
        } else {
            this.qbittorrentProperties =
                    qbittorrentProperties != null ? qbittorrentProperties : QBittorrentProperties.fromEnvironment();
            this.qbittorrentClient = this.qbittorrentProperties.getUrl() != null
                    ? new QBittorrentClient(this.httpClient, this.qbittorrentProperties, this.moshi)
                    : null;
        }

        // Overseerr
        if (overseerrClient != null) {
            this.overseerrClient = overseerrClient;
            this.overseerrProperties = overseerrProperties;
        } else {
            this.overseerrProperties =
                    overseerrProperties != null ? overseerrProperties : OverseerrProperties.fromEnvironment();
            this.overseerrClient = this.overseerrProperties.getUrl() != null
                    ? new OverseerrClient(this.httpClient, this.overseerrProperties, this.moshi)
                    : null;
        }

        // Tautulli
        if (tautulliClient != null) {
            this.tautulliClient = tautulliClient;
            this.tautulliProperties = tautulliProperties;
        } else {
            this.tautulliProperties =
                    tautulliProperties != null ? tautulliProperties : TautulliProperties.fromEnvironment();
            this.tautulliClient = this.tautulliProperties.getUrl() != null
                    ? new TautulliClient(this.httpClient, this.tautulliProperties, this.moshi)
                    : null;
        }

        if (this.tautulliClient == null) {
            log.warn("Tautulli not configured. Some features may not be available");
        }
        if (this.overseerrClient == null) {
            log.warn("Overseerr not configured. Some features may not be available");
        }
        if (this.radarrClient == null) {
            log.warn("Radarr not configured. Some features may not be available");
        }
        if (this.sonarrClient == null) {
            log.warn("Sonarr not configured. Some features may not be available");
        }
        if (this.qbittorrentClient == null) {
            log.warn("QBittorrent not configured. Some features may not be available");
        }
    }

    private final OkHttpClient httpClient;
    private final Moshi moshi;
    private final PlexProperties plexProperties;
    private final RadarrProperties radarrProperties;
    private final SonarrProperties sonarrProperties;
    private final QBittorrentProperties qbittorrentProperties;
    private final OverseerrProperties overseerrProperties;
    private final TautulliProperties tautulliProperties;

    private final PlexClient plexClient;
    private final RadarrClient radarrClient;
    private final SonarrClient sonarrClient;
    private final QBittorrentClient qbittorrentClient;
    private final OverseerrClient overseerrClient;
    private final TautulliClient tautulliClient;
}
