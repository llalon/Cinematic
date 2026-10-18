package de.llalon.cinematic;

import static org.junit.jupiter.api.Assertions.*;

import de.llalon.cinematic.domain.*;
import de.llalon.cinematic.util.collections.StreamUtils;
import java.util.Objects;
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
            library.getContext().getSeerrClient();
            library.getContext().getLidarrClient();
        } catch (Exception e) {
            throw new TestAbortedException("Environment variables for client configuration not set.");
        }
    }

    @Test
    void canGetMusicTrackWatches() {
        for (var tracks : library.tracks()) {
            for (var watches : tracks.watches()) {
                assertNotNull(watches.getRatingKey());
                return;
            }
        }
    }

    @Test
    void canGetMusicTrackTorrents() {
        for (var album : library.albums()) {
            for (var torrent : album.torrents()) {
                assertNotNull(torrent.getHash());
                return;
            }
        }
    }

    @Test
    void canGetArtistAlbumTrackBackToArtist() {
        for (var artist : library.artists()) {
            for (var album : artist.albums()) {
                for (var track : album.tracks()) {
                    assertEquals(artist.getId(), track.album().artist().getId());
                    return;
                }
            }
        }
    }

    @Test
    void canGetMovieVideoFilesFormats() {
        for (var movie : library.movies()) {
            for (var file : movie.files()) {
                assertNotNull(Objects.requireNonNull(file.getFormat()).getVideoFormat());
                return;
            }
        }
    }

    @Test
    void canGetSeriesVideoEpisodeFileFormats() {
        for (var series : library.series()) {
            for (var episode : series.episodes()) {
                for (var file : episode.files()) {
                    assertNotNull(Objects.requireNonNull(file.getFormat()).getVideoFormat());
                    return;
                }
            }
        }
    }

    @Test
    void canGetMovieVideoFormats() {
        for (var movie : library.movies()) {
            for (var format : movie.formats()) {
                assertNotNull(format.getVideoFormat());
                return;
            }
        }
    }

    @Test
    void canGetSeriesVideoFormats() {
        for (var series : library.series()) {
            for (var episode : series.episodes()) {
                for (var format : episode.formats()) {
                    assertNotNull(format.getVideoFormat());
                    return;
                }
            }
        }
    }

    @Test
    void canGetUsers() {
        for (var result : library.users()) {
            assertNotNull(result);
            return;
        }
    }

    @Test
    void canGetUsersRequests() {
        for (var user : library.users()) {
            for (var request : user.requests()) {
                assertNotNull(request.getStatus());
                return;
            }
        }
    }

    @Test
    void canGetMovies() {
        for (var result : library.movies()) {
            assertNotNull(result);
            return;
        }
    }

    @Test
    void canGetSeries() {
        for (var result : library.series()) {
            assertNotNull(result);
            return;
        }
    }

    @Test
    void canCanTorrents() {
        for (var result : library.torrents()) {
            assertNotNull(result);
            return;
        }
    }

    @Test
    void canCanRequests() {
        for (var result : library.requests()) {
            assertNotNull(result);
            return;
        }
    }

    @Test
    void canGetTags() {
        for (var result : library.tags()) {
            assertNotNull(result);
            return;
        }
    }

    @Test
    void canGetMovieTorrents() {
        for (var movie : library.movies()) {
            for (var torrent : movie.torrents()) {
                assertNotNull(torrent.getHash());
                return;
            }
        }
    }

    @Test
    void canGetSeriesTorrents() {
        for (var series : library.series()) {
            for (var torrent : series.torrents()) {
                assertNotNull(torrent.getHash());
                return;
            }
        }
    }

    @Test
    void canGetEpisodeTorrents() {
        for (var series : library.series()) {
            for (var episode : series.episodes()) {
                for (var torrent : episode.torrents()) {
                    assertNotNull(torrent.getHash());
                    return;
                }
            }
        }
    }

    @Test
    void canGetMovieRequests() {
        for (var movie : library.movies()) {
            for (var request : movie.requests()) {
                assertNotNull(request.getStatus());
                return;
            }
        }
    }

    @Test
    void canGetSeriesRequests() {
        for (var series : library.series()) {
            for (var request : series.requests()) {
                assertNotNull(request.getStatus());
                return;
            }
        }
    }

    @Test
    void canGetMovieWatches() {
        for (var movie : library.movies()) {
            for (var watches : movie.watches()) {
                assertNotNull(watches.getRatingKey());
                return;
            }
        }
    }

    @Test
    void canGetSeriesWatches() {
        for (var series : library.series()) {
            for (var watches : series.watches()) {
                assertNotNull(watches.getRatingKey());
                return;
            }
        }
    }

    @Test
    void canGetMovieTags() {
        for (var movie : library.movies()) {
            for (var tag : movie.tags()) {
                assertNotNull(tag.getName());
                return;
            }
        }
    }

    @Test
    void canGetSeriesTags() {
        for (var series : library.series()) {
            for (var tag : series.tags()) {
                assertNotNull(tag.getName());
                return;
            }
        }
    }

    @Test
    void canGetTorrentSeries() {
        for (var torrent : library.torrents()) {
            for (var series : torrent.series()) {
                assertNotNull(series.getTvdbId());
                return;
            }
        }
    }

    @Test
    void canGetTorrentMovies() {
        for (var torrent : library.torrents()) {
            for (var movie : torrent.movies()) {
                assertNotNull(movie.getTmdbId());
                return;
            }
        }
    }

    @Test
    void canGetTagName() {
        for (var tag : library.tags()) {
            assertNotNull(tag.getName());
            assertFalse(tag.getName().isBlank());
            return;
        }
    }

    @Test
    void canGetTagMovies() {
        for (var tag : library.tags()) {
            for (var movie : tag.movies()) {
                assertNotNull(movie.getTmdbId());
                return;
            }
        }
    }

    @Test
    void canGetTagSeries() {
        for (var tag : library.tags()) {
            for (var series : tag.series()) {
                assertNotNull(series.getTvdbId());
                return;
            }
        }
    }

    @Test
    void canGetTagTorrents() {
        for (var tag : library.tags()) {
            for (var torrent : tag.torrents()) {
                assertNotNull(torrent.getHash());
                return;
            }
        }
    }

    @Test
    void canGetRequestUser() {
        for (var request : library.requests()) {
            var user = request.user();
            assertNotNull(user);
            assertNotNull(user.getEmail());
            return;
        }
    }

    @Test
    void canGetMovieTmdbId() {
        for (var movie : library.movies()) {
            assertNotNull(movie.getTmdbId());
            return;
        }
    }

    @Test
    void canGetSeriesTvdbId() {
        for (var series : library.series()) {
            assertNotNull(series.getTvdbId());
            return;
        }
    }

    @Test
    void canGetUserEmail() {
        for (var user : library.users()) {
            assertNotNull(user.getEmail());
            assertFalse(user.getEmail().isBlank());
            return;
        }
    }

    @Test
    void canGetUserWatches() {
        for (var user : library.users()) {
            for (var watches : user.watches()) {
                assertNotNull(watches.getRatingKey());
                return;
            }
        }
    }

    // ----- Movie property tests -----

    @Test
    void canGetMovieTitle() {
        for (var movie : library.movies()) {
            assertNotNull(movie.getTitle());
            assertFalse(movie.getTitle().isBlank());
            return;
        }
    }

    @Test
    void canGetMovieYear() {
        for (var movie : library.movies()) {
            assertNotNull(movie.getYear());
            return;
        }
    }

    @Test
    void canGetMovieStatus() {
        for (var movie : library.movies()) {
            assertNotNull(movie.getStatus());
            assertFalse(movie.getStatus().isBlank());
            return;
        }
    }

    @Test
    void canGetMovieHasFile() {
        for (var movie : library.movies()) {
            assertNotNull(movie.getHasFile());
            return;
        }
    }

    @Test
    void canGetMovieImdbId() {
        for (var movie : library.movies()) {
            // imdbId may be null; method must not throw
            movie.getImdbId();
            return;
        }
    }

    @Test
    void canGetMovieTvdbId() {
        for (var movie : library.movies()) {
            // tvdbId is always null for movies by design
            assertNull(movie.getTvdbId());
            return;
        }
    }

    @Test
    void canMovieHasTagByString() {
        for (var movie : library.movies()) {
            for (var tag : movie.tags()) {
                assertTrue(movie.hasTag(tag.getName()));
                return;
            }
        }
    }

    @Test
    void canMovieHasTagByTag() {
        for (var movie : library.movies()) {
            for (var tag : movie.tags()) {
                assertTrue(movie.hasTag(tag));
                return;
            }
        }
    }

    // ----- Series property tests -----

    @Test
    void canGetSeriesTitle() {
        for (var series : library.series()) {
            assertNotNull(series.getTitle());
            assertFalse(series.getTitle().isBlank());
            return;
        }
    }

    @Test
    void canGetSeriesYear() {
        for (var series : library.series()) {
            assertNotNull(series.getYear());
            return;
        }
    }

    @Test
    void canGetSeriesStatus() {
        for (var series : library.series()) {
            assertNotNull(series.getStatus());
            assertFalse(series.getStatus().isBlank());
            return;
        }
    }

    @Test
    void canGetSeriesTmdbId() {
        for (var series : library.series()) {
            // tmdbId may be null for some series; method must not throw
            series.getTmdbId();
            return;
        }
    }

    @Test
    void canGetSeriesImdbId() {
        for (var series : library.series()) {
            // imdbId may be null; method must not throw
            series.getImdbId();
            return;
        }
    }

    @Test
    void canSeriesHasTagByString() {
        for (var series : library.series()) {
            for (var tag : series.tags()) {
                assertTrue(series.hasTag(tag.getName()));
                return;
            }
        }
    }

    @Test
    void canSeriesHasTagByTag() {
        for (var series : library.series()) {
            for (var tag : series.tags()) {
                assertTrue(series.hasTag(tag));
                return;
            }
        }
    }

    // ----- Torrent property tests -----

    @Test
    void canGetTorrentName() {
        for (var torrent : library.torrents()) {
            assertNotNull(torrent.getName());
            assertFalse(torrent.getName().isBlank());
            return;
        }
    }

    @Test
    void canGetTorrentHash() {
        for (var torrent : library.torrents()) {
            assertNotNull(torrent.getHash());
            assertFalse(torrent.getHash().isBlank());
            return;
        }
    }

    @Test
    void canGetTorrentState() {
        for (var torrent : library.torrents()) {
            assertNotNull(torrent.getState());
            assertFalse(torrent.getState().isBlank());
            return;
        }
    }

    @Test
    void canGetTorrentCategory() {
        for (var torrent : library.torrents()) {
            assertNotNull(torrent.getCategory());
            return;
        }
    }

    @Test
    void canGetTorrentTracker() {
        for (var torrent : library.torrents()) {
            assertNotNull(torrent.getTracker());
            return;
        }
    }

    // ----- Request property tests -----

    @Test
    void canGetRequestStatus() {
        for (var request : library.requests()) {
            assertNotNull(request.getStatus());
            return;
        }
    }

    @Test
    void canGetRequestUserById() {
        for (var request : library.requests()) {
            var user = request.user();
            assertNotNull(user.getEmail());
            return;
        }
    }

    @Test
    void canGetRequestTvdbId() {
        for (var request : library.requests()) {
            // tvdbId may be null depending on media type; method must not throw
            request.getTvdbId();
            return;
        }
    }

    // ----- Watches property tests -----

    @Test
    void canGetMovieWatchesProperties() {
        for (var movie : library.movies()) {
            for (var watches : movie.watches()) {
                assertNotNull(watches.getTitle());
                assertNotNull(watches.getRatingKey());
                return;
            }
        }
    }

    @Test
    void canGetSeriesWatchesProperties() {
        for (var series : library.series()) {
            for (var watches : series.watches()) {
                assertNotNull(watches.getTitle());
                assertNotNull(watches.getRatingKey());
                return;
            }
        }
    }

    @Test
    void canGetUserWatchesProperties() {
        for (var user : library.users()) {
            for (var watches : user.watches()) {
                assertNotNull(watches.getTitle());
                assertNotNull(watches.getRatingKey());
                return;
            }
        }
    }

    @Test
    void canGetWatchesUser() {
        for (var user : library.users()) {
            for (var watches : user.watches()) {
                var email = user.getEmail();
                var watchesEmail = watches.user().getEmail();
                assertNotNull(email);
                assertNotNull(watchesEmail);
                assertTrue(email.equalsIgnoreCase(watchesEmail));
                return;
            }
        }
    }

    @Test
    void canGetArtists() {
        for (var artist : library.artists()) {
            assertNotNull(artist);
            return;
        }
    }

    @Test
    void canGetAlbums() {
        for (var album : library.albums()) {
            assertNotNull(album);
            return;
        }
    }

    @Test
    void canGetTracks() {
        for (var track : library.tracks()) {
            assertNotNull(track);
            return;
        }
    }

    @Test
    void canGetEpisodes() {
        for (var episode : library.episodes()) {
            assertNotNull(episode);
            return;
        }
    }

    @Test
    void canGetLibraryContext() {
        assertNotNull(library.getContext());
    }

    @Test
    void canGetArtistMetadata() {
        for (var artist : library.artists()) {
            assertNotNull(artist.getId());
            assertNotNull(artist.getArtistName());
            assertDoesNotThrow(artist::getForeignArtistId);
            assertDoesNotThrow(artist::getStatus);
            assertDoesNotThrow(artist::getPath);
            assertDoesNotThrow(artist::getMonitored);
            assertNotNull(artist.getName());
            return;
        }
    }

    @Test
    void canGetAlbumMetadata() {
        for (var album : library.albums()) {
            assertNotNull(album.getId());
            assertNotNull(album.getTitle());
            assertDoesNotThrow(album::getForeignAlbumId);
            assertDoesNotThrow(album::getMonitored);
            assertDoesNotThrow(album::getAlbumType);
            assertDoesNotThrow(album::getReleaseDate);
            return;
        }
    }

    @Test
    void canGetTrackMetadata() {
        for (var track : library.tracks()) {
            assertNotNull(track.getId());
            assertNotNull(track.getTitle());
            assertDoesNotThrow(track::getTrackNumber);
            assertDoesNotThrow(track::getMediumNumber);
            assertDoesNotThrow(track::getDuration);
            assertDoesNotThrow(track::getHasFile);
            assertDoesNotThrow(track::getForeignTrackId);
            assertDoesNotThrow(track::getForeignRecordingId);
            return;
        }
    }

    @Test
    void canGetEpisodeMetadata() {
        for (var episode : library.episodes()) {
            assertNotNull(episode.getTitle());
            assertDoesNotThrow(episode::getSeasonNumber);
            assertDoesNotThrow(episode::getEpisodeNumber);
            assertDoesNotThrow(episode::getHasFile);
            assertDoesNotThrow(episode::getMonitored);
            assertDoesNotThrow(episode::getAirDate);
            assertDoesNotThrow(episode::getAirDateUtc);
            return;
        }
    }

    @Test
    void canGetTorrentMetadata() {
        for (var torrent : library.torrents()) {
            assertDoesNotThrow(torrent::getContentPath);
            assertDoesNotThrow(torrent::getAmountLeft);
            assertDoesNotThrow(torrent::getProgress);
            assertDoesNotThrow(torrent::getCompletionOn);
            return;
        }
    }

    @Test
    void canGetMovieMetadata() {
        for (var movie : library.movies()) {
            assertDoesNotThrow(movie::getTitleSlug);
            return;
        }
    }

    @Test
    void canGetArtistExternalVideoIds() {
        for (var artist : library.artists()) {
            assertNull(artist.getTmdbId());
            assertNull(artist.getTvdbId());
            assertNull(artist.getImdbId());
            return;
        }
    }

    @Test
    void canGetArtistRequests() {
        for (var artist : library.artists()) {
            assertTrue(StreamUtils.iterateToList(artist.requests()).isEmpty());
            return;
        }
    }

    @Test
    void canGetAlbumExternalVideoIds() {
        for (var album : library.albums()) {
            assertNull(album.getTmdbId());
            assertNull(album.getTvdbId());
            assertNull(album.getImdbId());
            return;
        }
    }

    @Test
    void canGetAlbumRequests() {
        for (var album : library.albums()) {
            assertTrue(StreamUtils.iterateToList(album.requests()).isEmpty());
            return;
        }
    }

    @Test
    void canGetTrackExternalVideoIds() {
        for (var track : library.tracks()) {
            assertNull(track.getTmdbId());
            assertNull(track.getTvdbId());
            assertNull(track.getImdbId());
            return;
        }
    }

    @Test
    void canGetTrackRequests() {
        for (var track : library.tracks()) {
            assertTrue(StreamUtils.iterateToList(track.requests()).isEmpty());
            return;
        }
    }

    @Test
    void canGetArtistTracks() {
        for (var artist : library.artists()) {
            for (var track : artist.tracks()) {
                assertNotNull(track.getId());
                return;
            }
        }
    }

    @Test
    void canGetArtistFiles() {
        for (var artist : library.artists()) {
            for (var file : artist.files()) {
                assertNotNull(file.getId());
                return;
            }
        }
    }

    @Test
    void canGetArtistTags() {
        for (var artist : library.artists()) {
            for (var tag : artist.tags()) {
                assertNotNull(tag.getName());
                return;
            }
        }
    }

    @Test
    void canGetArtistTorrents() {
        for (var artist : library.artists()) {
            for (var torrent : artist.torrents()) {
                assertNotNull(torrent.getHash());
                return;
            }
        }
    }

    @Test
    void canGetArtistWatches() {
        for (var artist : library.artists()) {
            for (var watches : artist.watches()) {
                assertNotNull(watches.getRatingKey());
                return;
            }
        }
    }

    @Test
    void canGetArtistFormats() {
        for (var artist : library.artists()) {
            for (var format : artist.formats()) {
                assertNotNull(format.getAudioFormat());
                return;
            }
        }
    }

    @Test
    void canGetAlbumFiles() {
        for (var album : library.albums()) {
            for (var file : album.files()) {
                assertNotNull(file.getId());
                return;
            }
        }
    }

    @Test
    void canGetAlbumTags() {
        for (var album : library.albums()) {
            for (var tag : album.tags()) {
                assertNotNull(tag.getName());
                return;
            }
        }
    }

    @Test
    void canGetAlbumWatches() {
        for (var album : library.albums()) {
            for (var watches : album.watches()) {
                assertNotNull(watches.getRatingKey());
                return;
            }
        }
    }

    @Test
    void canGetAlbumFormats() {
        for (var album : library.albums()) {
            for (var format : album.formats()) {
                assertNotNull(format.getAudioFormat());
                return;
            }
        }
    }

    @Test
    void canGetTrackFiles() {
        for (var track : library.tracks()) {
            for (var file : track.files()) {
                assertNotNull(file.getId());
                return;
            }
        }
    }

    @Test
    void canGetTrackTags() {
        for (var track : library.tracks()) {
            for (var tag : track.tags()) {
                assertNotNull(tag.getName());
                return;
            }
        }
    }

    @Test
    void canGetTrackTorrents() {
        for (var track : library.tracks()) {
            for (var torrent : track.torrents()) {
                assertNotNull(torrent.getHash());
                return;
            }
        }
    }

    @Test
    void canGetTrackFormats() {
        for (var track : library.tracks()) {
            for (var format : track.formats()) {
                assertNotNull(format.getAudioFormat());
                return;
            }
        }
    }

    @Test
    void canGetArtistAlbumsBackToArtist() {
        for (var artist : library.artists()) {
            for (var album : artist.albums()) {
                assertEquals(artist.getId(), album.artist().getId());
                return;
            }
        }
    }

    @Test
    void canGetArtistTracksBackToArtist() {
        for (var artist : library.artists()) {
            for (var track : artist.tracks()) {
                assertEquals(artist.getId(), track.artist().getId());
                return;
            }
        }
    }

    @Test
    void canGetAlbumTracksBackToAlbum() {
        for (var album : library.albums()) {
            for (var track : album.tracks()) {
                assertEquals(album.getId(), track.album().getId());
                return;
            }
        }
    }

    @Test
    void canGetAlbumArtist() {
        for (var album : library.albums()) {
            assertNotNull(album.artist().getId());
            return;
        }
    }

    @Test
    void canGetTrackArtist() {
        for (var track : library.tracks()) {
            assertEquals(track.album().artist().getId(), track.artist().getId());
            return;
        }
    }

    @Test
    void canGetTrackAlbum() {
        for (var track : library.tracks()) {
            assertNotNull(track.album().getId());
            return;
        }
    }

    @Test
    void canGetSeriesEpisodesBackToSeries() {
        for (var series : library.series()) {
            for (var episode : series.episodes()) {
                assertEquals(series.getTvdbId(), episode.series().getTvdbId());
                return;
            }
        }
    }

    @Test
    void canGetSeriesFiles() {
        for (var series : library.series()) {
            for (Object file : series.files()) {
                assertNotNull(file);
                return;
            }
        }
    }

    @Test
    void canArtistHasTagByString() {
        for (var artist : library.artists()) {
            for (var tag : artist.tags()) {
                assertTrue(artist.hasTag(tag.getName()));
                return;
            }
        }
    }

    @Test
    void canArtistHasTagByTag() {
        for (var artist : library.artists()) {
            for (var tag : artist.tags()) {
                assertTrue(artist.hasTag(tag));
                return;
            }
        }
    }

    @Test
    void canAlbumHasTagByString() {
        for (var album : library.albums()) {
            for (var tag : album.tags()) {
                assertTrue(album.hasTag(tag.getName()));
                return;
            }
        }
    }

    @Test
    void canAlbumHasTagByTag() {
        for (var album : library.albums()) {
            for (var tag : album.tags()) {
                assertTrue(album.hasTag(tag));
                return;
            }
        }
    }

    @Test
    void canTrackHasTagByString() {
        for (var track : library.tracks()) {
            for (var tag : track.tags()) {
                assertTrue(track.hasTag(tag.getName()));
                return;
            }
        }
    }

    @Test
    void canTrackHasTagByTag() {
        for (var track : library.tracks()) {
            for (var tag : track.tags()) {
                assertTrue(track.hasTag(tag));
                return;
            }
        }
    }

    @Test
    void canTorrentHasTagByString() {
        for (var torrent : library.torrents()) {
            for (var tag : torrent.tags()) {
                assertTrue(torrent.hasTag(tag.getName()));
                return;
            }
        }
    }

    @Test
    void canTorrentHasTagByTag() {
        for (var torrent : library.torrents()) {
            for (var tag : torrent.tags()) {
                assertTrue(torrent.hasTag(tag));
                return;
            }
        }
    }

    @Test
    void canGetTorrentArtists() {
        for (var torrent : library.torrents()) {
            for (var artist : torrent.artists()) {
                assertNotNull(artist.getId());
                return;
            }
        }
    }

    @Test
    void canGetTorrentAlbums() {
        for (var torrent : library.torrents()) {
            for (var album : torrent.albums()) {
                assertNotNull(album.getId());
                return;
            }
        }
    }

    @Test
    void canGetTorrentTags() {
        for (var torrent : library.torrents()) {
            for (var tag : torrent.tags()) {
                assertTrue(torrent.hasTag(tag));
                return;
            }
        }
    }

    @Test
    void canGetTagArtists() {
        for (var tag : library.tags()) {
            for (var artist : tag.artists()) {
                assertTrue(artist.hasTag(tag));
                return;
            }
        }
    }

    @Test
    void canGetTorrentCompletion() {
        for (var torrent : library.torrents()) {
            var completed = torrent.isCompleted();
            if (completed) {
                assertEquals(Long.valueOf(0), torrent.getAmountLeft());
                assertTrue(torrent.getCompletionOn() > 0);
            }
            return;
        }
    }

    @Test
    void canGetTorrentFiles() {
        for (var torrent : library.torrents()) {
            for (var file : torrent.files()) {
                assertNotNull(file.getName());
                assertDoesNotThrow(file::getSize);
                assertDoesNotThrow(file::getProgress);
                return;
            }
        }
    }

    @Test
    void canGetWatchesWatchedStatus() {
        for (var user : library.users()) {
            for (var watches : user.watches()) {
                assertDoesNotThrow(watches::getWatchedStatus);
                return;
            }
        }
    }

    @Test
    void canGetMovieFileMetadata() {
        for (var movie : library.movies()) {
            for (var file : movie.files()) {
                assertNotNull(file.getId());
                assertDoesNotThrow(file::getMovieId);
                assertDoesNotThrow(file::getSeriesId);
                assertDoesNotThrow(file::getSeasonNumber);
                assertDoesNotThrow(file::getEpisodeNumber);
                assertDoesNotThrow(file::getRelativePath);
                assertDoesNotThrow(file::getPath);
                assertDoesNotThrow(file::getSize);
                assertDoesNotThrow(file::getDateAdded);
                assertDoesNotThrow(file::getSceneName);
                assertDoesNotThrow(file::getReleaseGroup);
                assertDoesNotThrow(file::getQualityCutoffNotMet);
                assertDoesNotThrow(file::getQuality);
                assertDoesNotThrow(file::getQualityWeight);
                assertDoesNotThrow(file::getCustomFormatScore);
                assertDoesNotThrow(file::getOriginalFilePath);
                assertSame(file.format(), file.getFormat());
                return;
            }
        }
    }

    @Test
    void canGetMovieFormatMetadata() {
        for (var movie : library.movies()) {
            for (var format : movie.formats()) {
                assertDoesNotThrow(format::getVideoCodec);
                assertDoesNotThrow(format::getVideoFormat);
                assertDoesNotThrow(format::getAudioCodec);
                assertDoesNotThrow(format::getAudioFormat);
                assertDoesNotThrow(format::getResolution);
                assertDoesNotThrow(format::getAudioChannels);
                assertDoesNotThrow(format::getAudioLanguages);
                assertDoesNotThrow(format::getVideoDynamicRange);
                assertEquals(format.getVideoCodec(), format.getVideoFormat());
                assertEquals(format.getAudioCodec(), format.getAudioFormat());
                return;
            }
        }
    }

    @Test
    void canGetEpisodeFileMetadata() {
        for (var episode : library.episodes()) {
            for (var file : episode.files()) {
                assertNotNull(file.getId());
                assertDoesNotThrow(file::getMovieId);
                assertDoesNotThrow(file::getSeriesId);
                assertDoesNotThrow(file::getSeasonNumber);
                assertDoesNotThrow(file::getEpisodeNumber);
                assertDoesNotThrow(file::getRelativePath);
                assertDoesNotThrow(file::getPath);
                assertDoesNotThrow(file::getSize);
                assertDoesNotThrow(file::getDateAdded);
                assertDoesNotThrow(file::getSceneName);
                assertDoesNotThrow(file::getReleaseGroup);
                assertDoesNotThrow(file::getQualityCutoffNotMet);
                assertDoesNotThrow(file::getQuality);
                assertDoesNotThrow(file::getQualityWeight);
                assertDoesNotThrow(file::getCustomFormatScore);
                assertDoesNotThrow(file::getOriginalFilePath);
                assertSame(file.format(), file.getFormat());
                return;
            }
        }
    }

    @Test
    void canGetEpisodeFormatMetadata() {
        for (var episode : library.episodes()) {
            for (var format : episode.formats()) {
                assertDoesNotThrow(format::getVideoCodec);
                assertDoesNotThrow(format::getVideoFormat);
                assertDoesNotThrow(format::getAudioCodec);
                assertDoesNotThrow(format::getAudioFormat);
                assertDoesNotThrow(format::getResolution);
                assertDoesNotThrow(format::getAudioChannels);
                assertDoesNotThrow(format::getAudioLanguages);
                assertDoesNotThrow(format::getVideoDynamicRange);
                assertEquals(format.getVideoCodec(), format.getVideoFormat());
                assertEquals(format.getAudioCodec(), format.getAudioFormat());
                return;
            }
        }
    }

    @Test
    void canGetTrackFileMetadata() {
        for (var track : library.tracks()) {
            for (var file : track.files()) {
                assertNotNull(file.getId());
                assertDoesNotThrow(file::getMovieId);
                assertDoesNotThrow(file::getSeriesId);
                assertDoesNotThrow(file::getSeasonNumber);
                assertDoesNotThrow(file::getEpisodeNumber);
                assertDoesNotThrow(file::getRelativePath);
                assertDoesNotThrow(file::getPath);
                assertDoesNotThrow(file::getSize);
                assertDoesNotThrow(file::getDateAdded);
                assertDoesNotThrow(file::getSceneName);
                assertDoesNotThrow(file::getReleaseGroup);
                assertDoesNotThrow(file::getQualityCutoffNotMet);
                assertDoesNotThrow(file::getQuality);
                assertDoesNotThrow(file::getQualityWeight);
                assertDoesNotThrow(file::getCustomFormatScore);
                assertDoesNotThrow(file::getOriginalFilePath);
                assertSame(file.format(), file.getFormat());
                return;
            }
        }
    }

    @Test
    void canGetTrackFormatMetadata() {
        for (var track : library.tracks()) {
            for (var format : track.formats()) {
                assertDoesNotThrow(format::getVideoCodec);
                assertDoesNotThrow(format::getVideoFormat);
                assertDoesNotThrow(format::getAudioCodec);
                assertDoesNotThrow(format::getAudioFormat);
                assertDoesNotThrow(format::getResolution);
                assertDoesNotThrow(format::getAudioChannels);
                assertDoesNotThrow(format::getAudioLanguages);
                assertDoesNotThrow(format::getVideoDynamicRange);
                assertEquals(format.getVideoCodec(), format.getVideoFormat());
                assertEquals(format.getAudioCodec(), format.getAudioFormat());
                return;
            }
        }
    }

    @Test
    void canGetTrackFileArtist() {
        for (var artist : library.artists()) {
            for (var file : artist.files()) {
                assertEquals(artist.getId(), file.getArtistId());
                assertEquals(artist.getId(), file.artist().getId());
                return;
            }
        }
    }

    @Test
    void canGetTrackFileAlbum() {
        for (var album : library.albums()) {
            for (var file : album.files()) {
                assertEquals(album.getId(), file.getAlbumId());
                assertEquals(album.getId(), file.album().getId());
                return;
            }
        }
    }
}
