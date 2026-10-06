package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.lidarr.dto.ArtistResource;
import de.llalon.cinematic.client.plex.dto.PlexMediaItem;
import de.llalon.cinematic.client.tautulli.dto.History;
import java.util.Collections;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/** Domain representation of a Lidarr artist, with lazy, cached relationships. */
public class Artist extends LibraryMediaItem {
    private final ArtistResource resource;

    Artist(ClientContext ctx, ArtistResource resource) {
        super(ctx, LibraryMediaType.ARTIST);
        this.resource = resource;
    }

    /** Returns the id reported by Lidarr. */
    public Integer getId() {
        return resource.getId();
    }

    /** Returns the artistName reported by Lidarr. */
    public String getArtistName() {
        return resource.getArtistName();
    }

    /** Returns the foreignArtistId reported by Lidarr. */
    public String getForeignArtistId() {
        return resource.getForeignArtistId();
    }

    /** Returns the status reported by Lidarr. */
    public String getStatus() {
        return resource.getStatus();
    }

    /** Returns the path reported by Lidarr. */
    public String getPath() {
        return resource.getPath();
    }

    /** Returns the monitored reported by Lidarr. */
    public Boolean getMonitored() {
        return resource.getMonitored();
    }

    /** Returns the artist display name. */
    public String getName() {
        return resource.getArtistName();
    }

    /** Returns audio format metadata for imported files. */
    public Iterable<MediaFormat> formats() {
        return () -> StreamSupport.stream(files().spliterator(), false)
                .map(TrackFile::format)
                .filter(Objects::nonNull)
                .iterator();
    }

    /** Returns this artist's albums. */
    public Iterable<Album> albums() {
        return () -> lidarrAlbumsByArtist(resource.getId())
                .map(a -> new Album(ctx, a))
                .iterator();
    }
    /** Returns this artist's tracks. */
    public Iterable<Track> tracks() {
        return () -> lidarrAlbumsByArtist(resource.getId())
                .flatMap(a -> lidarrTracksByAlbum(a.getId()))
                .map(t -> new Track(ctx, t))
                .iterator();
    }
    /** Returns imported audio files for this artist. */
    public Iterable<TrackFile> files() {
        return () -> lidarrTrackFilesByArtist(resource.getId())
                .map(f -> new TrackFile(ctx, f))
                .iterator();
    }
    /** Returns the artist's assigned tags. */
    @Override
    public Iterable<Tag> tags() {
        return () -> resource.getTags() == null
                ? Collections.emptyIterator()
                : lidarrTags()
                        .filter(t -> resource.getTags().contains(t.getId()))
                        .map(t -> new Tag(ctx, t.getLabel()))
                        .iterator();
    }
    /** Tests whether this artist has a tag by name. */
    @Override
    public boolean hasTag(String name) {
        return name != null
                && StreamSupport.stream(tags().spliterator(), false).anyMatch(t -> name.equalsIgnoreCase(t.getName()));
    }
    /** Tests whether this artist has a tag. */
    @Override
    public boolean hasTag(Tag tag) {
        return hasTag(tag.getName());
    }
    /** Returns torrents correlated through Lidarr's download queue. */
    @Override
    public Iterable<Torrent> torrents() {
        return () -> {
            Set<String> hashes = lidarrQueue()
                    .filter(q -> Objects.equals(q.getArtistId(), resource.getId()))
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
        return Stream.of(resource.getForeignArtistId(), resource.getMbId());
    }

    @Override
    protected Stream<History> playbackHistory(String ratingKey) {
        return tautulliHistoryByGrandparentRatingKey(ratingKey);
    }

    @Override
    protected Stream<PlexMediaItem> plexCandidates() {
        return plexCandidatesWithExternalIds(musicBrainzIds());
    }

    @Override
    protected Optional<PlexMediaItem> selectPlexMatch(Stream<PlexMediaItem> matches) {
        return uniquePlexMatch(matches);
    }
}
