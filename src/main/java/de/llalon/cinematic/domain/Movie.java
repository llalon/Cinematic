package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.qbittorrent.dto.TorrentInfo;
import de.llalon.cinematic.client.radarr.dto.MovieResource;
import de.llalon.cinematic.client.radarr.dto.QueueResource;
import de.llalon.cinematic.client.radarr.dto.TagResource;
import de.llalon.cinematic.util.collections.OffsetPagedIterable;
import de.llalon.cinematic.util.collections.PagePagedIterable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.experimental.Delegate;

public class Movie extends DomainModel {

    @Delegate
    private final MovieResource radarrMovie;

    public Movie(ClientContext ctx, MovieResource radarrMovie) {
        super(ctx);
        this.radarrMovie = radarrMovie;
    }

    public Iterable<Tag> tags() {
        return () -> {
            final Map<Integer, String> tags = ctx.getRadarrClient().getAllTags().stream()
                    .collect(Collectors.toMap(TagResource::getId, TagResource::getLabel));

            return radarrMovie.getTags().stream()
                    .map(tagId -> new Tag(ctx, tags.get(tagId)))
                    .iterator();
        };
    }

    public Iterable<Torrent> torrents() {
        final Integer movieId = radarrMovie.getId();

        final PagePagedIterable<QueueResource> allQueuePaged = new PagePagedIterable<>((take, skip) ->
                ctx.getRadarrClient().getQueue(take, skip, false).getRecords());

        return () -> {
            final List<TorrentInfo> allTorrents = ctx.getQbittorrentClient().getTorrents();
            return allQueuePaged.stream()
                    .filter(queueResource -> queueResource.getMovieId().equals(movieId))
                    .flatMap(queueResource -> allTorrents.stream()
                            .filter(torrent -> torrent.getHash() != null && queueResource.getDownloadId() != null)
                            .filter(torrent -> torrent.getHash().equalsIgnoreCase(queueResource.getDownloadId()))
                            .map(torrent -> new Torrent(ctx, torrent)))
                    .iterator();
        };
    }

    public Iterable<Request> requests() {
        final Integer tmdbId = radarrMovie.getTmdbId();

        return () -> new OffsetPagedIterable<>((take, skip) -> ctx.getOverseerrClient()
                        .getAllRequests(take, skip, null, null, null)
                        .getResults())
                .stream()
                        .filter(request -> request.getMedia() != null)
                        .filter(request -> tmdbId.equals(request.getMedia().getTmdbId()))
                        .map(x -> new Request(ctx, x))
                        .iterator();
    }
}
