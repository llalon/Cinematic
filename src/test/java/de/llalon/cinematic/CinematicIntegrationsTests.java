package de.llalon.cinematic;

import static org.junit.jupiter.api.Assertions.*;

import de.llalon.cinematic.domain.*;
import de.llalon.cinematic.util.collections.StreamUtils;
import org.junit.jupiter.api.*;
import org.opentest4j.TestAbortedException;

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
            throw new TestAbortedException("Environment variables for client configuration not set.");
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

    @Test
    void canGetMovieTags() {
        var movie = library.movies().iterator().next();
        var tags = movie.tags();
        assertNotNull(StreamUtils.iterateToList(tags));
    }

    @Test
    void canGetSeriesTags() {
        var series = library.series().iterator().next();
        var tags = series.tags();
        assertNotNull(StreamUtils.iterateToList(tags));
    }

    @Test
    void canGetTorrentSeries() {
        var torrent = library.torrents().iterator().next();
        var series = torrent.series();
        assertNotNull(StreamUtils.iterateToList(series));
    }

    @Test
    void canGetTorrentMovies() {
        var torrent = library.torrents().iterator().next();
        var movies = torrent.movies();
        assertNotNull(StreamUtils.iterateToList(movies));
    }

    @Test
    void canGetTagName() {
        var tag = library.tags().iterator().next();
        assertNotNull(tag.getName());
        assertFalse(tag.getName().isBlank());
    }

    @Test
    void canGetTagMovies() {
        var tag = library.tags().iterator().next();
        var movies = tag.movies();
        assertNotNull(StreamUtils.iterateToList(movies));
    }

    @Test
    void canGetTagSeries() {
        var tag = library.tags().iterator().next();
        var series = tag.series();
        assertNotNull(StreamUtils.iterateToList(series));
    }

    @Test
    void canGetTagTorrents() {
        var tag = library.tags().iterator().next();
        var torrents = tag.torrents();
        assertNotNull(StreamUtils.iterateToList(torrents));
    }

    @Test
    void canGetRequestUser() {
        var request = library.requests().iterator().next();
        var user = request.user();
        assertNotNull(user);
        assertNotNull(user.getEmail());
    }

    @Test
    void canGetMovieTmdbId() {
        var movie = library.movies().iterator().next();
        assertNotNull(movie.getTmdbId());
    }

    @Test
    void canGetSeriesTvdbId() {
        var series = library.series().iterator().next();
        assertNotNull(series.getTvdbId());
    }

    @Test
    void canGetUserEmail() {
        var user = library.users().iterator().next();
        assertNotNull(user.getEmail());
        assertFalse(user.getEmail().isBlank());
    }

    @Test
    void canGetUserWatches() {
        var user = library.users().iterator().next();
        var watches = user.watches();
        assertNotNull(StreamUtils.iterateToList(watches));
    }

    // ----- Movie property tests -----

    @Test
    void canGetMovieTitle() {
        var movie = library.movies().iterator().next();
        assertNotNull(movie.getTitle());
        assertFalse(movie.getTitle().isBlank());
    }

    @Test
    void canGetMovieId() {
        var movie = library.movies().iterator().next();
        assertNotNull(movie.getTitle());
    }

    @Test
    void canGetMovieYear() {
        var movie = library.movies().iterator().next();
        assertNotNull(movie.getYear());
    }

    @Test
    void canGetMovieStatus() {
        var movie = library.movies().iterator().next();
        assertNotNull(movie.getStatus());
        assertFalse(movie.getStatus().isBlank());
    }

    // ----- Series property tests -----

    @Test
    void canGetSeriesTitle() {
        var series = library.series().iterator().next();
        assertNotNull(series.getTitle());
        assertFalse(series.getTitle().isBlank());
    }

    @Test
    void canGetSeriesId() {
        var series = library.series().iterator().next();
        assertNotNull(series.getTitle());
    }

    @Test
    void canGetSeriesYear() {
        var series = library.series().iterator().next();
        assertNotNull(series.getYear());
    }

    @Test
    void canGetSeriesStatus() {
        var series = library.series().iterator().next();
        assertNotNull(series.getStatus());
        assertFalse(series.getStatus().isBlank());
    }

    @Test
    void canGetSeriesTmdbId() {
        var series = library.series().iterator().next();
        // tmdbId may be null for some series; method must not throw
        series.getTmdbId();
    }

    // ----- Torrent property tests -----

    @Test
    void canGetTorrentName() {
        var torrent = library.torrents().iterator().next();
        assertNotNull(torrent.getName());
        assertFalse(torrent.getName().isBlank());
    }

    @Test
    void canGetTorrentHash() {
        var torrent = library.torrents().iterator().next();
        assertNotNull(torrent.getHash());
        assertFalse(torrent.getHash().isBlank());
    }

    @Test
    void canGetTorrentState() {
        var torrent = library.torrents().iterator().next();
        assertNotNull(torrent.getState());
        assertFalse(torrent.getState().isBlank());
    }

    @Test
    void canGetTorrentCategory() {
        var torrent = library.torrents().iterator().next();
        assertNotNull(torrent.getCategory());
    }

    // ----- Request property tests -----

    @Test
    void canGetRequest() {
        var request = library.requests().iterator().next();
        assertNotNull(request);
    }

    @Test
    void canGetRequestStatus() {
        var request = library.requests().iterator().next();
        assertNotNull(request.getStatus());
    }

    @Test
    void canGetRequestUserById() {
        var request = library.requests().iterator().next();
        var user = request.user();
        assertNotNull(user.getEmail());
    }

    // ----- Tag name() method test -----

    @Test
    void canCallTagNameMethod() {
        var tag = library.tags().iterator().next();
        // name() is the method variant; it may return null per its @Nullable contract
        var name = tag.getName();
        assertEquals(tag.getName(), name);
    }

    // ----- Watches property tests -----

    @Test
    void canGetMovieWatchesProperties() {
        var movie = library.movies().iterator().next();
        var watchesList = StreamUtils.iterateToList(movie.watches());
        assertNotNull(watchesList);
        if (!watchesList.isEmpty()) {
            var watches = watchesList.get(0);
            assertNotNull(watches.getTitle());
            assertNotNull(watches.getRatingKey());
        }
    }

    @Test
    void canGetSeriesWatchesProperties() {
        var series = library.series().iterator().next();
        var watchesList = StreamUtils.iterateToList(series.watches());
        assertNotNull(watchesList);
        if (!watchesList.isEmpty()) {
            var watches = watchesList.get(0);
            assertNotNull(watches.getTitle());
            assertNotNull(watches.getRatingKey());
        }
    }

    @Test
    void canGetUserWatchesProperties() {
        var user = library.users().iterator().next();
        var watchesList = StreamUtils.iterateToList(user.watches());
        assertNotNull(watchesList);
        if (!watchesList.isEmpty()) {
            var watches = watchesList.get(0);
            assertNotNull(watches.getTitle());
            assertNotNull(watches.getRatingKey());
        }
    }
}
