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

        try {
            library.getContext().getPlexClient();
            library.getContext().getRadarrClient();
            library.getContext().getQbittorrentClient();
            library.getContext().getSonarrClient();
            library.getContext().getTautulliClient();
            library.getContext().getOverseerrClient();
        } catch (Exception e) {
            Assumptions.assumeTrue(e instanceof ClientNotConfiguredException);
        }
    }

    @Test
    void test() {}

    @Test
    void canGetUsers() {
        var results = library.users();
        assertNotNull(results.iterator().next());
    }

    @Test
    void canGetUsersRequests() {
        var users = library.users();
        var requests = users.iterator().next().requests();
        assertNotNull(requests.iterator().next());
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
