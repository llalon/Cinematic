package de.llalon.cinematic.domain;

import de.llalon.cinematic.util.collections.OffsetPagedIterable;
import lombok.Getter;

public class User extends DomainModel {

    @Getter
    private final String email;

    User(ClientContext ctx, de.llalon.cinematic.client.overseerr.dto.User overseerrUser) {
        super(ctx);
        this.email = overseerrUser.getEmail();
    }

    User(ClientContext ctx, String email) {
        super(ctx);
        this.email = email;
    }

    private Integer getOverseerrUserId() {
        // ToDo
        return null;
    }

    public Iterable<Request> requests() {
        return () -> {
            final Integer userId = this.getOverseerrUserId();
            return new OffsetPagedIterable<>((take, skip) -> ctx.getOverseerrClient()
                            .getAllRequests(take, skip, null, null, userId)
                            .getResults())
                    .stream().map(request -> new Request(ctx, request)).iterator();
        };
    }
}
