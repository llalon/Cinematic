package de.llalon.cinematic.domain;

import static org.junit.jupiter.api.Assertions.*;

import com.squareup.moshi.Moshi;
import de.llalon.cinematic.client.lidarr.LidarrClient;
import de.llalon.cinematic.client.lidarr.config.LidarrProperties;
import de.llalon.cinematic.client.lidarr.dto.AlbumResource;
import de.llalon.cinematic.client.lidarr.dto.ArtistResource;
import de.llalon.cinematic.client.lidarr.dto.TrackResource;
import de.llalon.cinematic.client.plex.config.PlexProperties;
import de.llalon.cinematic.client.plex.dto.PlexMediaItem;
import de.llalon.cinematic.client.plex.exception.PlexApiException;
import de.llalon.cinematic.client.radarr.dto.MovieResource;
import de.llalon.cinematic.client.sonarr.dto.SeriesResource;
import de.llalon.cinematic.client.tautulli.TautulliClient;
import de.llalon.cinematic.client.tautulli.config.TautulliProperties;
import de.llalon.cinematic.client.tautulli.dto.History;
import de.llalon.cinematic.client.tautulli.dto.TableResponse;
import de.llalon.cinematic.util.collections.StreamUtils;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;
import javax.cache.CacheManager;
import javax.cache.Caching;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PlexMusicTests {
    private MockWebServer server;
    private CacheManager cacheManager;
    private ClientContext ctx;
    private AlbumResource albumResource;
    private String lastHistoryRatingKey;
    private String lastHistoryFilter;

    @BeforeEach
    void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        cacheManager = Caching.getCachingProvider()
                .getCacheManager(
                        URI.create("plex-music-" + UUID.randomUUID()),
                        getClass().getClassLoader());
        albumResource = AlbumResource.builder().id(2).foreignAlbumId("group-id").build();
        LidarrClient lidarr =
                new LidarrClient(
                        new OkHttpClient(),
                        LidarrProperties.builder()
                                .url(server.url("/").toString())
                                .apiKey("lidarr-key")
                                .build(),
                        new Moshi.Builder().build()) {
                    @Override
                    public AlbumResource getAlbum(int id) {
                        assertEquals(2, id);
                        return albumResource;
                    }
                };
        TautulliClient tautulli =
                new TautulliClient(
                        new OkHttpClient(),
                        TautulliProperties.builder()
                                .url(server.url("/").toString())
                                .apiKey("test")
                                .build(),
                        new Moshi.Builder().build()) {
                    @Override
                    public TableResponse<History> getHistoryByRatingKey(String ratingKey, int start, int length) {
                        lastHistoryFilter = "rating_key";
                        return history(ratingKey, start);
                    }

                    @Override
                    public TableResponse<History> getHistoryByParentRatingKey(String ratingKey, int start, int length) {
                        lastHistoryFilter = "parent_rating_key";
                        return history(ratingKey, start);
                    }

                    @Override
                    public TableResponse<History> getHistoryByGrandparentRatingKey(
                            String ratingKey, int start, int length) {
                        lastHistoryFilter = "grandparent_rating_key";
                        return history(ratingKey, start);
                    }

                    private TableResponse<History> history(String ratingKey, int start) {
                        if (start > 0) {
                            return new TableResponse<>(1, 1, 1, List.of(), null, null, null, null, null);
                        }
                        lastHistoryRatingKey = ratingKey;
                        try {
                            History history = ctx.getMoshi()
                                    .adapter(History.class)
                                    .fromJson("{\"rating_key\":\"" + ratingKey + "\",\"title\":\"Played item\"}");
                            return new TableResponse<>(1, 1, 1, List.of(history), null, null, null, null, null);
                        } catch (java.io.IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
        ctx = ClientContext.builder()
                .cacheManager(cacheManager)
                .lidarrClient(lidarr)
                .tautulliClient(tautulli)
                .plexProperties(PlexProperties.builder()
                        .url(server.url("/").toString())
                        .token("plex-token")
                        .build())
                .build();
    }

    @AfterEach
    void tearDown() throws Exception {
        cacheManager.close();
        server.shutdown();
    }

    private void json(String body) {
        server.enqueue(
                new MockResponse().setHeader("Content-Type", "application/json").setBody(body));
    }

    private void musicSections() {
        json(
                "{\"MediaContainer\":{\"Directory\":[{\"key\":\"1\",\"type\":\"movie\"},{\"key\":\"2\",\"type\":\"artist\"}]}}");
    }

    private void request(String path) throws Exception {
        var request = server.takeRequest(2, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals("GET", request.getMethod());
        assertEquals(path, request.getPath());
        assertEquals("plex-token", request.getHeader("X-Plex-Token"));
        assertEquals("application/json", request.getHeader("Accept"));
    }

    @Test
    void artistResolvesExternalGuidFromFullMetadataAndCachesIt() throws Exception {
        musicSections();
        json(
                "{\"MediaContainer\":{\"Metadata\":[{\"ratingKey\":\"100\",\"type\":\"artist\",\"guid\":\"plex://artist/opaque-id\"}]}}");
        json(
                "{\"MediaContainer\":{\"Metadata\":[{\"ratingKey\":\"100\",\"type\":\"artist\",\"Guid\":[{\"id\":\"mbid://ARTIST-ID\"}]}]}}");
        Artist artist = new Artist(
                ctx, ArtistResource.builder().id(1).foreignArtistId("artist-id").build());
        assertEquals("100", artist.getPlexRatingKey());
        request("/library/sections");
        request("/library/sections/2/all?type=8&includeGuids=1");
        request("/library/metadata/100?includeGuids=1");
        assertEquals("100", artist.getPlexRatingKey());
        assertEquals(3, server.getRequestCount());
        new Library(ctx).invalidateCache();
        musicSections();
        json(
                "{\"MediaContainer\":{\"Metadata\":[{\"ratingKey\":\"101\",\"type\":\"artist\",\"guid\":\"mbid://artist-id\"}]}}");
        assertEquals("101", artist.getPlexRatingKey());
        request("/library/sections");
        request("/library/sections/2/all?type=8&includeGuids=1");
    }

    @Test
    void albumMatchesMusicBrainzEditionIdsAndLegacyDirectGuids() throws Exception {
        albumResource = ctx.getMoshi()
                .adapter(AlbumResource.class)
                .fromJson(
                        "{\"id\":2,\"foreignAlbumId\":\"group-id\",\"releases\":[{\"foreignReleaseId\":\"edition-id\"}]}");
        assertEquals("edition-id", albumResource.getReleases().get(0).getForeignReleaseId());
        musicSections();
        json(
                "{\"MediaContainer\":{\"Metadata\":[{\"ratingKey\":\"200\",\"type\":\"album\",\"guid\":\"mbid://edition-id\"}]}}");
        assertEquals("200", new Album(ctx, albumResource).getPlexRatingKey());
        request("/library/sections");
        request("/library/sections/2/all?type=9&includeGuids=1");
    }

    @Test
    void recordingMatchIsScopedToAlbumWhenRecordingAppearsOnMultipleAlbums() throws Exception {
        musicSections();
        json(
                "{\"MediaContainer\":{\"Metadata\":[{\"ratingKey\":\"200\",\"type\":\"album\",\"Guid\":[{\"id\":\"mbid://group-id\"}]}]}}");
        json(
                "{\"MediaContainer\":{\"Metadata\":["
                        + "{\"ratingKey\":\"300\",\"type\":\"track\",\"parentRatingKey\":\"999\",\"Guid\":[{\"id\":\"mbid://recording-id\"}]},"
                        + "{\"ratingKey\":\"301\",\"type\":\"track\",\"parentRatingKey\":\"200\",\"Guid\":[{\"id\":\"mbid://recording-id\"}]}]}}");
        Track track = new Track(
                ctx,
                TrackResource.builder()
                        .id(3)
                        .albumId(2)
                        .foreignTrackId("track-id")
                        .foreignRecordingId("recording-id")
                        .build());
        assertEquals("301", track.getPlexRatingKey());
        request("/library/sections");
        request("/library/sections/2/all?type=9&includeGuids=1");
        request("/library/sections/2/all?type=10&includeGuids=1");
        assertEquals("301", track.getPlexRatingKey());
        assertEquals(3, server.getRequestCount());
    }

    @Test
    void missingIdentifiersDoNotMakeRequestsAndAmbiguousMatchesReturnNull() throws Exception {
        assertNull(new Artist(ctx, ArtistResource.builder().id(1).build()).getPlexRatingKey());
        assertNull(
                new Album(ctx, AlbumResource.builder().id(2).foreignAlbumId(" ").build()).getPlexRatingKey());
        assertNull(new Track(ctx, TrackResource.builder().id(3).build()).getPlexRatingKey());
        assertEquals(0, server.getRequestCount());
        musicSections();
        json("{\"MediaContainer\":{\"Metadata\":["
                + "{\"ratingKey\":\"100\",\"type\":\"artist\",\"guid\":\"mbid://artist-id\"},"
                + "{\"ratingKey\":\"101\",\"type\":\"artist\",\"guid\":\"mbid://artist-id\"}]}}");
        assertNull(new Artist(
                        ctx,
                        ArtistResource.builder()
                                .id(1)
                                .foreignArtistId("artist-id")
                                .build())
                .getPlexRatingKey());
        request("/library/sections");
        request("/library/sections/2/all?type=8&includeGuids=1");
    }

    @Test
    void unrelatedIdentifiersAndTitlesAreNotUsedAsMatches() throws Exception {
        musicSections();
        json(
                "{\"MediaContainer\":{\"Metadata\":[{\"ratingKey\":\"100\",\"type\":\"artist\",\"title\":\"Same Name\",\"Guid\":[{\"id\":\"plex://artist/artist-id\"}]}]}}");
        assertNull(new Artist(
                        ctx,
                        ArtistResource.builder()
                                .id(1)
                                .artistName("Same Name")
                                .foreignArtistId("artist-id")
                                .build())
                .getPlexRatingKey());
        request("/library/sections");
        request("/library/sections/2/all?type=8&includeGuids=1");
    }

    @Test
    void trackDoesNotMatchRecordingFromAnUnresolvedAlbumAndApiErrorsPropagate() throws Exception {
        musicSections();
        json("{\"MediaContainer\":{\"Metadata\":[]}}");
        assertNull(new Track(
                        ctx,
                        TrackResource.builder()
                                .id(3)
                                .albumId(2)
                                .foreignRecordingId("recording-id")
                                .build())
                .getPlexRatingKey());
        request("/library/sections");
        request("/library/sections/2/all?type=9&includeGuids=1");
        new Library(ctx).invalidateCache();
        server.enqueue(new MockResponse().setResponseCode(401).setBody("unauthorized"));
        assertThrows(PlexApiException.class, () -> new Artist(
                        ctx,
                        ArtistResource.builder()
                                .id(1)
                                .foreignArtistId("artist-id")
                                .build())
                .getPlexRatingKey());
        request("/library/sections");
        assertNull(new PlexMediaItem("1", null, null, "movie", "Movie", 2020, null, List.of()).getParentRatingKey());
    }

    @Test
    void movieAndSeriesUseTheSameBasePlexResolverAndHistoryNavigation() throws Exception {
        json(
                "{\"MediaContainer\":{\"Directory\":[{\"key\":\"1\",\"type\":\"movie\"},{\"key\":\"3\",\"type\":\"show\"},{\"key\":\"2\",\"type\":\"artist\"}]}}");
        json(
                "{\"MediaContainer\":{\"Metadata\":[{\"ratingKey\":\"400\",\"type\":\"movie\",\"Guid\":[{\"id\":\"plex://movie/opaque\"},{\"id\":\"tmdb://42\"}]}]}}");
        LibraryMediaItem movie = new Movie(
                ctx,
                ctx.getMoshi().adapter(MovieResource.class).fromJson("{\"id\":1,\"title\":\"Movie\",\"tmdbId\":42}"));
        assertEquals("400", movie.getPlexRatingKey());
        request("/library/sections");
        request("/library/sections/1/all?type=1&includeGuids=1");
        assertEquals("400", StreamUtils.iterateToList(movie.watches()).get(0).getRatingKey());
        assertEquals("400", lastHistoryRatingKey);
        assertEquals("rating_key", lastHistoryFilter);
        json(
                "{\"MediaContainer\":{\"Metadata\":[{\"ratingKey\":\"500\",\"type\":\"show\",\"Guid\":[{\"id\":\"tvdb://84\"}]}]}}");
        LibraryMediaItem series = new Series(
                ctx,
                ctx.getMoshi().adapter(SeriesResource.class).fromJson("{\"id\":2,\"title\":\"Series\",\"tvdbId\":84}"));
        assertEquals("500", series.getPlexRatingKey());
        request("/library/sections/3/all?type=2&includeGuids=1");
        assertEquals("500", StreamUtils.iterateToList(series.watches()).get(0).getRatingKey());
        assertEquals("500", lastHistoryRatingKey);
        assertEquals("grandparent_rating_key", lastHistoryFilter);
    }

    @Test
    void musicSharesBaseRelationshipsAndDoesNotFetchSeerrRequests() throws Exception {
        LibraryMediaItem artist = new Artist(
                ctx, ArtistResource.builder().id(1).foreignArtistId("artist-id").build());
        LibraryMediaItem album = new Album(ctx, albumResource);
        LibraryMediaItem track = new Track(
                ctx,
                TrackResource.builder()
                        .id(3)
                        .albumId(2)
                        .foreignTrackId("track-id")
                        .build());
        for (LibraryMediaItem item : List.of(artist, album, track)) {
            assertTrue(StreamUtils.iterateToList(item.requests()).isEmpty());
        }
        assertEquals(0, server.getRequestCount());
        musicSections();
        json(
                "{\"MediaContainer\":{\"Metadata\":[{\"ratingKey\":\"100\",\"type\":\"artist\",\"guid\":\"mbid://artist-id\"}]}}");
        assertEquals("100", StreamUtils.iterateToList(artist.watches()).get(0).getRatingKey());
        assertEquals("100", lastHistoryRatingKey);
        assertEquals("grandparent_rating_key", lastHistoryFilter);
        request("/library/sections");
        request("/library/sections/2/all?type=8&includeGuids=1");
        assertEquals("100", artist.getPlexRatingKey());
        assertEquals(2, server.getRequestCount());
    }

    @Test
    void albumAndTrackHistoryUseTheCorrectHierarchyLevel() throws Exception {
        musicSections();
        json(
                "{\"MediaContainer\":{\"Metadata\":[{\"ratingKey\":\"200\",\"type\":\"album\",\"guid\":\"mbid://group-id\"}]}}");
        LibraryMediaItem album = new Album(ctx, albumResource);
        assertEquals(1, StreamUtils.iterateToList(album.watches()).size());
        assertEquals("parent_rating_key", lastHistoryFilter);
        assertEquals("200", lastHistoryRatingKey);
        request("/library/sections");
        request("/library/sections/2/all?type=9&includeGuids=1");
        json(
                "{\"MediaContainer\":{\"Metadata\":[{\"ratingKey\":\"300\",\"type\":\"track\",\"parentRatingKey\":\"200\",\"guid\":\"mbid://track-id\"}]}}");
        LibraryMediaItem track = new Track(
                ctx,
                TrackResource.builder()
                        .id(3)
                        .albumId(2)
                        .foreignTrackId("track-id")
                        .build());
        assertEquals(1, StreamUtils.iterateToList(track.watches()).size());
        assertEquals("rating_key", lastHistoryFilter);
        assertEquals("300", lastHistoryRatingKey);
        request("/library/sections/2/all?type=10&includeGuids=1");
    }

    @Test
    void tautulliClientSendsExplicitHistoryHierarchyFilters() throws Exception {
        TautulliClient client = new TautulliClient(
                new OkHttpClient(),
                TautulliProperties.builder()
                        .url(server.url("/").toString())
                        .apiKey("test")
                        .build(),
                ctx.getMoshi());
        json("{\"response\":{\"result\":\"success\",\"data\":{\"data\":[]}}}");
        client.getHistoryByParentRatingKey("200", 0, 25);
        var parent = server.takeRequest(2, TimeUnit.SECONDS);
        assertNotNull(parent);
        assertEquals("get_history", parent.getRequestUrl().queryParameter("cmd"));
        assertEquals("200", parent.getRequestUrl().queryParameter("parent_rating_key"));
        assertNull(parent.getRequestUrl().queryParameter("rating_key"));
        json("{\"response\":{\"result\":\"success\",\"data\":{\"data\":[]}}}");
        client.getHistoryByGrandparentRatingKey("100", 25, 25);
        var grandparent = server.takeRequest(2, TimeUnit.SECONDS);
        assertNotNull(grandparent);
        assertEquals("100", grandparent.getRequestUrl().queryParameter("grandparent_rating_key"));
        assertEquals("25", grandparent.getRequestUrl().queryParameter("start"));
        assertNull(grandparent.getRequestUrl().queryParameter("rating_key"));
    }

    @Test
    void sharedWorkflowHonorsSubtypeMatchingAndHistoryOverrides() throws Exception {
        MovieResource resource = ctx.getMoshi().adapter(MovieResource.class).fromJson("{\"id\":1,\"tmdbId\":42}");
        History history = ctx.getMoshi()
                .adapter(History.class)
                .fromJson("{\"rating_key\":\"601\",\"title\":\"Custom playback\"}");
        LibraryMediaItem item = new Movie(ctx, resource) {
            @Override
            protected Stream<PlexMediaItem> plexCandidates() {
                return Stream.of(
                        new PlexMediaItem("600", null, "tmdb://42", "movie", "First", null, null, null),
                        new PlexMediaItem("601", null, "tmdb://42", "movie", "Second", null, null, null));
            }

            @Override
            protected Optional<PlexMediaItem> selectPlexMatch(Stream<PlexMediaItem> matches) {
                return matches.reduce((first, last) -> last);
            }

            @Override
            protected Stream<History> playbackHistory(String ratingKey) {
                assertEquals("601", ratingKey);
                return Stream.of(history);
            }
        };
        assertEquals("601", item.getPlexRatingKey());
        assertEquals(
                "Custom playback",
                StreamUtils.iterateToList(item.watches()).get(0).getTitle());
        assertEquals(0, server.getRequestCount());
    }
}
