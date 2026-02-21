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
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Slf4j
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
        if (plexClient != null) {
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

        if (this.plexClient == null) {
            log.warn("Plex not configured. Some features may not be available");
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

    /**
     * The HTTP client used for API calls.
     */
    private final OkHttpClient httpClient;

    /**
     * The Moshi instance for JSON serialization/deserialization.
     */
    private final Moshi moshi;

    /**
     * The Plex properties for configuration.
     */
    private final PlexProperties plexProperties;

    /**
     * The Radarr properties for configuration.
     */
    private final RadarrProperties radarrProperties;

    /**
     * The Sonarr properties for configuration.
     */
    private final SonarrProperties sonarrProperties;

    /**
     * The QBittorrent properties for configuration.
     */
    private final QBittorrentProperties qbittorrentProperties;

    /**
     * The Overseerr properties for configuration.
     */
    private final OverseerrProperties overseerrProperties;

    /**
     * The Tautulli properties for configuration.
     */
    private final TautulliProperties tautulliProperties;

    /**
     * The Plex client instance.
     */
    private final PlexClient plexClient;

    /**
     * The Radarr client instance.
     */
    private final RadarrClient radarrClient;

    /**
     * The Sonarr client instance.
     */
    private final SonarrClient sonarrClient;

    /**
     * The QBittorrent client instance.
     */
    private final QBittorrentClient qbittorrentClient;

    /**
     * The Overseerr client instance.
     */
    private final OverseerrClient overseerrClient;

    /**
     * The Tautulli client instance.
     */
    private final TautulliClient tautulliClient;

    @NotNull
    public OkHttpClient getHttpClient() {
        if (this.httpClient == null) {
            throw new IllegalStateException("Configured http client is invalid");
        }
        return this.httpClient;
    }

    @NotNull
    public Moshi getMoshi() {
        if (this.moshi == null) {
            throw new IllegalStateException("Configured http client is invalid");
        }

        return this.moshi;
    }

    @Nullable
    public PlexProperties getPlexProperties() {
        return this.plexProperties;
    }

    @Nullable
    public RadarrProperties getRadarrProperties() {
        return this.radarrProperties;
    }

    @Nullable
    public SonarrProperties getSonarrProperties() {
        return this.sonarrProperties;
    }

    @Nullable
    public QBittorrentProperties getQbittorrentProperties() {
        return this.qbittorrentProperties;
    }

    @Nullable
    public OverseerrProperties getOverseerrProperties() {
        return this.overseerrProperties;
    }

    @Nullable
    public TautulliProperties getTautulliProperties() {
        return this.tautulliProperties;
    }

    @NotNull
    public PlexClient getPlexClient() {
        if (this.plexClient == null) {
            throw new ClientNotConfiguredException("Plex not configured");
        }
        return this.plexClient;
    }

    @NotNull
    public RadarrClient getRadarrClient() {
        if (this.radarrClient == null) {
            throw new ClientNotConfiguredException("Radarr not configured");
        }
        return this.radarrClient;
    }

    @NotNull
    public SonarrClient getSonarrClient() {
        if (this.sonarrClient == null) {
            throw new ClientNotConfiguredException("Sonarr not configured");
        }
        return this.sonarrClient;
    }

    @NotNull
    public QBittorrentClient getQbittorrentClient() {
        if (this.qbittorrentClient == null) {
            throw new ClientNotConfiguredException("QBittorrent not configured");
        }
        return this.qbittorrentClient;
    }

    @NotNull
    public OverseerrClient getOverseerrClient() {
        if (this.overseerrClient == null) {
            throw new ClientNotConfiguredException("Overseerr not configured");
        }
        return this.overseerrClient;
    }

    @NotNull
    public TautulliClient getTautulliClient() {
        if (this.tautulliClient == null) {
            throw new ClientNotConfiguredException("Tautulli not configured");
        }
        return this.tautulliClient;
    }
}
