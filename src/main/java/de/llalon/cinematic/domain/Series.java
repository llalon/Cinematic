package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.sonarr.dto.SeriesResource;
import de.llalon.cinematic.client.sonarr.dto.TagResource;
import de.llalon.cinematic.util.collections.OffsetPagedIterable;
import java.util.Map;
import java.util.Objects;
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

    public Iterable<Request> requests() {
        final Integer tvdbId = sonarrSeries.getTvdbId();
        final Integer tmdbId = sonarrSeries.getTmdbId();

        return () -> new OffsetPagedIterable<>((take, skip) -> ctx.getOverseerrClient()
                        .getAllRequests(take, skip, null, null, null)
                        .getResults())
                .stream()
                        .filter(r -> r.getMedia() != null
                                && ((tvdbId != null
                                                && Objects.equals(r.getMedia().getTvdbId(), tvdbId))
                                        || (tmdbId != null
                                                && Objects.equals(r.getMedia().getTmdbId(), tmdbId))))
                        .map(x -> new Request(ctx, x))
                        .iterator();
    }
}
