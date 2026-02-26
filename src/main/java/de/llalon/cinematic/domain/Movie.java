package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.radarr.dto.MovieResource;
import de.llalon.cinematic.client.radarr.dto.RadarrTag;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class Movie extends LibraryMediaItem {

    @NotNull
    private final MovieResource radarrMovie;

    public String getTmdbId() {
        if (this.radarrMovie.getTmdbId() == null) {
            return null;
        }
        return this.radarrMovie.getTmdbId().toString();
    }

    /**
     * Creates a new Movie instance with the given client context and Radarr movie resource.
     *
     * @param ctx the client context
     * @param radarrMovie the Radarr movie resource
     */
    Movie(@NotNull ClientContext ctx, @NotNull MovieResource radarrMovie) {
        super(ctx, radarrMovie);
        this.radarrMovie = radarrMovie;
    }

    /**
     * Returns the tags associated with this movie.
     *
     * @return an iterable of Tag objects
     */
    @Override
    @NotNull
    public Iterable<Tag> tags() {
        return () -> {
            final Map<Integer, String> tags =
                    radarrTags().collect(Collectors.toMap(RadarrTag::getId, RadarrTag::getLabel));

            return radarrMovie.getTags().stream()
                    .map(tagId -> {
                        if (tags.containsKey(tagId)) {
                            return new Tag(ctx, tags.get(tagId));
                        } else {
                            // This can happen when tags are removed or added. Usually they still exist with a different
                            // ID.
                            // For example, tag1 could have id 1, and 2, but 2 doesn't exist.
                            log.warn("Tag with id {} not found in Radarr", tagId);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .iterator();
        };
    }

    /**
     * Returns the torrents associated with this movie.
     *
     * @return an iterable of Torrent objects
     */
    @Override
    @NotNull
    public Iterable<Torrent> torrents() {
        return () -> radarrQueue()
                .filter(queueResource -> queueResource.getMovieId().equals(radarrMovie.getId()))
                .flatMap(queueResource -> super.qbittorrentTorrents()
                        .filter(torrent -> torrent.getHash() != null && queueResource.getDownloadId() != null)
                        .filter(torrent -> torrent.getHash().equalsIgnoreCase(queueResource.getDownloadId()))
                        .map(torrent -> new Torrent(ctx, torrent)))
                .iterator();
    }

    public String getTitle() {
        return this.radarrMovie.getTitle();
    }

    public Integer getYear() {
        return this.radarrMovie.getYear();
    }

    public String getStatus() {
        return this.radarrMovie.getStatus();
    }

    public Boolean getHasFile() {
        return this.radarrMovie.getHasFile();
    }
}
