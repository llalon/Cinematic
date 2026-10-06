package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.lidarr.dto.TrackResource;
import de.llalon.cinematic.client.plex.dto.PlexMediaItem;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/** Domain representation of a Lidarr track, with lazy, cached relationships. */
public class Track extends LibraryMediaItem {
    private final TrackResource resource;

    Track(ClientContext ctx, TrackResource resource) {
        super(ctx, LibraryMediaType.TRACK);
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

    /** Returns the trackNumber reported by Lidarr. */
    public String getTrackNumber() {
        return resource.getTrackNumber();
    }

    /** Returns the mediumNumber reported by Lidarr. */
    public Integer getMediumNumber() {
        return resource.getMediumNumber();
    }

    /** Returns the duration reported by Lidarr. */
    public Integer getDuration() {
        return resource.getDuration();
    }

    /** Returns the hasFile reported by Lidarr. */
    public Boolean getHasFile() {
        return resource.getHasFile();
    }

    /** Returns the foreignTrackId reported by Lidarr. */
    public String getForeignTrackId() {
        return resource.getForeignTrackId();
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
    /** Returns the parent album. */
    public Album album() {
        return new Album(ctx, lidarrAlbumById(resource.getAlbumId()));
    }
    /** Returns the imported audio file, or an empty iterable for an unimported track. */
    public Iterable<TrackFile> files() {
        return () -> {
            if (resource.getTrackFile() != null) {
                return List.of(new TrackFile(ctx, resource.getTrackFile())).iterator();
            }
            if (resource.getTrackFileId() == null || resource.getTrackFileId() <= 0) {
                return Collections.emptyIterator();
            }
            return List.of(new TrackFile(ctx, lidarrTrackFile(resource.getTrackFileId())))
                    .iterator();
        };
    }

    @Override
    protected Stream<String> musicBrainzIds() {
        return Stream.of(resource.getForeignTrackId(), resource.getForeignRecordingId());
    }

    /** Returns the MusicBrainz recording identifier. */
    public String getForeignRecordingId() {
        return resource.getForeignRecordingId();
    }
    /** Returns the parent artist's tags. */
    @Override
    public Iterable<Tag> tags() {
        return artist().tags();
    }

    /** Returns downloads associated with the parent album. */
    @Override
    public Iterable<Torrent> torrents() {
        return album().torrents();
    }

    @Override
    protected Stream<PlexMediaItem> plexCandidates() {
        if (musicBrainzIds().noneMatch(id -> id != null && !id.isBlank())) {
            return Stream.empty();
        }
        String parent = album().getPlexRatingKey();
        if (parent == null) {
            return Stream.empty();
        }
        return plexCandidatesWithExternalIds(musicBrainzIds(), item -> parent.equals(item.getParentRatingKey()));
    }

    @Override
    protected Optional<PlexMediaItem> selectPlexMatch(Stream<PlexMediaItem> matches) {
        return uniquePlexMatch(matches);
    }
}
