package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.plex.dto.PlexMediaContainerWrapper;
import de.llalon.cinematic.client.plex.dto.PlexMediaItem;
import de.llalon.cinematic.client.plex.dto.PlexSectionsContainer;
import de.llalon.cinematic.client.radarr.dto.MovieResource;
import de.llalon.cinematic.client.sonarr.dto.SeriesResource;
import de.llalon.cinematic.util.collections.OffsetPagedIterable;
import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

public abstract class LibraryMediaItem extends DomainModel {

    @Getter
    @RequiredArgsConstructor
    protected enum LibraryMediaType {
        SERIES("show", "2"),
        MOVIE("movie", "1");

        private final String plexLibraryType;
        protected final String plexMediaType;
    }

    private final String tmdbId;
    private final String tvdbId; // Would be null for movies!
    private final String imdbId;
    private final LibraryMediaType libraryMediaType; // show or movie

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

    public abstract Iterable<Tag> tags();

    public abstract Iterable<Torrent> torrents();

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

    public Iterable<Watches> watches() {
        return () -> fetchPlexMediaItem()
                .map(PlexMediaItem::getRatingKey)
                .map(s -> new OffsetPagedIterable<>((take, skip) ->
                                ctx.getTautulliClient().getHistoryByRatingKey(s, skip, take).getData().stream()
                                        .map(h -> new Watches(ctx, h))
                                        .collect(Collectors.toList()))
                        .iterator())
                .orElse(Collections.emptyIterator());
    }

    protected Optional<PlexMediaItem> fetchPlexMediaItem() {
        final PlexMediaContainerWrapper<PlexSectionsContainer> sections =
                ctx.getPlexClient().getSections();
        return sections.getMediaContainer().getDirectories().stream()
                .filter(section -> libraryMediaType.getPlexLibraryType().equalsIgnoreCase(section.getType()))
                .map(section ->
                        ctx.getPlexClient().getSection(section.getKey(), libraryMediaType.getPlexMediaType(), true))
                .flatMap(sectionData -> sectionData.getMediaContainer().getMetadata().stream())
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
