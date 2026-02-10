package de.llalon.cinematic;

import de.llalon.cinematic.client.overseerr.config.OverseerrProperties;
import de.llalon.cinematic.client.qbittorrent.config.QBittorrentProperties;
import de.llalon.cinematic.client.radarr.config.RadarrProperties;
import de.llalon.cinematic.client.sonarr.config.SonarrProperties;
import de.llalon.cinematic.client.tautulli.config.TautulliProperties;
import de.llalon.cinematic.domain.ClientContext;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;

class CinematicTests {

    static MockWebServer server;

    @BeforeAll
    static void setUp() {
        ClientContext.builder()
                .sonarrProperties(SonarrProperties.builder()
                        .url("http://localhost:8989")
                        .apiKey("test")
                        .build())
                .radarrProperties(RadarrProperties.builder()
                        .url("http://localhost:7878")
                        .apiKey("test")
                        .build())
                .qbittorrentProperties(QBittorrentProperties.builder()
                        .url("http://localhost:7878")
                        .username("user")
                        .password("pass")
                        .build())
                .tautulliProperties(TautulliProperties.builder()
                        .url("http://localhost:8181")
                        .apiKey("test")
                        .build())
                .overseerrProperties(OverseerrProperties.builder()
                        .url("http://localhost:5055")
                        .apiKey("test")
                        .build())
                .build()
                .register();
    }

    @Test
    void todo() {
        // ToDo: Configure mock web server....
    }
}
