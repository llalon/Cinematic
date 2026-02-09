package de.llalon.cinematic;

import static org.junit.jupiter.api.Assertions.*;

import de.llalon.cinematic.domain.ClientContextHolder;
import de.llalon.cinematic.domain.Movie;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CinematicTests {

    @BeforeEach
    void setUp() {
        ClientContextHolder.configure();
    }

    @AfterEach
    void tearDown() {}

    @Test
    void canFetchAllMovies() {
        List<Movie> movies = Movie.fetchAll();
        Movie movie = Movie.fetchOne(String.valueOf(movies.get(0).getId()));
        assertEquals(movies.get(0).getId(), movie.getId());
    }
}
