package de.llalon.cinematic.client.lidarr;

import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import de.llalon.cinematic.client.lidarr.config.LidarrProperties;
import de.llalon.cinematic.client.lidarr.dto.*;
import de.llalon.cinematic.client.lidarr.exception.LidarrApiException;
import de.llalon.cinematic.client.lidarr.exception.LidarrClientException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import okhttp3.*;

/** Synchronous, API-key authenticated client for Lidarr's v1 API. */
public class LidarrClient {
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    private final OkHttpClient httpClient;
    private final Moshi moshi;
    private final HttpUrl baseUrl;
    private final String apiKey;

    /**
     * Creates a Lidarr client using shared HTTP and JSON infrastructure.
     * @param httpClient HTTP client
     * @param properties server URL (including any reverse-proxy prefix) and API key
     * @param moshi JSON adapters
     */
    public LidarrClient(OkHttpClient httpClient, LidarrProperties properties, Moshi moshi) {
        this.httpClient = httpClient;
        this.moshi = moshi;
        this.baseUrl = properties.getUrl() == null ? null : HttpUrl.parse(properties.getUrl());
        if (baseUrl == null) {
            throw new IllegalArgumentException("Invalid Lidarr URL: " + properties.getUrl());
        }
        this.apiKey = properties.getApiKey();
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("Lidarr API key must not be blank");
        }
    }

    /** Returns all artists. */
    public List<ArtistResource> getAllArtists() {
        return getList(url("artist"), ArtistResource.class);
    }

    /** Returns an artist by its Lidarr ID. */
    public ArtistResource getArtist(int artistId) {
        return get(url("artist/" + artistId), ArtistResource.class);
    }

    /** Adds an artist; callers must check for duplicates before adding. */
    public ArtistResource addArtist(ArtistResource artist) {
        return write("POST", url("artist"), artist, ArtistResource.class);
    }

    /** Updates an artist. */
    public ArtistResource updateArtist(ArtistResource artist) {
        return write("PUT", url("artist/" + artist.getId()), artist, ArtistResource.class);
    }

    /** Deletes an artist, optionally deleting files and excluding future imports. */
    public void deleteArtist(int artistId, boolean deleteFiles, boolean addImportListExclusion) {
        delete(url("artist/" + artistId)
                .newBuilder()
                .addQueryParameter("deleteFiles", String.valueOf(deleteFiles))
                .addQueryParameter("addImportListExclusion", String.valueOf(addImportListExclusion))
                .build());
    }

    /** Looks up artists by a search term. */
    public List<ArtistResource> lookupArtists(String term) {
        return getList(query("artist/lookup", "term", term), ArtistResource.class);
    }

    /** Returns all albums. */
    public List<AlbumResource> getAllAlbums() {
        return getList(url("album"), AlbumResource.class);
    }

    /** Returns an album by its Lidarr ID. */
    public AlbumResource getAlbum(int albumId) {
        return get(url("album/" + albumId), AlbumResource.class);
    }

    /** Adds an album; callers must check for duplicates before adding. */
    public AlbumResource addAlbum(AlbumResource album) {
        return write("POST", url("album"), album, AlbumResource.class);
    }

    /** Updates an album. */
    public AlbumResource updateAlbum(AlbumResource album) {
        return write("PUT", url("album/" + album.getId()), album, AlbumResource.class);
    }

    /** Deletes an album, optionally deleting files and excluding future imports. */
    public void deleteAlbum(int albumId, boolean deleteFiles, boolean addImportListExclusion) {
        delete(url("album/" + albumId)
                .newBuilder()
                .addQueryParameter("deleteFiles", String.valueOf(deleteFiles))
                .addQueryParameter("addImportListExclusion", String.valueOf(addImportListExclusion))
                .build());
    }

    /** Looks up albums by a search term. */
    public List<AlbumResource> lookupAlbums(String term) {
        return getList(query("album/lookup", "term", term), AlbumResource.class);
    }

    /** Returns artists matching a MusicBrainz ID. */
    public List<ArtistResource> getArtistsByMusicBrainzId(String mbId) {
        return getList(query("artist", "mbId", mbId), ArtistResource.class);
    }

    /** Returns albums belonging to an artist. */
    public List<AlbumResource> getAlbumsByArtist(int artistId) {
        return getList(query("album", "artistId", artistId), AlbumResource.class);
    }

    /** Returns tracks belonging to an artist. */
    public List<TrackResource> getTracksByArtist(int artistId) {
        return getList(query("track", "artistId", artistId), TrackResource.class);
    }

    /** Returns tracks belonging to an album. */
    public List<TrackResource> getTracksByAlbum(int albumId) {
        return getList(query("track", "albumId", albumId), TrackResource.class);
    }

    /** Returns a track by its Lidarr ID. */
    public TrackResource getTrack(int id) {
        return get(url("track/" + id), TrackResource.class);
    }

    /** Returns track files belonging to an artist. */
    public List<TrackFileResource> getTrackFilesByArtist(int artistId) {
        return getList(query("trackfile", "artistId", artistId), TrackFileResource.class);
    }

    /** Returns track files belonging to an album. */
    public List<TrackFileResource> getTrackFilesByAlbum(int albumId) {
        return getList(query("trackfile", "albumId", albumId), TrackFileResource.class);
    }

    /** Returns a track file by its Lidarr ID. */
    public TrackFileResource getTrackFile(int id) {
        return get(url("trackfile/" + id), TrackFileResource.class);
    }

    /** Updates an imported track file. */
    public TrackFileResource updateTrackFile(TrackFileResource trackFile) {
        return write("PUT", url("trackfile/" + trackFile.getId()), trackFile, TrackFileResource.class);
    }

    /** Deletes an imported track file from Lidarr and disk. */
    public void deleteTrackFile(int id) {
        delete(url("trackfile/" + id));
    }

    /** Returns one page of the download queue, with optional artist and album resources. */
    public QueueResourcePagingResource getQueue(int page, int pageSize, boolean includeArtist, boolean includeAlbum) {
        return get(
                url("queue")
                        .newBuilder()
                        .addQueryParameter("page", String.valueOf(page))
                        .addQueryParameter("pageSize", String.valueOf(pageSize))
                        .addQueryParameter("includeArtist", String.valueOf(includeArtist))
                        .addQueryParameter("includeAlbum", String.valueOf(includeAlbum))
                        .build(),
                QueueResourcePagingResource.class);
    }

    /** Returns queue entries for an artist. */
    public List<LidarrQueue> getQueueForArtist(int artistId) {
        return getList(
                query("queue/details", "artistId", artistId)
                        .newBuilder()
                        .addQueryParameter("includeArtist", "true")
                        .addQueryParameter("includeAlbum", "true")
                        .build(),
                LidarrQueue.class);
    }

    /** Returns queue entries for an album. */
    public List<LidarrQueue> getQueueForAlbum(int albumId) {
        return getList(
                query("queue/details", "albumIds", albumId)
                        .newBuilder()
                        .addQueryParameter("includeArtist", "true")
                        .addQueryParameter("includeAlbum", "true")
                        .build(),
                LidarrQueue.class);
    }

    /** Removes a queue entry with explicit blocklist, client removal, and replacement search controls. */
    public void deleteQueueItem(int id, boolean blocklist, boolean removeFromClient, boolean skipRedownload) {
        delete(url("queue/" + id)
                .newBuilder()
                .addQueryParameter("blocklist", String.valueOf(blocklist))
                .addQueryParameter("removeFromClient", String.valueOf(removeFromClient))
                .addQueryParameter("skipRedownload", String.valueOf(skipRedownload))
                .build());
    }

    /** Returns all tags. */
    public List<LidarrTag> getAllTags() {
        return getList(url("tag"), LidarrTag.class);
    }

    /** Returns a tag by ID. */
    public LidarrTag getTag(int id) {
        return get(url("tag/" + id), LidarrTag.class);
    }

    /** Creates or updates a tag. */
    public LidarrTag createTag(LidarrTag tag) {
        return write("POST", url("tag"), tag, LidarrTag.class);
    }

    /** Creates or updates a tag. */
    public LidarrTag updateTag(LidarrTag tag) {
        return write("PUT", url("tag/" + tag.getId()), tag, LidarrTag.class);
    }

    /** Deletes a tag. */
    public void deleteTag(int id) {
        delete(url("tag/" + id));
    }

    private HttpUrl url(String path) {
        // A trailing slash makes OkHttp append rather than replace the final prefix segment.
        String prefix = baseUrl.encodedPath();
        return baseUrl.newBuilder()
                .encodedPath(prefix.endsWith("/") ? prefix : prefix + "/")
                .addPathSegments("api/v1/" + path)
                .build();
    }

    private HttpUrl query(String path, String key, Object value) {
        return url(path)
                .newBuilder()
                .addQueryParameter(key, String.valueOf(value))
                .build();
    }

    private <T> List<T> getList(HttpUrl url, Class<T> type) {
        return get(url, Types.newParameterizedType(List.class, type));
    }

    private <T> T get(HttpUrl url, Type type) {
        return execute(
                new Request.Builder().url(url).header("X-Api-Key", apiKey).get().build(), type);
    }

    private <T> T write(String method, HttpUrl url, Object body, Type type) {
        final String json;
        try {
            json = toJson(body);
        } catch (RuntimeException e) {
            throw new LidarrClientException("Failed to serialize Lidarr request: " + url, e);
        }
        return execute(
                new Request.Builder()
                        .url(url)
                        .header("X-Api-Key", apiKey)
                        .method(method, RequestBody.create(json, JSON))
                        .build(),
                type);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private String toJson(Object body) {
        JsonAdapter adapter = moshi.adapter(body.getClass());
        return adapter.toJson(body);
    }

    private void delete(HttpUrl url) {
        execute(
                new Request.Builder()
                        .url(url)
                        .header("X-Api-Key", apiKey)
                        .delete()
                        .build(),
                null);
    }

    private <T> T execute(Request request, Type type) {
        try (Response response = httpClient.newCall(request).execute()) {
            String body = response.body() == null ? "" : response.body().string();
            if (!response.isSuccessful()) {
                throw new LidarrApiException(
                        "Lidarr API request failed: HTTP " + response.code(), response.code(), body);
            }
            if (type == null || body.isEmpty()) {
                return null;
            }
            try {
                JsonAdapter<T> adapter = moshi.adapter(type);
                return adapter.fromJson(body);
            } catch (IOException | RuntimeException e) {
                throw new LidarrClientException("Failed to parse Lidarr response: " + request.url(), e);
            }
        } catch (IOException e) {
            throw new LidarrApiException("Failed to execute Lidarr request: " + request.url(), e);
        }
    }
}
