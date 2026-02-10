package de.llalon.cinematic;

import static org.junit.jupiter.api.Assertions.assertEquals;

import de.llalon.cinematic.domain.ClientContext;
import de.llalon.cinematic.domain.Movie;
import java.util.List;
import org.junit.jupiter.api.*;

class CinematicIntegrationsTests {

    @BeforeAll
    static void setUp() {
        ClientContext.builder().build().register();

        Assumptions.assumeFalse(ClientContext.getInstance().getRadarrClient() == null);
        Assumptions.assumeFalse(ClientContext.getInstance().getRadarrClient() == null);
    }

    @Test
    void canFetchTorrentsFromMovie() {
        List<Movie> movies = Movie.fetchAll();
        Movie movie1 = movies.get(0);
        var torrents = movie1.getTorrents();
        var torrent1 = torrents.get(0);
        Movie movie2 = torrent1.getMovie();
        assertEquals(movie1.getId(), movie2.getId());
    }

    @Test
    void canFetchAllMovies() {
        List<Movie> movies = Movie.fetchAll();
        Movie movie1 = movies.get(0);
        Movie movie2 = Movie.fetchOne(String.valueOf(movie1.getId()));
        assertEquals(movie1.getId(), movie2.getId());
    }
}
