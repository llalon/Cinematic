package de.llalon.cinematic.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;

@Slf4j
public enum ClientContextHolder {
    QBITTORRENT,
    SONARR,
    RADARR,
    TAUTULI,
    PLEX;

    private static ObjectMapper objectMapper;

    private static OkHttpClient okHttpClient;

    private static OverseerrClient overseerrClient;

    private static QBittorrentClient qbittorrentClient;

    private static RadarrClient radarrClient;

    private static SonarrClient sonarrClient;

    private static TautulliClient tautulliClient;

    private static void configureQBittorrent(Properties properties) {
        final String url = properties.getProperty("qbittorrent.url");
        if (url != null && !url.isEmpty()) {
            final String username = properties.getProperty("qbittorrent.username");
            final String password = properties.getProperty("qbittorrent.password");

            QBittorrentProperties config = new QBittorrentProperties();

            config.setUrl(url);
            config.setUsername(username);
            config.setPassword(password);

            log.debug("Configuring QBittorrent with url: {}", url);

            qbittorrentClient = new QBittorrentClient(okHttpClient, config, objectMapper);
        }
    }

    private static void configureOverseerr(Properties properties) {
        final String url = properties.getProperty("overseerr.url");
        if (url != null && !url.isEmpty()) {
            final String apiKey = properties.getProperty("overseerr.api_key");

            OverseerrProperties config = new OverseerrProperties();
            config.setUrl(url);
            config.setApiKey(apiKey);

            log.debug("Configuring Overseerr with url: {}", url);

            overseerrClient = new OverseerrClient(okHttpClient, config, objectMapper);
        }
    }

    private static void configureSonarr(Properties properties) {
        final String url = properties.getProperty("sonarr.url");
        if (url != null && !url.isEmpty()) {
            final String apiKey = properties.getProperty("sonarr.api_key");

            SonarrProperties config = new SonarrProperties();
            config.setUrl(url);
            config.setApiKey(apiKey);

            log.debug("Configuring Sonarr with url: {}", url);

            sonarrClient = new SonarrClient(okHttpClient, config, objectMapper);
        }
    }

    private static void configureRadarr(Properties properties) {
        final String url = properties.getProperty("radarr.url");
        if (url != null && !url.isEmpty()) {
            final String apiKey = properties.getProperty("radarr.api_key");

            RadarrProperties config = new RadarrProperties();
            config.setUrl(url);
            config.setApiKey(apiKey);

            log.debug("Configuring Radarr with url: {}", url);

            radarrClient = new RadarrClient(okHttpClient, config, objectMapper);
        }
    }

    private static void configureTautulli(Properties properties) {
        final String url = properties.getProperty("tautulli.url");
        if (url != null && !url.isEmpty()) {
            final String apiKey = properties.getProperty("tautulli.api_key");

            TautulliProperties config = new TautulliProperties();
            config.setUrl(url);
            config.setApiKey(apiKey);

            log.debug("Configuring Tautulli with url: {}", url);

            tautulliClient = new TautulliClient(okHttpClient, config, objectMapper);
        }
    }

    public static void configure(Properties properties) {
        okHttpClient = new OkHttpClient();
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        configureQBittorrent(properties);
        configureOverseerr(properties);
        configureSonarr(properties);
        configureRadarr(properties);
        configureTautulli(properties);
    }

    public static void configure() {
        final Map<String, String> environmentVariables = System.getenv();
        final Properties properties = new Properties();
        environmentVariables.forEach((key, value) -> {
            final var newKey = key.toLowerCase().replaceFirst("_", ".");
            properties.put(newKey, value);
        });
        configure(properties);
    }

    public static RadarrClient getRadarrClient() {
        if (radarrClient == null) {
            throw new IllegalStateException("Radarr client has not been configured");
        }
        return radarrClient;
    }

    public static SonarrClient getSonarrClient() {
        if (sonarrClient == null) {
            throw new IllegalStateException("Sonarr client has not been configured");
        }
        return sonarrClient;
    }

    public static QBittorrentClient getQbittorrentClient() {
        if (qbittorrentClient == null) {
            throw new IllegalStateException("QBittorrent client has not been configured");
        }
        return qbittorrentClient;
    }
}
