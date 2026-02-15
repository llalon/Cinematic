package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.sonarr.dto.SeriesResource;
import de.llalon.cinematic.client.sonarr.dto.TagResource;
import java.util.Map;
import java.util.stream.Collectors;

public class Series extends DomainModel {

    private final SeriesResource sonarrSeries;

    Series(ClientContext ctx, SeriesResource seriesResource) {
        super(ctx);
        this.sonarrSeries = seriesResource;
    }

    public Iterable<Tag> tags() {
        return () -> {
            final Map<Integer, String> tags = ctx.getSonarrClient().getAllTags().stream()
                    .collect(Collectors.toMap(TagResource::getId, TagResource::getLabel));

            return sonarrSeries.getTags().stream()
                    .map(tagId -> new Tag(ctx, tags.get(tagId)))
                    .iterator();
        };
    }
}
