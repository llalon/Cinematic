package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.radarr.dto.MovieResource;
import de.llalon.cinematic.client.radarr.dto.TagResource;
import java.util.Map;
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
}
