package de.llalon.cinematic;

import static org.junit.jupiter.api.Assertions.*;

import de.llalon.cinematic.domain.*;
import org.junit.jupiter.api.*;

/**
 * Tests which require api clients to be configured via environment variables. If not configured they will be skipped.
 */
class CinematicIntegrationsTests {

    static int TEST_SERIES_ID = 367;
    static int TEST_MOVIE_ID = 763215;

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

    private Series getTestSeries() {
        for (var series : library.series()) {
            if (series.getId().equals(TEST_SERIES_ID) || series.getTmdbId().equals(TEST_SERIES_ID)) {
                return series;
            }
        }
        throw new RuntimeException("No series with id " + TEST_SERIES_ID);
    }

    private Movie getTestMovie() {
        for (var movie : library.movies()) {
            if (movie.getId().equals(TEST_MOVIE_ID) || movie.getTmdbId().equals(TEST_MOVIE_ID)) {
                return movie;
            }
        }
        throw new RuntimeException("No movie with id " + TEST_MOVIE_ID);
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
        var movie = getTestMovie();
        var torrent = movie.torrents();
        assertNotNull(torrent.iterator().next());
    }

    @Test
    void canGetSeriesTorrents() {
        var series = getTestSeries();
        var torrent = series.torrents();
        assertNotNull(torrent.iterator().next());
    }

    @Test
    void canGetMovieRequests() {
        var movie = getTestMovie();
        var requests = movie.requests();
        assertNotNull(requests.iterator().next());
    }

    @Test
    void canGetSeriesRequests() {
        var series = getTestSeries();
        var requests = series.requests();
        assertNotNull(requests.iterator().next());
    }

    @Test
    void canGetMovieWatches() {
        var movie = getTestMovie();
        var watches = movie.watches();
        assertNotNull(watches.iterator().next());
    }

    @Test
    void canGetSeriesWatches() {
        var series = getTestSeries();
        var watches = series.watches();
        assertNotNull(watches.iterator().next());
    }
}
