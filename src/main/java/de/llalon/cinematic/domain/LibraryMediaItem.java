package de.llalon.cinematic.domain;

import static de.llalon.cinematic.util.collections.StreamUtils.streamIterator;

import de.llalon.cinematic.client.plex.dto.PlexMediaItem;
import de.llalon.cinematic.client.radarr.dto.MovieResource;
import de.llalon.cinematic.client.sonarr.dto.SeriesResource;
import java.util.Collections;
import java.util.Optional;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Slf4j
abstract class LibraryMediaItem extends DomainModel {

    @Getter
    @RequiredArgsConstructor
    protected enum LibraryIdType {
        TMDB("tmdb"),
        IMDB("imdb"),
        TVDB("tvdb");

        protected final String value;
    }

    @Getter
    @RequiredArgsConstructor
    protected enum LibraryMediaType {
        SERIES("show", "2"),
        MOVIE("movie", "1");

        protected final String plexLibraryType;
        protected final String plexMediaType;
    }

    @Getter
    @Nullable
    protected final String tmdbId;

    @Getter // Would be null for movies!
    @Nullable
    protected final String tvdbId;

    @Getter
    @Nullable
    protected final String imdbId;

    protected final LibraryMediaType libraryMediaType; // show or movie

    protected LibraryMediaItem(@NotNull ClientContext ctx, @NotNull MovieResource radarrMovie) {
        super(ctx);
        this.tmdbId = radarrMovie.getTmdbId() == null ? null : String.valueOf(radarrMovie.getTmdbId());
        this.imdbId = radarrMovie.getImdbId() == null ? null : String.valueOf(radarrMovie.getImdbId());
        this.tvdbId = null; // no TV ID for movies!
        this.libraryMediaType = LibraryMediaType.MOVIE;
    }

    protected LibraryMediaItem(@NotNull ClientContext ctx, @NotNull SeriesResource sonarrSeries) {
        super(ctx);
        this.tmdbId = sonarrSeries.getTmdbId() == null ? null : String.valueOf(sonarrSeries.getTmdbId());
        this.imdbId = sonarrSeries.getImdbId() == null ? null : String.valueOf(sonarrSeries.getImdbId());
        this.tvdbId = sonarrSeries.getTvdbId() == null ? null : String.valueOf(sonarrSeries.getTvdbId());
        this.libraryMediaType = LibraryMediaType.SERIES;
    }

    /**
     * @param tag tag object to check
     * @return true if item has the given tag
     */
    public boolean hasTag(Tag tag) {
        return this.hasTag(tag.getName());
    }

    /**
     * @param tag tag name of check
     * @return true if item has the given tag name
     */
    public boolean hasTag(@Nullable String tag) {
        return streamIterator(this.tags()).anyMatch(t -> t.getName().equals(tag));
    }

    /**
     * Returns the tags associated with this media item.
     *
     * @return an iterable of Tag objects
     */
    @NotNull
    public abstract Iterable<Tag> tags();

    /**
     * Returns the torrents associated with this media item.
     *
     * @return an iterable of Torrent objects
     */
    @NotNull
    public abstract Iterable<Torrent> torrents();

    /**
     * Returns the requests associated with this media item.
     *
     * @return an iterable of Request objects
     */
    @NotNull
    public Iterable<Request> requests() {
        return () -> overseerrRequests()
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
    @NotNull
    public Iterable<Watches> watches() {
        return () -> fetchPlexMediaItem()
                .map(PlexMediaItem::getRatingKey)
                .map(ratingKey -> tautulliHistoryByRatingKey(ratingKey)
                        .map(history -> new Watches(ctx, history))
                        .iterator())
                .orElse(Collections.emptyIterator());
    }

    @NotNull
    protected Optional<PlexMediaItem> fetchPlexMediaItem() {
        return plexSections()
                .filter(section -> libraryMediaType.getPlexLibraryType().equalsIgnoreCase(section.getType()))
                .map(section -> plexSection(section.getKey(), libraryMediaType.getPlexMediaType()))
                .filter(section -> section.getMediaContainer() != null
                        && section.getMediaContainer().getMetadata() != null)
                .flatMap(section -> section.getMediaContainer().getMetadata().stream())
                .filter(series -> series.getGuids() != null)
                .filter(series -> series.getGuids().stream().anyMatch(guid -> {
                    if (guid.getId() == null) {
                        return false;
                    }

                    try {
                        String[] parts = guid.getId().split("://");
                        if (parts.length != 2) {
                            return false;
                        }
                        String prefix = parts[0];
                        String id = parts[1];

                        LibraryIdType type = LibraryIdType.valueOf(prefix.toUpperCase());
                        return plexMatchesId(type, id);
                    } catch (IllegalArgumentException | NullPointerException e) {
                        log.warn("Unknown Plex id type: {}", guid.getId());
                        return false;
                    }
                }))
                .findFirst();
    }

    /**
     * Checks whether the given Plex GUID prefix and ID match one of this media item's external identifiers.
     *
     * @param prefix the GUID scheme (e.g. {@code "tmdb"}, {@code "imdb"}, {@code "tvdb"})
     * @param id     the identifier value from the Plex GUID
     * @return {@code true} if the identifier matches this media item
     */
    protected boolean plexMatchesId(@NotNull LibraryIdType prefix, @NotNull String id) {
        switch (prefix) {
            case TMDB:
                return id.equalsIgnoreCase(String.valueOf(this.tmdbId));
            case IMDB:
                return id.equalsIgnoreCase(this.imdbId);
            case TVDB:
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
