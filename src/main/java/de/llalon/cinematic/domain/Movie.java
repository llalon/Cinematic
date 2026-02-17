package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.qbittorrent.dto.TorrentInfo;
import de.llalon.cinematic.client.radarr.dto.MovieResource;
import de.llalon.cinematic.client.radarr.dto.TagResource;
import de.llalon.cinematic.util.collections.PagePagedIterable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.experimental.Delegate;

public class Movie extends LibraryMediaItem {

    @Delegate
    private final MovieResource radarrMovie;

    /**
     * Creates a new Movie instance with the given client context and Radarr movie resource.
     *
     * @param ctx the client context
     * @param radarrMovie the Radarr movie resource
     */
    public Movie(ClientContext ctx, MovieResource radarrMovie) {
        super(ctx, radarrMovie);
        this.radarrMovie = radarrMovie;
    }

    /**
     * Returns the tags associated with this movie.
     *
     * @return an iterable of Tag objects
     */
    @Override
    public Iterable<Tag> tags() {
        return () -> {
            final Map<Integer, String> tags = ctx.getRadarrClient().getAllTags().stream()
                    .collect(Collectors.toMap(TagResource::getId, TagResource::getLabel));

            return radarrMovie.getTags().stream()
                    .map(tagId -> new Tag(ctx, tags.get(tagId)))
                    .iterator();
        };
    }

    /**
     * Returns the torrents associated with this movie.
     *
     * @return an iterable of Torrent objects
     */
    @Override
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
