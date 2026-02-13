package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.radarr.dto.MovieResource;
import de.llalon.cinematic.client.radarr.dto.TagResource;
import java.util.Map;
import java.util.stream.Collectors;

public class Movie {

    private final ClientContext context;
    private final MovieResource radarrMovie;

    public Movie(ClientContext context, MovieResource radarrMovie) {
        this.context = context;
        this.radarrMovie = radarrMovie;
    }

    public Iterable<Tag> tags() {
        final Map<Integer, String> tags = context.getRadarrClient().getAllTags().stream()
                .collect(Collectors.toMap(TagResource::getId, TagResource::getLabel));

        return radarrMovie.getTags().stream()
                .map(tagId -> new Tag(context, tags.get(tagId)))
                .toList();
    }
}
