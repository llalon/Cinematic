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
                        .url("http://localhost:8080")
                        .apiKey("test")
                        .build())
                .radarrProperties(RadarrProperties.fromEnvironment())
                .qbittorrentProperties(QBittorrentProperties.fromEnvironment())
                .tautulliProperties(TautulliProperties.fromEnvironment())
                .overseerrProperties(OverseerrProperties.fromEnvironment())
                .build()
                .register();

        Assumptions.assumeFalse(ClientContext.getInstance() == null);
    }

    @Test
    void todo() {
        // ToDo: Configure mock web server....
    }
}
