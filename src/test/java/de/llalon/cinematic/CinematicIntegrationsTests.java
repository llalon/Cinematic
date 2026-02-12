package de.llalon.cinematic;

import static org.junit.jupiter.api.Assertions.*;

import de.llalon.cinematic.domain.ClientContext;
import de.llalon.cinematic.domain.Movie;
import de.llalon.cinematic.domain.Series;
import de.llalon.cinematic.domain.Torrent;
import java.util.List;
import org.junit.jupiter.api.*;

class CinematicIntegrationsTests {

    @BeforeAll
    static void setUp() {
        ClientContext.builder().build().register();

        Assumptions.assumeFalse(ClientContext.getInstance().getRadarrClient() == null);
        Assumptions.assumeFalse(ClientContext.getInstance().getQbittorrentClient() == null);
        Assumptions.assumeFalse(ClientContext.getInstance().getTautulliClient() == null);
        Assumptions.assumeFalse(ClientContext.getInstance().getSonarrClient() == null);
        Assumptions.assumeFalse(ClientContext.getInstance().getOverseerrClient() == null);
    }

    @Test
    void fetchTorrentsForMovie() {
        List<Movie> movies = Movie.fetchAll();
        Movie movie1 = movies.get(0);
        var torrents = movie1.fetchTorrents();
        var torrent1 = torrents.get(0);
        Movie movie2 = torrent1.fetchMovie();
        assertEquals(movie1.getId(), movie2.getId());
    }

    @Test
    void fetchAllMovies() {
        List<Movie> movies = Movie.fetchAll();
        Movie movie1 = movies.get(0);
        Movie movie2 = Movie.fetchOne(String.valueOf(movie1.getId()));
        assertEquals(movie1.getId(), movie2.getId());
    }

    @Test
    void findTorrentFilesWithToxicFiles() {
        var virusFiles = Torrent.fetchAll().stream()
                .peek(a -> System.out.println("Checking torrent: " + a))
                .filter(x -> x.fetchFiles().stream()
                        .peek(b -> System.out.println("Checking file: " + b))
                        .anyMatch(y -> y.getName().endsWith(".exe")))
                .toList();

        assertNotNull(virusFiles);
    }

    @Test
    void increaseTorrentPriorityForMoviesWithTag() {
        Movie.fetchAll().stream()
                .peek(movie -> System.out.println("Checking torrent: " + movie.getTitle()))
                .filter(movie -> movie.hasTag("hp"))
                .forEach(movie -> movie.fetchTorrents().stream()
                        .peek(torrent -> System.out.println(
                                "Bumping " + movie.getTitle() + " torrent priority: " + torrent.getName()))
                        .forEach(Torrent::setTopPriority));
    }

    @Test
    void increaseTorrentPriorityForSeriesWithTag() {
        Series.fetchAll().stream()
                .peek(series -> System.out.println("Checking torrent: " + series.getTitle()))
                .filter(series -> series.hasTag("hp"))
                .forEach(series -> series.fetchTorrents().stream()
                        .peek(torrent -> System.out.println(
                                "Bumping " + series.getTitle() + " torrent priority: " + torrent.getName()))
                        .forEach(Torrent::setTopPriority));
    }
}
