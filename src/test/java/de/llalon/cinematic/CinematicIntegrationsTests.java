package de.llalon.cinematic;

import static org.junit.jupiter.api.Assertions.*;

import de.llalon.cinematic.domain.*;
import de.llalon.cinematic.util.collections.StreamUtils;
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
        Assumptions.assumeFalse(library.getContext().getPlexClient() == null);
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
        var movie = library.movies().iterator().next();
        var torrent = movie.torrents();
        assertNotNull(StreamUtils.iterateToList(torrent));
    }

    @Test
    void canGetSeriesTorrents() {
        var series = library.series().iterator().next();
        var torrent = series.torrents();
        assertNotNull(StreamUtils.iterateToList(torrent));
    }

    @Test
    void canGetMovieRequests() {
        var movie = library.movies().iterator().next();
        var requests = movie.requests();
        assertNotNull(StreamUtils.iterateToList(requests));
    }

    @Test
    void canGetSeriesRequests() {
        var series = library.series().iterator().next();
        var requests = series.requests();
        assertNotNull(StreamUtils.iterateToList(requests));
    }

    @Test
    void canGetMovieWatches() {
        var movie = library.movies().iterator().next();
        var watches = movie.watches();
        assertNotNull(StreamUtils.iterateToList(watches));
    }

    @Test
    void canGetSeriesWatches() {
        var series = library.series().iterator().next();
        var watches = series.watches();
        assertNotNull(StreamUtils.iterateToList(watches));
    }
}
