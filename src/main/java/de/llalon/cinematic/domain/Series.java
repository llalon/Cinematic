package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.plex.dto.PlexMediaItem;
import de.llalon.cinematic.client.sonarr.dto.SeriesResource;
import de.llalon.cinematic.client.sonarr.dto.SonarrTag;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

/**
 * Domain representation of a TV series sourced from Sonarr.
 *
 * <p>Provides navigation to associated {@link Tag}s, {@link Torrent}s, {@link Request}s,
 * and watch history ({@link Watches}) for the series. All collection relations are lazy
 * and returned as {@link Iterable}.</p>
 */
@Slf4j
public class Series extends LibraryMediaItem {

    @NotNull
    private final SeriesResource sonarrSeries;

    /**
     * Creates a new Series instance with the given client context and Sonarr series resource.
     *
     * @param ctx            the client context
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
            final Map<Integer, String> tags =
                    sonarrTags().collect(Collectors.toMap(SonarrTag::getId, SonarrTag::getLabel));

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
        return () -> sonarrQueue()
                .filter(queueResource -> queueResource.getSeriesId().equals(sonarrSeries.getId()))
                .flatMap(queueResource -> super.qbittorrentTorrents()
                        .filter(torrent -> torrent.getHash() != null && queueResource.getDownloadId() != null)
                        .filter(torrent -> torrent.getHash().equalsIgnoreCase(queueResource.getDownloadId()))
                        .map(torrent -> new Torrent(ctx, torrent)))
                .iterator();
    }

    /**
     * Returns the title of this series.
     *
     * @return the series title
     */
    public String getTitle() {
        return this.sonarrSeries.getTitle();
    }

    /**
     * Returns the year this series premiered.
     *
     * @return the premiere year
     */
    public Integer getYear() {
        return this.sonarrSeries.getYear();
    }

    /**
     * Returns the Sonarr status of this series (e.g. {@code continuing}, {@code ended}).
     *
     * @return the series status
     */
    public String getStatus() {
        return this.sonarrSeries.getStatus();
    }

    /**
     * Returns the media versions for all episodes in this series.
     *
     * <p>Traverses seasons and episodes via Plex children to collect all episode
     * media versions.</p>
     *
     * @return an iterable of {@link MediaVersion} objects; empty if the series is not in Plex
     */
    @Override
    @NotNull
    public Iterable<MediaVersion> mediaVersions() {
        return () -> fetchPlexMediaItem()
                .map(PlexMediaItem::getRatingKey)
                .map(ratingKey -> super.plexChildren(ratingKey).getMediaContainer().getMetadata().stream()
                        .filter(season -> season.getRatingKey() != null)
                        .flatMap(season ->
                                super.plexChildren(season.getRatingKey()).getMediaContainer().getMetadata().stream()
                                        .filter(episode -> episode.getMedia() != null)
                                        .flatMap(episode -> episode.getMedia().stream()
                                                .map(media -> new MediaVersion(ctx, media, episode.getRatingKey()))))
                        .iterator())
                .orElse(Collections.emptyIterator());
    }
}
