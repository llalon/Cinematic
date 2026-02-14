package de.llalon.cinematic;

import static org.junit.jupiter.api.Assertions.*;

import de.llalon.cinematic.domain.*;
import org.junit.jupiter.api.*;

class CinematicIntegrationsTests {

    static Library library;

    @BeforeAll
    static void setUp() {
        library = new Library(ClientContext.builder().build());

        Assumptions.assumeFalse(library.getContext() == null);
        Assumptions.assumeFalse(library.getContext().getRadarrClient() == null);
        Assumptions.assumeFalse(library.getContext().getQbittorrentClient() == null);
        Assumptions.assumeFalse(library.getContext().getTautulliClient() == null);
        Assumptions.assumeFalse(library.getContext().getSonarrClient() == null);
        Assumptions.assumeFalse(library.getContext().getOverseerrClient() == null);
    }

    @Test
    void canGetMovies() {
        var results = library.movies();
        assertNotNull(results);
    }

    @Test
    void canGetSeries() {
        var results = library.series();
        assertNotNull(results);
    }

    @Test
    void canCanTorrents() {
        var results = library.torrents();
        assertNotNull(results);
    }

    @Test
    void canCanRequests() {
        var results = library.requests();
        assertNotNull(results);
    }

    @Test
    void canGetTags() {
        var results = library.tags();
        assertNotNull(results);
    }
}
