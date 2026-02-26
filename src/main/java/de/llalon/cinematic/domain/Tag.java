package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.radarr.dto.RadarrTag;
import de.llalon.cinematic.client.sonarr.dto.SonarrTag;
import de.llalon.cinematic.util.collections.StreamUtils;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
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
     * Returns the movies associated with this tag.
     *
     * @return an iterable of Movie objects
     */
    @NotNull
    public Iterable<Movie> movies() {
        return () -> {
            final Set<Integer> tagIds = radarrTags()
                    .filter(t -> t.getLabel().equals(this.name))
                    .map(RadarrTag::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            if (tagIds.isEmpty()) {
                return StreamUtils.emptyIterator();
            }

            return radarrMovies()
                    .filter(radarrMovie -> radarrMovie.getTags().stream().anyMatch(tagIds::contains))
                    .map(radarrMovie -> new Movie(ctx, radarrMovie))
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
            final Set<Integer> tagIds = sonarrTags()
                    .filter(t -> t.getLabel().equals(this.name))
                    .map(SonarrTag::getId)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());

            if (tagIds.isEmpty()) {
                return StreamUtils.emptyIterator();
            }

            return sonarrSeries()
                    .filter(sonarrSeries -> sonarrSeries.getTags().stream().anyMatch(tagIds::contains))
                    .map(sonarrSeries -> new Series(ctx, sonarrSeries))
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
