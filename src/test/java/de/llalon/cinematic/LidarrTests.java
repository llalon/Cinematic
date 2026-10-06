package de.llalon.cinematic;

import static org.junit.jupiter.api.Assertions.*;

import com.squareup.moshi.Moshi;
import de.llalon.cinematic.client.lidarr.LidarrClient;
import de.llalon.cinematic.client.lidarr.config.LidarrProperties;
import de.llalon.cinematic.client.lidarr.dto.*;
import de.llalon.cinematic.client.lidarr.exception.LidarrApiException;
import de.llalon.cinematic.client.lidarr.exception.LidarrClientException;
import de.llalon.cinematic.client.qbittorrent.QBittorrentClient;
import de.llalon.cinematic.client.qbittorrent.config.QBittorrentProperties;
import de.llalon.cinematic.client.qbittorrent.dto.QBittorrentInfo;
import de.llalon.cinematic.domain.*;
import de.llalon.cinematic.util.collections.PagePagedIterable;
import de.llalon.cinematic.util.collections.StreamUtils;
import de.llalon.cinematic.util.json.LenientDateTimeAdapter;
import de.llalon.cinematic.util.json.LenientNumberAdapterFactory;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.cache.CacheManager;
import javax.cache.Caching;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LidarrTests {
    private MockWebServer server;
    private LidarrClient client;
    private CacheManager cacheManager;
    private Library library;

    @BeforeEach
    void setUp() throws Exception {
        server = new MockWebServer();
        server.start();
        Moshi moshi = new Moshi.Builder()
                .add(new LenientDateTimeAdapter())
                .add(new LenientNumberAdapterFactory())
                .build();
        client = new LidarrClient(
                new OkHttpClient(),
                LidarrProperties.builder()
                        .url(server.url("/music").toString())
                        .apiKey("test-key")
                        .build(),
                moshi);
        cacheManager = Caching.getCachingProvider()
                .getCacheManager(
                        URI.create("lidarr-test-" + java.util.UUID.randomUUID()),
                        getClass().getClassLoader());
        // A prebuilt download client provides records without an unrelated authentication request.
        QBittorrentClient downloads =
                new QBittorrentClient(
                        new OkHttpClient(),
                        QBittorrentProperties.builder()
                                .url(server.url("/").toString())
                                .username("test")
                                .password("test")
                                .build(),
                        moshi) {
                    @Override
                    public List<QBittorrentInfo> getTorrents() {
                        try {
                            return List.of(moshi.adapter(QBittorrentInfo.class)
                                    .fromJson("{\"hash\":\"aBc123\",\"name\":\"Album torrent\"}"));
                        } catch (java.io.IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                };
        library = new Library(ClientContext.builder()
                .lidarrClient(client)
                .cacheManager(cacheManager)
                .qbittorrentClient(downloads)
                .build());
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

    private RecordedRequest request(String method, String path) throws Exception {
        RecordedRequest request = server.takeRequest(2, TimeUnit.SECONDS);
        assertNotNull(request);
        assertEquals(method, request.getMethod());
        assertEquals(path, request.getPath());
        assertEquals("test-key", request.getHeader("X-Api-Key"));
        return request;
    }

    @Test
    void configurationSupportsPropertiesAndPrebuiltClients() {
        assertSame(client, library.getContext().getLidarrClient());
        assertTrue(library.getContext().isLidarrConfigured());
        ClientContext configured = ClientContext.builder()
                .lidarrProperties(LidarrProperties.builder()
                        .url(server.url("/").toString())
                        .apiKey("test")
                        .build())
                .build();
        assertNotNull(configured.getLidarrClient());
        ClientContext absent =
                ClientContext.builder().lidarrProperties(new LidarrProperties()).build();
        assertFalse(absent.isLidarrConfigured());
        assertThrows(ClientNotConfiguredException.class, absent::getLidarrClient);
        assertThrows(
                ClientNotConfiguredException.class,
                () -> new Library(absent).artists().iterator());
        assertThrows(
                IllegalArgumentException.class,
                () -> new LidarrClient(
                        new OkHttpClient(),
                        LidarrProperties.builder().url("invalid").apiKey("test").build(),
                        new Moshi.Builder().build()));
        assertThrows(
                IllegalArgumentException.class,
                () -> new LidarrClient(
                        new OkHttpClient(),
                        LidarrProperties.builder()
                                .url(server.url("/").toString())
                                .apiKey(" ")
                                .build(),
                        new Moshi.Builder().build()));
    }

    @Test
    void artistsSupportCrudLookupAndApiErrors() throws Exception {
        json("[{\"id\":1,\"artistName\":\"Artist\",\"foreignArtistId\":\"mb-id\",\"tags\":[3]}]");
        assertEquals("Artist", client.getAllArtists().get(0).getArtistName());
        request("GET", "/music/api/v1/artist");
        json("[]");
        client.getArtistsByMusicBrainzId("mb-id");
        request("GET", "/music/api/v1/artist?mbId=mb-id");
        json("[]");
        client.lookupArtists("A & B");
        request("GET", "/music/api/v1/artist/lookup?term=A%20%26%20B");
        ArtistResource artist = ArtistResource.builder()
                .artistName("Artist")
                .foreignArtistId("mb-id")
                .qualityProfileId(1)
                .metadataProfileId(2)
                .rootFolderPath("/music")
                .build();
        json("{\"id\":1,\"artistName\":\"Artist\"}");
        assertEquals(1, client.addArtist(artist).getId());
        assertTrue(request("POST", "/music/api/v1/artist").getBody().readUtf8().contains("\"metadataProfileId\":2"));
        json("{\"id\":1}");
        client.updateArtist(ArtistResource.builder().id(1).monitored(false).build());
        request("PUT", "/music/api/v1/artist/1");
        server.enqueue(new MockResponse().setResponseCode(204));
        client.deleteArtist(1, true, false);
        request("DELETE", "/music/api/v1/artist/1?deleteFiles=true&addImportListExclusion=false");
        server.enqueue(new MockResponse().setResponseCode(429).setBody("rate limited"));
        LidarrApiException error = assertThrows(
                LidarrApiException.class,
                () -> client.updateArtist(ArtistResource.builder().id(1).build()));
        assertEquals(429, error.getStatusCode());
        assertEquals("rate limited", error.getResponseBody());
        assertTrue(error.isRetryable());
        request("PUT", "/music/api/v1/artist/1");
    }

    @Test
    void albumsTracksFilesAndTagsUseLidarrParameters() throws Exception {
        json("[{\"id\":2,\"artistId\":1,\"releaseDate\":\"2020-01-01T00:00:00Z\"}]");
        assertEquals(2020, client.getAlbumsByArtist(1).get(0).getReleaseDate().getYear());
        request("GET", "/music/api/v1/album?artistId=1");
        json("{\"id\":2}");
        client.addAlbum(AlbumResource.builder()
                .title("Album")
                .artistId(1)
                .foreignAlbumId("mb-album")
                .build());
        request("POST", "/music/api/v1/album");
        json("{\"id\":2}");
        client.updateAlbum(AlbumResource.builder().id(2).monitored(false).build());
        request("PUT", "/music/api/v1/album/2");
        json("[]");
        client.lookupAlbums("lidarr:mb-album");
        request("GET", "/music/api/v1/album/lookup?term=lidarr%3Amb-album");
        json("[{\"id\":4,\"trackNumber\":\"A1\",\"duration\":123000}]");
        assertEquals("A1", client.getTracksByAlbum(2).get(0).getTrackNumber());
        request("GET", "/music/api/v1/track?albumId=2");
        json("[]");
        client.getTracksByArtist(1);
        request("GET", "/music/api/v1/track?artistId=1");
        json("[{\"id\":5,\"size\":4000000000,\"mediaInfo\":{\"audioCodec\":\"FLAC\",\"audioChannels\":2}}]");
        assertEquals(4000000000L, client.getTrackFilesByAlbum(2).get(0).getSize());
        request("GET", "/music/api/v1/trackfile?albumId=2");
        json("{\"id\":5}");
        client.updateTrackFile(TrackFileResource.builder().id(5).build());
        request("PUT", "/music/api/v1/trackfile/5");
        json("{\"id\":3,\"label\":\"rock\"}");
        assertEquals(
                "rock",
                client.createTag(LidarrTag.builder().label("rock").build()).getLabel());
        request("POST", "/music/api/v1/tag");
        json("{\"id\":3}");
        client.updateTag(LidarrTag.builder().id(3).label("pop").build());
        request("PUT", "/music/api/v1/tag/3");
        server.enqueue(new MockResponse().setResponseCode(204));
        client.deleteTag(3);
        request("DELETE", "/music/api/v1/tag/3");
        server.enqueue(new MockResponse().setResponseCode(204));
        client.deleteAlbum(2, false, true);
        request("DELETE", "/music/api/v1/album/2?deleteFiles=false&addImportListExclusion=true");
    }

    @Test
    void queueUsesArtistAlbumFlagsAndPages() throws Exception {
        json("{\"page\":1,\"records\":[{\"id\":7,\"artistId\":1,\"albumId\":2,\"downloadId\":\"ABC123\"}]}");
        json("{\"page\":2,\"records\":[{\"id\":8}]}");
        json("{\"page\":3,\"records\":[]}");
        List<LidarrQueue> records = StreamUtils.iterateToList(new PagePagedIterable<>(
                (page, pageSize) -> client.getQueue(page, pageSize, true, true).getRecords(), 1));
        assertEquals(2, records.size());
        for (int page = 1; page <= 3; page++) {
            request("GET", "/music/api/v1/queue?page=" + page + "&pageSize=1&includeArtist=true&includeAlbum=true");
        }
        json("[]");
        client.getQueueForAlbum(2);
        request("GET", "/music/api/v1/queue/details?albumIds=2&includeArtist=true&includeAlbum=true");
        json("[]");
        client.getQueueForArtist(1);
        request("GET", "/music/api/v1/queue/details?artistId=1&includeArtist=true&includeAlbum=true");
        server.enqueue(new MockResponse().setResponseCode(204));
        client.deleteQueueItem(7, true, false, true);
        request("DELETE", "/music/api/v1/queue/7?blocklist=true&removeFromClient=false&skipRedownload=true");
    }

    @Test
    void parseAndHttpErrorsRemainDistinct() throws Exception {
        json("{\"id\":\"invalid-number\"}");
        assertThrows(LidarrClientException.class, () -> client.getArtist(1));
        request("GET", "/music/api/v1/artist/1");
        json("not json");
        assertThrows(LidarrClientException.class, () -> client.getArtist(1));
        request("GET", "/music/api/v1/artist/1");
        server.enqueue(new MockResponse().setResponseCode(401).setBody("unauthorized"));
        LidarrApiException error = assertThrows(LidarrApiException.class, () -> client.deleteTrackFile(5));
        assertFalse(error.isRetryable());
        assertEquals(401, error.getStatusCode());
        request("DELETE", "/music/api/v1/trackfile/5");
    }

    @Test
    void domainCollectionsCacheAndNavigateMusicFilesAndTags() throws Exception {
        json("[{\"id\":1,\"artistName\":\"Artist\",\"tags\":[3]}]");
        Artist artist = StreamUtils.iterateToList(library.artists()).get(0);
        request("GET", "/music/api/v1/artist");
        assertEquals(
                "Artist", StreamUtils.iterateToList(library.artists()).get(0).getName());
        assertEquals(1, server.getRequestCount());
        json("[{\"id\":2,\"artistId\":1,\"title\":\"Album\",\"artist\":{\"id\":1,\"artistName\":\"Artist\"}}]");
        Album album = StreamUtils.iterateToList(artist.albums()).get(0);
        request("GET", "/music/api/v1/album?artistId=1");
        assertEquals("Artist", album.artist().getName());
        json("[{\"id\":4,\"title\":\"Track\",\"albumId\":2,\"trackFileId\":5},{\"id\":6,\"trackFileId\":0}]");
        List<Track> tracks = StreamUtils.iterateToList(album.tracks());
        request("GET", "/music/api/v1/track?albumId=2");
        assertFalse(tracks.get(1).files().iterator().hasNext());
        json("{\"id\":2,\"title\":\"Album\"}");
        assertEquals("Album", tracks.get(0).album().getTitle());
        request("GET", "/music/api/v1/album/2");
        json(
                "{\"id\":5,\"artistId\":1,\"albumId\":2,\"path\":\"/music/track.flac\",\"mediaInfo\":{\"audioCodec\":\"FLAC\",\"audioChannels\":2}}");
        TrackFile file = tracks.get(0).files().iterator().next();
        request("GET", "/music/api/v1/trackfile/5");
        assertEquals("FLAC", file.getFormat().getAudioCodec());
        assertEquals(2.0, file.getFormat().getAudioChannels());
        assertNull(file.getFormat().getVideoCodec());
        assertEquals("/music/track.flac", file.getPath());
        assertEquals("Album", file.album().getTitle());
        json("[{\"id\":3,\"label\":\"rock\"}]");
        Tag tag = StreamUtils.iterateToList(artist.tags()).get(0);
        request("GET", "/music/api/v1/tag");
        assertTrue(artist.hasTag("ROCK"));
        assertEquals("Artist", StreamUtils.iterateToList(tag.artists()).get(0).getName());
        server.enqueue(new MockResponse().setResponseCode(204));
        file.delete();
        request("DELETE", "/music/api/v1/trackfile/5");
        json("{\"id\":5,\"path\":\"updated.flac\"}");
        assertEquals("updated.flac", tracks.get(0).files().iterator().next().getPath());
        request("GET", "/music/api/v1/trackfile/5");
        library.invalidateCache();
        json("[]");
        assertTrue(StreamUtils.iterateToList(library.artists()).isEmpty());
        request("GET", "/music/api/v1/artist");
    }

    @Test
    void lidarrOnlyLibraryExposesRootAlbumsTracksAndTags() throws Exception {
        Library music = new Library(ClientContext.builder()
                .lidarrClient(client)
                .cacheManager(cacheManager)
                .sonarrProperties(new de.llalon.cinematic.client.sonarr.config.SonarrProperties())
                .radarrProperties(new de.llalon.cinematic.client.radarr.config.RadarrProperties())
                .qbittorrentProperties(new QBittorrentProperties())
                .build());
        json("[{\"id\":2,\"title\":\"Album\"}]");
        assertEquals("Album", StreamUtils.iterateToList(music.albums()).get(0).getTitle());
        request("GET", "/music/api/v1/album");
        json("[{\"id\":4,\"title\":\"Track\",\"artistId\":1,\"trackFile\":{\"id\":5,\"path\":\"embedded.flac\"}}]");
        Track track = StreamUtils.iterateToList(music.tracks()).get(0);
        request("GET", "/music/api/v1/track?albumId=2");
        assertEquals("embedded.flac", track.files().iterator().next().getPath());
        json("{\"id\":1,\"artistName\":\"Artist\"}");
        assertEquals("Artist", track.artist().getName());
        request("GET", "/music/api/v1/artist/1");
        json("[{\"id\":3,\"label\":\"rock\"}]");
        assertEquals("rock", StreamUtils.iterateToList(music.tags()).get(0).getName());
        request("GET", "/music/api/v1/tag");
    }

    @Test
    void torrentsNavigateBothWaysAndBlacklistLidarrOnly() throws Exception {
        // Disable Sonarr and Radarr explicitly, irrespective of the developer's environment.
        library = new Library(ClientContext.builder()
                .lidarrClient(client)
                .cacheManager(cacheManager)
                .qbittorrentClient(library.getContext().getQbittorrentClient())
                .sonarrProperties(new de.llalon.cinematic.client.sonarr.config.SonarrProperties())
                .radarrProperties(new de.llalon.cinematic.client.radarr.config.RadarrProperties())
                .build());
        json("[{\"id\":1,\"artistName\":\"Artist\"}]");
        Artist artist = StreamUtils.iterateToList(library.artists()).get(0);
        request("GET", "/music/api/v1/artist");
        json(
                "{\"records\":[{\"id\":7,\"artistId\":1,\"albumId\":2,\"downloadId\":\"ABC123\"},{\"id\":8,\"artistId\":1,\"albumId\":2,\"downloadId\":\"ABC123\"},{\"id\":9}]}");
        List<Torrent> torrents = StreamUtils.iterateToList(artist.torrents());
        assertEquals(1, torrents.size());
        RecordedRequest queueRequest = server.takeRequest(2, TimeUnit.SECONDS);
        assertNotNull(queueRequest);
        assertTrue(queueRequest.getPath().startsWith("/music/api/v1/queue?page=1&"));
        json("{\"id\":1,\"artistName\":\"Artist\"}");
        assertEquals(1, StreamUtils.iterateToList(torrents.get(0).artists()).size());
        request("GET", "/music/api/v1/artist/1");
        json("{\"id\":2,\"title\":\"Album\"}");
        Album album = StreamUtils.iterateToList(torrents.get(0).albums()).get(0);
        request("GET", "/music/api/v1/album/2");
        assertEquals(1, StreamUtils.iterateToList(album.torrents()).size());
        server.enqueue(new MockResponse().setResponseCode(204));
        server.enqueue(new MockResponse().setResponseCode(204));
        torrents.get(0).blacklist();
        request("DELETE", "/music/api/v1/queue/7?blocklist=true&removeFromClient=false&skipRedownload=true");
        request("DELETE", "/music/api/v1/queue/8?blocklist=true&removeFromClient=false&skipRedownload=true");
    }
}
