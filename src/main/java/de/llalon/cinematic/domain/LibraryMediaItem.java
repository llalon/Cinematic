package de.llalon.cinematic.domain;

import static de.llalon.cinematic.util.collections.StreamUtils.streamIterator;

import de.llalon.cinematic.client.plex.dto.PlexMediaItem;
import de.llalon.cinematic.client.radarr.dto.MovieResource;
import de.llalon.cinematic.client.sonarr.dto.SeriesResource;
import de.llalon.cinematic.client.tautulli.dto.History;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

abstract class LibraryMediaItem extends DomainModel {

    /**
     * Supported external identifier schemes used by Plex GUIDs.
     */
    @Getter
    @RequiredArgsConstructor
    protected enum LibraryIdType {
        TMDB("tmdb"),
        IMDB("imdb"),
        TVDB("tvdb"),
        MBID("mbid");

        protected final String value;
    }

    /**
     * Supported media categories and their Plex API identifiers.
     */
    @Getter
    @RequiredArgsConstructor
    protected enum LibraryMediaType {
        SERIES("show", "2", "show"),
        MOVIE("movie", "1", "movie"),
        ARTIST("artist", "8", "artist"),
        ALBUM("artist", "9", "album"),
        TRACK("artist", "10", "track");

        protected final String plexLibraryType;
        protected final String plexMediaType;
        protected final String plexItemType;
    }

    /** TMDB identifier for this media item, when known. */
    @Getter
    @Nullable
    protected final String tmdbId;

    /** TVDB identifier for this media item, when known. */
    @Getter // Would be null for movies!
    @Nullable
    protected final String tvdbId;

    /** IMDB identifier for this media item, when known. */
    @Getter
    @Nullable
    protected final String imdbId;

    /** Media type used to query matching Plex library sections. */
    protected final LibraryMediaType libraryMediaType;

    protected LibraryMediaItem(@NonNull ClientContext ctx, @NonNull MovieResource radarrMovie) {
        super(ctx);
        this.tmdbId = radarrMovie.getTmdbId() == null ? null : String.valueOf(radarrMovie.getTmdbId());
        this.imdbId = radarrMovie.getImdbId() == null ? null : String.valueOf(radarrMovie.getImdbId());
        this.tvdbId = null; // no TV ID for movies!
        this.libraryMediaType = LibraryMediaType.MOVIE;
    }

    protected LibraryMediaItem(@NonNull ClientContext ctx, @NonNull SeriesResource sonarrSeries) {
        super(ctx);
        this.tmdbId = sonarrSeries.getTmdbId() == null ? null : String.valueOf(sonarrSeries.getTmdbId());
        this.imdbId = sonarrSeries.getImdbId() == null ? null : String.valueOf(sonarrSeries.getImdbId());
        this.tvdbId = sonarrSeries.getTvdbId() == null ? null : String.valueOf(sonarrSeries.getTvdbId());
        this.libraryMediaType = LibraryMediaType.SERIES;
    }

    /** Creates a media item whose external identifiers are supplied by its subtype. */
    protected LibraryMediaItem(@NonNull ClientContext ctx, @NonNull LibraryMediaType type) {
        super(ctx);
        this.tmdbId = null;
        this.tvdbId = null;
        this.imdbId = null;
        this.libraryMediaType = type;
    }

    /** Returns the MusicBrainz identifiers supplied by music subtypes. */
    protected Stream<String> musicBrainzIds() {
        return Stream.empty();
    }

