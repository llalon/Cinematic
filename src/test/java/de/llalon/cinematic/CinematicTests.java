package de.llalon.cinematic;

import de.llalon.cinematic.client.overseerr.config.OverseerrProperties;
import de.llalon.cinematic.client.plex.config.PlexProperties;
import de.llalon.cinematic.client.qbittorrent.config.QBittorrentProperties;
import de.llalon.cinematic.client.radarr.config.RadarrProperties;
import de.llalon.cinematic.client.sonarr.config.SonarrProperties;
import de.llalon.cinematic.client.tautulli.config.TautulliProperties;
import de.llalon.cinematic.domain.ClientContext;
import de.llalon.cinematic.domain.Library;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;

/**
 * Contains tests which do not require access to real APIs. Utilizing mock web server.
 */
class CinematicTests {

    static MockWebServer server;

    static Library library;

    @BeforeAll
    static void setUp() {
        library = new Library(ClientContext.builder()
                .plexProperties(PlexProperties.builder()
                        .url("http://localhost:32400")
                        .token("test")
                        .build())
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
                .build());
    }

    @Test
    void canConfigureClients() {
        Assertions.assertNotNull(library.getContext().getRadarrClient());
        Assertions.assertNotNull(library.getContext().getQbittorrentClient());
        Assertions.assertNotNull(library.getContext().getTautulliClient());
        Assertions.assertNotNull(library.getContext().getSonarrClient());
        Assertions.assertNotNull(library.getContext().getOverseerrClient());
    }
}
