package de.llalon.cinematic;

import de.llalon.cinematic.domain.ClientContextHolder;
import de.llalon.cinematic.domain.Movie;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
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
    void test() {
        var movies = Movie.fetchAll();

        Assertions.assertNotNull(movies);
    }
}
