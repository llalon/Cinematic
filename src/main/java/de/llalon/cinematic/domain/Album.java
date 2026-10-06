package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.lidarr.dto.AlbumResource;
import de.llalon.cinematic.client.plex.dto.PlexMediaItem;
import de.llalon.cinematic.client.tautulli.dto.History;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/** Domain representation of a Lidarr album, with lazy, cached relationships. */
public class Album extends LibraryMediaItem {
    private final AlbumResource resource;

    Album(ClientContext ctx, AlbumResource resource) {
        super(ctx, LibraryMediaType.ALBUM);
        this.resource = resource;
    }

    /** Returns the id reported by Lidarr. */
    public Integer getId() {
        return resource.getId();
    }

    /** Returns the title reported by Lidarr. */
    public String getTitle() {
        return resource.getTitle();
    }

    /** Returns the foreignAlbumId reported by Lidarr. */
    public String getForeignAlbumId() {
        return resource.getForeignAlbumId();
    }

    /** Returns the monitored reported by Lidarr. */
    public Boolean getMonitored() {
        return resource.getMonitored();
    }

    /** Returns the albumType reported by Lidarr. */
    public String getAlbumType() {
        return resource.getAlbumType();
    }

    /** Returns the releaseDate reported by Lidarr. */
    public LocalDateTime getReleaseDate() {
        return resource.getReleaseDate();
    }

    /** Returns audio format metadata for imported files. */
    public Iterable<MediaFormat> formats() {
        return () -> StreamSupport.stream(files().spliterator(), false)
                .map(TrackFile::format)
                .filter(Objects::nonNull)
                .iterator();
    }

    /** Returns the parent artist, using embedded metadata when available. */
    public Artist artist() {
        return new Artist(
                ctx, resource.getArtist() != null ? resource.getArtist() : lidarrArtistById(resource.getArtistId()));
    }
    /** Returns this album's tracks. */
    public Iterable<Track> tracks() {
        return () -> lidarrTracksByAlbum(resource.getId())
                .map(t -> new Track(ctx, t))
                .iterator();
    }
    /** Returns imported audio files for this album. */
    public Iterable<TrackFile> files() {
        return () -> lidarrTrackFilesByAlbum(resource.getId())
                .map(f -> new TrackFile(ctx, f))
                .iterator();
    }
    /** Returns the parent artist's tags. */
    @Override
    public Iterable<Tag> tags() {
        return artist().tags();
    }
    /** Returns torrents correlated through Lidarr's download queue. */
    @Override
    public Iterable<Torrent> torrents() {
        return () -> {
            Set<String> hashes = lidarrQueue()
                    .filter(q -> Objects.equals(q.getAlbumId(), resource.getId()))
                    .map(q -> q.getDownloadId())
                    .filter(Objects::nonNull)
                    .map(h -> h.toLowerCase(Locale.ROOT))
                    .collect(Collectors.toSet());
            return qbittorrentTorrents()
                    .filter(t ->
                            t.getHash() != null && hashes.contains(t.getHash().toLowerCase(Locale.ROOT)))
                    .map(t -> new Torrent(ctx, t))
                    .iterator();
        };
    }

    @Override
    protected Stream<String> musicBrainzIds() {
        return Stream.concat(
                Stream.of(resource.getForeignAlbumId()),
                resource.getReleases() == null
                        ? Stream.empty()
                        : resource.getReleases().stream()
                                .filter(Objects::nonNull)
                                .map(release -> release.getForeignReleaseId()));
    }

    @Override
    protected Stream<History> playbackHistory(String ratingKey) {
        return tautulliHistoryByParentRatingKey(ratingKey);
    }

    @Override
    protected Stream<PlexMediaItem> plexCandidates() {
        return plexCandidatesWithExternalIds(musicBrainzIds());
    }

    @Override
    protected boolean hasExternalPlexIdentifiers(PlexMediaItem item) {
        return hasPlexGuidScheme(item.getGuid(), LibraryIdType.MBID) || super.hasExternalPlexIdentifiers(item);
    }

    @Override
    protected Optional<PlexMediaItem> selectPlexMatch(Stream<PlexMediaItem> matches) {
        return uniquePlexMatch(matches);
    }
}
