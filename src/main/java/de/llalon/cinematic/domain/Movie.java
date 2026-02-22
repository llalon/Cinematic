package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.qbittorrent.dto.TorrentInfo;
import de.llalon.cinematic.client.radarr.dto.MovieResource;
import de.llalon.cinematic.client.radarr.dto.TagResource;
import de.llalon.cinematic.util.collections.PagePagedIterable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class Movie extends LibraryMediaItem {

    @Delegate
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
            final Map<Integer, String> tags = ctx.getRadarrClient().getAllTags().stream()
                    .collect(Collectors.toMap(TagResource::getId, TagResource::getLabel));

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
        return () -> {
            final List<TorrentInfo> allTorrents = ctx.getQbittorrentClient().getTorrents();
            return new PagePagedIterable<>((take, skip) ->
                            ctx.getRadarrClient().getQueue(take, skip, false).getRecords())
                    .stream()
                            .filter(queueResource -> queueResource.getMovieId().equals(radarrMovie.getId()))
                            .flatMap(queueResource -> allTorrents.stream()
                                    .filter(torrent ->
                                            torrent.getHash() != null && queueResource.getDownloadId() != null)
                                    .filter(torrent ->
                                            torrent.getHash().equalsIgnoreCase(queueResource.getDownloadId()))
                                    .map(torrent -> new Torrent(ctx, torrent)))
                            .iterator();
        };
    }
}
