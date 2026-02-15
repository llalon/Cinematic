package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.qbittorrent.dto.TorrentInfo;
import de.llalon.cinematic.client.sonarr.dto.QueueResource;
import de.llalon.cinematic.client.sonarr.dto.SeriesResource;
import de.llalon.cinematic.client.sonarr.dto.TagResource;
import de.llalon.cinematic.util.collections.OffsetPagedIterable;
import de.llalon.cinematic.util.collections.PagePagedIterable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.experimental.Delegate;

public class Series extends DomainModel {

    @Delegate
    private final SeriesResource sonarrSeries;

    Series(ClientContext ctx, SeriesResource seriesResource) {
        super(ctx);
        this.sonarrSeries = seriesResource;
    }

    public Iterable<Tag> tags() {
        return () -> {
            final Map<Integer, String> tags = ctx.getSonarrClient().getAllTags().stream()
                    .collect(Collectors.toMap(TagResource::getId, TagResource::getLabel));

            return sonarrSeries.getTags().stream()
                    .map(tagId -> new Tag(ctx, tags.get(tagId)))
                    .iterator();
        };
    }

    public Iterable<Torrent> torrents() {
        final Integer seriesId = sonarrSeries.getId();

        final PagePagedIterable<QueueResource> allQueuePaged = new PagePagedIterable<>((take, skip) ->
                ctx.getSonarrClient().getQueue(take, skip, false).getRecords());

        return () -> {
            final List<TorrentInfo> allTorrents = ctx.getQbittorrentClient().getTorrents();
            return allQueuePaged.stream()
                    .filter(queueResource -> queueResource.getSeriesId().equals(seriesId))
                    .flatMap(queueResource -> allTorrents.stream()
                            .filter(torrent -> torrent.getHash() != null && queueResource.getDownloadId() != null)
                            .filter(torrent -> torrent.getHash().equalsIgnoreCase(queueResource.getDownloadId()))
                            .map(torrent -> new Torrent(ctx, torrent)))
                    .iterator();
        };
    }

    public Iterable<Request> requests() {
        final Integer tmdbId = sonarrSeries.getTmdbId();

        return () -> new OffsetPagedIterable<>((take, skip) -> ctx.getOverseerrClient()
                        .getAllRequests(take, skip, null, null, null)
                        .getResults())
                .stream()
                        .filter(request -> request.getMedia() != null)
                        .filter(request -> tmdbId.equals(request.getMedia().getTmdbId()))
                        .map(x -> new Request(ctx, x))
                        .iterator();
    }

    public Iterable<Watches> watches() {
        final Integer tmdbId = sonarrSeries.getTmdbId();

        return new OffsetPagedIterable<>((take, skip) ->
                ctx.getTautulliClient().getHistoryByRatingKey(tmdbId.toString(), skip, take).getData().stream()
                        .map(h -> new Watches(ctx, h))
                        .toList());
    }
}
