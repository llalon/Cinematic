package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.radarr.dto.TagResource;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Tag extends DomainModel {

    @Getter
    @NotNull
    private final String name;

    /**
     * Creates a new Tag instance with the given client context and name.
     *
     * @param context the client context
     * @param name the tag name
     */
    Tag(@NotNull ClientContext context, @NotNull String name) {
        super(context);
        this.name = name;
    }

    /**
     * Returns the name of this tag.
     *
     * @return the tag name
     */
    @Nullable
    public String name() {
        return name;
    }

    /**
     * Returns the movies associated with this tag.
     *
     * @return an iterable of Movie objects
     */
    @NotNull
    public Iterable<Movie> movies() {
        return () -> {
            // find radarr tag id for this label then lazily filter movies
            final Integer tagId = ctx.getRadarrClient().getAllTags().stream()
                    .filter(t -> t.getLabel().equals(this.name))
                    .findFirst()
                    .map(TagResource::getId)
                    .orElse(null);

            return ctx.getRadarrClient().getAllMovies().stream()
                    .filter(m -> tagId == null || m.getTags().contains(tagId))
                    .map(x -> new Movie(ctx, x))
                    .iterator();
        };
    }

    /**
     * Returns the series associated with this tag.
     *
     * @return an iterable of Series objects
     */
    @NotNull
    public Iterable<Series> series() {
        return () -> {
            final Integer tagId = ctx.getSonarrClient().getAllTags().stream()
                    .filter(t -> t.getLabel().equals(this.name))
                    .findFirst()
                    .map(de.llalon.cinematic.client.sonarr.dto.TagResource::getId)
                    .orElse(null);

            return ctx.getSonarrClient().getAllSeries().stream()
                    .filter(s -> tagId == null || s.getTags().contains(tagId))
                    .map(x -> new Series(ctx, x))
                    .iterator();
        };
    }

    /**
     * Returns the torrents associated with this tag.
     *
     * @return an iterable of Torrent objects
     */
    @NotNull
    public Iterable<Torrent> torrents() {
        return () -> ctx.getQbittorrentClient().getTorrents(null, null, this.name).stream()
                .map(x -> new Torrent(ctx, x))
                .iterator();
    }
}
