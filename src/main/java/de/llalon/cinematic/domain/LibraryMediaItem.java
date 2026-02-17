package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.plex.dto.PlexMediaItem;
import de.llalon.cinematic.client.radarr.dto.MovieResource;
import de.llalon.cinematic.client.sonarr.dto.SeriesResource;
import de.llalon.cinematic.util.collections.OffsetPagedIterable;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public abstract class LibraryMediaItem extends DomainModel {

    //    @Getter
    //    @RequiredArgsConstructor
    //    protected enum LibraryIdType {
    //        TMDB("tmdb"),
    //        IMDB("imdb"),
    //        TVDB("tvdb");
    //
    //        protected final String value;
    //    }

    @Getter
    @RequiredArgsConstructor
    protected enum LibraryMediaType {
        SERIES("show", "2"),
        MOVIE("movie", "1");

        protected final String plexLibraryType;
        protected final String plexMediaType;
    }

    @Getter
    protected final String tmdbId;

    @Getter
    protected final String tvdbId;

    @Getter // Would be null for movies!
    protected final String imdbId;

    protected final LibraryMediaType libraryMediaType; // show or movie

    protected LibraryMediaItem(ClientContext ctx, MovieResource radarrMovie) {
        super(ctx);
        this.tmdbId = radarrMovie.getTmdbId() == null ? null : String.valueOf(radarrMovie.getTmdbId());
        this.imdbId = radarrMovie.getImdbId() == null ? null : String.valueOf(radarrMovie.getImdbId());
        this.tvdbId = null; // no TV ID for movies!
        this.libraryMediaType = LibraryMediaType.MOVIE;
    }

    protected LibraryMediaItem(ClientContext ctx, SeriesResource sonarrSeries) {
        super(ctx);
        this.tmdbId = sonarrSeries.getTmdbId() == null ? null : String.valueOf(sonarrSeries.getTmdbId());
        this.imdbId = sonarrSeries.getImdbId() == null ? null : String.valueOf(sonarrSeries.getImdbId());
        this.tvdbId = sonarrSeries.getTvdbId() == null ? null : String.valueOf(sonarrSeries.getTvdbId());
        this.libraryMediaType = LibraryMediaType.SERIES;
    }

    /**
     * Returns the tags associated with this media item.
     *
     * @return an iterable of Tag objects
     */
    public abstract Iterable<Tag> tags();

    /**
     * Returns the torrents associated with this media item.
     *
     * @return an iterable of Torrent objects
     */
    public abstract Iterable<Torrent> torrents();

    /**
     * Returns the requests associated with this media item.
     *
     * @return an iterable of Request objects
     */
    public Iterable<Request> requests() {
        return () -> new OffsetPagedIterable<>((take, skip) -> ctx.getOverseerrClient()
                        .getAllRequests(take, skip, null, null, null)
                        .getResults())
                .stream()
                        .filter(request -> request.getMedia() != null)
                        .filter(request -> {
                            if (this.tvdbId != null && this.tvdbId.equalsIgnoreCase(request.getTvdbId())) {
                                return true;
                            }

                            if (this.tvdbId != null
                                    && this.tvdbId.equalsIgnoreCase(
                                            String.valueOf(request.getMedia().getTvdbId()))) {
                                return true;
                            }

                            if (this.tmdbId != null
                                    && this.tmdbId.equalsIgnoreCase(
                                            String.valueOf(request.getMedia().getTmdbId()))) {
                                return true;
                            }

                            // ToDo: Try to match IMDB id too..

                            return false;
                        })
                        .map(x -> new Request(ctx, x))
                        .iterator();
    }

    /**
     * Returns the watch history for this media item.
     *
     * @return an iterable of Watches objects
     */
    public Iterable<Watches> watches() {
        return () -> fetchPlexMediaItem()
                .map(PlexMediaItem::getRatingKey)
                .map(ratingKey -> new OffsetPagedIterable<>((take, skip) ->
                                ctx.getTautulliClient().getHistoryByRatingKey(ratingKey, skip, take).getData().stream()
                                        .map(history -> new Watches(ctx, history))
                                        .collect(Collectors.toList()))
                        .iterator())
                .orElse(Collections.emptyIterator());
    }

    protected Optional<PlexMediaItem> fetchPlexMediaItem() {
        return ctx.getPlexClient().getSections().getMediaContainer().getDirectories().stream()
                .filter(section -> libraryMediaType.getPlexLibraryType().equalsIgnoreCase(section.getType()))
                .map(section ->
                        ctx.getPlexClient().getSection(section.getKey(), libraryMediaType.getPlexMediaType(), true))
                .filter(section -> section.getMediaContainer() != null
                        && section.getMediaContainer().getMetadata() != null)
                .flatMap(section -> section.getMediaContainer().getMetadata().stream())
                .filter(series -> series.getGuids() != null)
                .filter(series -> series.getGuids().stream().anyMatch(guid -> {
                    if (guid.getId() == null) {
                        return false;
                    }
                    String[] parts = guid.getId().split("://");
                    if (parts.length != 2) {
                        return false;
                    }
                    String prefix = parts[0];
                    String id = parts[1];

                    return plexMatchesId(prefix, id);
                }))
                .findFirst();
    }

    protected boolean plexMatchesId(String prefix, String id) {
        switch (prefix) {
            case "tmdb":
                return id.equalsIgnoreCase(String.valueOf(this.tmdbId));
            case "imdb":
                return id.equalsIgnoreCase(this.imdbId);
            case "tvdb":
                // could or could not have "tt" prefix
                if (id.equalsIgnoreCase(this.tvdbId)) {
                    return true;
                }

                if (id.equalsIgnoreCase("tt" + this.tvdbId)) {
                    return true;
                }

                if (("tt" + id).equalsIgnoreCase(this.tvdbId)) {
                    return true;
                }
            default:
                return false;
        }
    }
}
