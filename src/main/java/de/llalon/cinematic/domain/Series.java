package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.qbittorrent.dto.TorrentInfo;
import de.llalon.cinematic.client.sonarr.dto.SeriesResource;
import de.llalon.cinematic.client.sonarr.dto.TagResource;
import de.llalon.cinematic.util.collections.PagePagedIterable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.experimental.Delegate;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class Series extends LibraryMediaItem {

    @Delegate
    @NotNull
    private final SeriesResource sonarrSeries;

    public String getTvdbId() {
        if (this.sonarrSeries.getTvdbId() == null) {
            return null;
        }
        return this.sonarrSeries.getTvdbId().toString();
    }

    public String getTmdbId() {
        if (this.sonarrSeries.getTmdbId() == null) {
            return null;
        }
        return this.sonarrSeries.getTmdbId().toString();
    }

    /**
     * Creates a new Series instance with the given client context and Sonarr series resource.
     *
     * @param ctx the client context
     * @param seriesResource the Sonarr series resource
     */
    Series(@NotNull ClientContext ctx, @NotNull SeriesResource seriesResource) {
        super(ctx, seriesResource);
        this.sonarrSeries = seriesResource;
    }

    /**
     * Returns the tags associated with this series.
     *
     * @return an iterable of Tag objects
     */
    @Override
    @NotNull
    public Iterable<Tag> tags() {
        return () -> {
            final Map<Integer, String> tags = ctx.getSonarrClient().getAllTags().stream()
                    .collect(Collectors.toMap(TagResource::getId, TagResource::getLabel));

            return sonarrSeries.getTags().stream()
                    .map(tagId -> {
                        if (tags.containsKey(tagId)) {
                            return new Tag(ctx, tags.get(tagId));
                        } else {
                            log.warn("Tag with id {} not found in Sonarr", tagId);
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .iterator();
        };
    }

    /**
     * Returns the torrents associated with this series.
     *
     * @return an iterable of Torrent objects
     */
    @Override
    @NotNull
    public Iterable<Torrent> torrents() {
        return () -> {
            final List<TorrentInfo> allTorrents = ctx.getQbittorrentClient().getTorrents();
            return new PagePagedIterable<>((take, skip) ->
                            ctx.getSonarrClient().getQueue(take, skip, false).getRecords())
                    .stream()
                            .filter(queueResource -> queueResource.getSeriesId().equals(sonarrSeries.getId()))
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
