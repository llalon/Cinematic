package de.llalon.cinematic.domain;

import de.llalon.cinematic.util.collections.OffsetPagedIterable;

public class User extends DomainModel {

    private final de.llalon.cinematic.client.overseerr.dto.User overseerrUser;

    User(ClientContext ctx, de.llalon.cinematic.client.overseerr.dto.User overseerrUser) {
        super(ctx);
        this.overseerrUser = overseerrUser;
    }

    public Iterable<Request> requests() {
        return () -> new OffsetPagedIterable<>((take, skip) -> ctx.getOverseerrClient()
                        .getAllRequests(take, skip, null, null, this.overseerrUser.getId())
                        .getResults())
                .stream().map(request -> new Request(ctx, request)).iterator();
    }
}
