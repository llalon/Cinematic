package de.llalon.cinematic;

import static org.junit.jupiter.api.Assertions.*;

import de.llalon.cinematic.domain.ClientContext;
import de.llalon.cinematic.domain.Movie;
import de.llalon.cinematic.domain.Torrent;
import java.util.List;
import org.junit.jupiter.api.*;

class CinematicIntegrationsTests {

    @BeforeAll
    static void setUp() {
        ClientContext.builder().build().register();

        Assumptions.assumeFalse(ClientContext.getInstance().getRadarrClient() == null);
        Assumptions.assumeFalse(ClientContext.getInstance().getQbittorrentClient() == null);
    }

    @Test
    void canFetchTorrentsFromMovie() {
        List<Movie> movies = Movie.fetchAll();
        Movie movie1 = movies.get(0);
        var torrents = movie1.fetchTorrents();
        var torrent1 = torrents.get(0);
        Movie movie2 = torrent1.fetchMovie();
        assertEquals(movie1.getId(), movie2.getId());
    }

    @Test
    void canFetchAllMovies() {
        List<Movie> movies = Movie.fetchAll();
        Movie movie1 = movies.get(0);
        Movie movie2 = Movie.fetchOne(String.valueOf(movie1.getId()));
        assertEquals(movie1.getId(), movie2.getId());
    }

    @Test
    void canFindTorrentFilesWithViruses() {
        var virusFiles = Torrent.fetchAll().stream()
                .peek(a -> System.out.println("Checking torrent: " + a))
                .filter(x -> x.fetchFiles().stream()
                        .peek(b -> System.out.println("Checking file: " + b))
                        .anyMatch(y -> y.getName().endsWith(".exe")))
                .toList();

        assertNotNull(virusFiles);
    }
}