    /**
     * Resolves this item's server-local Plex rating key from its external identifiers.
     * @return the rating key, or null when unmatched (including ambiguous music matches)
     * @throws ClientNotConfiguredException if matching requires an unconfigured Plex client
     */
    @Nullable
    public String getPlexRatingKey() {
        return fetchPlexMediaItem().map(PlexMediaItem::getRatingKey).orElse(null);
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
    @NonNull
    public abstract Iterable<Tag> tags();

    /**
     * Returns the torrents associated with this media item.
     *
     * @return an iterable of Torrent objects
     */
    @NonNull
    public abstract Iterable<Torrent> torrents();

    /**
     * Returns the requests associated with this media item.
     *
     * @return an iterable of Request objects
     */
    @NonNull
    public Iterable<Request> requests() {
        return Collections.emptyList();
    }

    /** Returns movie and TV requests matched by this item's external identifiers. */
    @NonNull
    protected Iterable<Request> seerrMediaRequests() {
        return () -> seerrRequests()
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
     * Returns playback history for this media item, including music listening history.
     *
     * @return an iterable of Watches objects
     */
    @NonNull
    public Iterable<Watches> watches() {
        return () -> fetchPlexMediaItem()
                .map(PlexMediaItem::getRatingKey)
                .map(ratingKey -> playbackHistory(ratingKey)
                        .map(history -> new Watches(ctx, history))
                        .iterator())
                .orElse(Collections.emptyIterator());
    }

    /** Loads playback history for this item; containers override the hierarchy level. */
    protected Stream<History> playbackHistory(String ratingKey) {
        return tautulliHistoryByRatingKey(ratingKey);
    }

    /**
     * Finds the matching Plex library item for this media item.
     *
     * @return matching Plex media item, or an empty optional when no match is found
     */
    @NonNull
    protected Optional<PlexMediaItem> fetchPlexMediaItem() {
        return selectPlexMatch(plexCandidates().filter(this::hasMatchingId));
    }

    /** Supplies candidates for this item's Plex identity. */
    protected Stream<PlexMediaItem> plexCandidates() {
        return plexLibraryItems();
    }

    /** Selects a matching item; subtypes can reject ambiguous matches. */
    protected Optional<PlexMediaItem> selectPlexMatch(Stream<PlexMediaItem> matches) {
        return matches.findFirst();
    }

    private Stream<PlexMediaItem> plexLibraryItems() {
        return plexSections().collect(Collectors.toList()).stream()
                .filter(section -> libraryMediaType.getPlexLibraryType().equalsIgnoreCase(section.getType()))
                .map(section -> plexSection(section.getKey(), libraryMediaType.getPlexMediaType()))
                .filter(section -> section.getMediaContainer() != null
                        && section.getMediaContainer().getMetadata() != null)
                .flatMap(section -> section.getMediaContainer().getMetadata().stream())
                .filter(Objects::nonNull);
    }

    /** Supplies candidates with full external metadata, skipping absent identifiers. */
    protected Stream<PlexMediaItem> plexCandidatesWithExternalIds(Stream<String> ids) {
        return plexCandidatesWithExternalIds(ids, item -> true);
    }

    /** Supplies external-ID candidates restricted to a subtype's parent or other scope. */
    protected Stream<PlexMediaItem> plexCandidatesWithExternalIds(Stream<String> ids, Predicate<PlexMediaItem> scope) {
        if (ids.noneMatch(id -> id != null && !id.isBlank())) {
            return Stream.empty();
        }
        return plexLibraryItems()
                .filter(item -> libraryMediaType.getPlexItemType().equalsIgnoreCase(item.getType()))
                .filter(scope)
                .map(this::withExternalIds);
    }

    /** Returns a match only when every matching candidate has the same Plex rating key. */
    protected Optional<PlexMediaItem> uniquePlexMatch(Stream<PlexMediaItem> matches) {
        List<PlexMediaItem> unique = matches
                .filter(item -> item.getRatingKey() != null)
                .collect(Collectors.toMap(PlexMediaItem::getRatingKey, item -> item, (first, duplicate) -> first))
                .values()
                .stream()
                .limit(2)
                .collect(Collectors.toList());
        return unique.size() == 1 ? Optional.of(unique.get(0)) : Optional.empty();
    }

    private PlexMediaItem withExternalIds(PlexMediaItem item) {
        if (item.getRatingKey() == null
                || isMusicBrainzGuid(item.getGuid())
                || (item.getGuids() != null && !item.getGuids().isEmpty())) {
            return item;
        }
        // Listings can omit external GUIDs; full item metadata may contain them.
        var metadata = plexMetadata(item.getRatingKey()).getMediaContainer();
        if (metadata == null || metadata.getMetadata() == null) {
            return item;
        }
        return metadata.getMetadata().stream()
                .filter(Objects::nonNull)
                .filter(full -> item.getRatingKey().equals(full.getRatingKey()))
                .filter(full -> libraryMediaType.getPlexItemType().equalsIgnoreCase(full.getType()))
                .findFirst()
                .orElse(item);
    }

    private boolean hasMatchingId(PlexMediaItem item) {
        Stream<String> guids = item.getGuids() == null
                ? Stream.empty()
                : item.getGuids().stream().filter(Objects::nonNull).map(guid -> guid.getId());
        return Stream.concat(Stream.of(item.getGuid()), guids)
                .filter(Objects::nonNull)
                .anyMatch(guid -> {
                    String[] parts = guid.split("://", 2);
                    if (parts.length != 2) {
                        return false;
                    }
                    try {
                        LibraryIdType type = LibraryIdType.valueOf(parts[0].toUpperCase(java.util.Locale.ROOT));
                        return plexMatchesId(type, parts[1]);
                    } catch (IllegalArgumentException e) {
                        return false;
                    }
                });
    }

    private static boolean isMusicBrainzGuid(String guid) {
        return guid != null && guid.regionMatches(true, 0, "mbid://", 0, "mbid://".length());
    }

    /**
     * Checks whether the given Plex GUID prefix and ID match one of this media item's external identifiers.
     *
     * @param prefix the GUID scheme (e.g. {@code "tmdb"}, {@code "imdb"}, {@code "tvdb"}, {@code "mbid"})
     * @param id     the identifier value from the Plex GUID
     * @return {@code true} if the identifier matches this media item
     */
    protected boolean plexMatchesId(@NonNull LibraryIdType prefix, @NonNull String id) {
        switch (prefix) {
            case TMDB:
                return this.tmdbId != null && id.equalsIgnoreCase(this.tmdbId);
            case IMDB:
                return id.equalsIgnoreCase(this.imdbId);
            case MBID:
                return musicBrainzIds()
                        .filter(Objects::nonNull)
                        .filter(candidate -> !candidate.isBlank())
                        .anyMatch(id::equalsIgnoreCase);
            case TVDB:
                if (this.tvdbId == null) {
                    return false;
                }
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
