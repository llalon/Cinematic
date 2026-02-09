package de.llalon.cinematic;

import static org.junit.jupiter.api.Assertions.*;

import de.llalon.cinematic.domain.ClientContextHolder;
import de.llalon.cinematic.domain.Movie;
import java.util.List;
import java.util.Properties;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;

class CinematicTests {

    static MockWebServer server;

    @BeforeAll
    static void setUp() {
        Properties properties = new Properties();

        properties.setProperty("qbittorrent.url", "http://localhost:8080");
        properties.setProperty("qbittorrent.username", "test");
        properties.setProperty("qbittorrent.password", "test");

        properties.setProperty("radarr.url", "http://localhost:7878");
        properties.setProperty("radarr.api_key", "test");

        properties.setProperty("sonarr.url", "http://localhost:8989");
        properties.setProperty("sonarr.api_key", "test");

        ClientContextHolder.configure(properties);

        Assumptions.assumeFalse(ClientContextHolder.getQbittorrentClient() == null);
        Assumptions.assumeFalse(ClientContextHolder.getSonarrClient() == null);
        Assumptions.assumeFalse(ClientContextHolder.getRadarrClient() == null);
    }

    @Test
    void canFetchAllMovies() {
        List<Movie> movies = Movie.fetchAll();
        Movie movie = Movie.fetchOne(String.valueOf(movies.get(0).getId()));
        assertEquals(movies.get(0).getId(), movie.getId());
    }
}
