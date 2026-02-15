package de.llalon.cinematic;

import static org.junit.jupiter.api.Assertions.*;

import de.llalon.cinematic.domain.*;
import org.junit.jupiter.api.*;

/**
 * Tests which require api clients to be configured via environment variables. If not configured they will be skipped.
 */
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
        assertNotNull(results.iterator().next());
    }

    @Test
    void canGetSeries() {
        var results = library.series();
        assertNotNull(results.iterator().next());
    }

    @Test
    void canCanTorrents() {
        var results = library.torrents();
        assertNotNull(results.iterator().next());
    }

    @Test
    void canCanRequests() {
        var results = library.requests();
        assertNotNull(results.iterator().next());
    }

    @Test
    void canGetTags() {
        var results = library.tags();
        assertNotNull(results.iterator().next());
    }

    @Test
    void canGetMovieTorrents() {
        Torrent foundTorrent = null;
        for (var movie : library.movies()) {
            if (!movie.getTitle().toUpperCase().contains("DYNAMITE")) {
                continue;
            }

            var torrent = movie.torrents();
            foundTorrent = torrent.iterator().next();
        }
        assertNotNull(foundTorrent);
    }

    @Test
    void canGetSeriesTorrents() {
        Torrent foundTorrent = null;
        for (var series : library.series()) {
            if (!series.getTitle().toUpperCase().contains("THE PIT")) {
                continue;
            }

            var torrent = series.torrents();
            foundTorrent = torrent.iterator().next();
        }
        assertNotNull(foundTorrent);
    }
}
