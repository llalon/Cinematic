package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.qbittorrent.dto.TorrentInfo;
import de.llalon.cinematic.client.radarr.dto.MovieResource;
import de.llalon.cinematic.client.radarr.dto.QueueResource;
import de.llalon.cinematic.client.radarr.dto.TagResource;
import de.llalon.cinematic.util.collections.PagePagedIterable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class Movie extends DomainModel {

    private final MovieResource radarrMovie;

    public Movie(ClientContext ctx, MovieResource radarrMovie) {
        super(ctx);
        this.radarrMovie = radarrMovie;
    }

    public Iterable<Tag> tags() {
        final Map<Integer, String> tags = ctx.getRadarrClient().getAllTags().stream()
                .collect(Collectors.toMap(TagResource::getId, TagResource::getLabel));

        return radarrMovie.getTags().stream()
                .map(tagId -> new Tag(ctx, tags.get(tagId)))
                .toList();
    }

    public Iterable<Torrent> torrents() {
        Integer movieId = radarrMovie.getId();

        PagePagedIterable<QueueResource> allQueuePaged = new PagePagedIterable<>((take, skip) ->
                ctx.getRadarrClient().getQueue(take, skip, false).getRecords());

        List<TorrentInfo> allTorrents = ctx.getQbittorrentClient().getTorrents();
        List<QueueResource> allQueue = allQueuePaged.toList();

        List<Torrent> torrents = new ArrayList<>();

        allQueue.forEach(queueResource -> {
            if (queueResource.getMovieId().equals(movieId)) {
                allTorrents.forEach(torrent -> {
                    if (Objects.equals(torrent.getHash(), queueResource.getDownloadId())) {
                        torrents.add(new Torrent(ctx, torrent));
                    }
                });
            }
        });

        return torrents;
    }
}
