package de.llalon.cinematic.domain;

import de.llalon.cinematic.util.collections.OffsetPagedIterable;
import java.util.Optional;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

public class User extends DomainModel {

    @Getter
    private final String email;

    @Nullable
    private final de.llalon.cinematic.client.overseerr.dto.User overseerrUser;

    User(ClientContext ctx, de.llalon.cinematic.client.overseerr.dto.User overseerrUser) {
        super(ctx);
        this.email = overseerrUser.getEmail();
        this.overseerrUser = overseerrUser;
    }

    User(ClientContext ctx, String email) {
        super(ctx);
        this.email = email;
        this.overseerrUser = null;
    }

    private Optional<Integer> getOverseerrUserId() {
        if (Optional.ofNullable(overseerrUser).isPresent()) {
            return Optional.ofNullable(overseerrUser.getId());
        }
        return new OffsetPagedIterable<>((take, skip) ->
                        ctx.getOverseerrClient().getAllUsers(take, skip, null).getResults())
                .stream()
                        .filter(u -> this.email.equalsIgnoreCase(u.getEmail()))
                        .map(de.llalon.cinematic.client.overseerr.dto.User::getId)
                        .findAny();
    }

    public Iterable<Request> requests() {
        return () -> {
            final Integer userId = this.getOverseerrUserId().get();
            return new OffsetPagedIterable<>((take, skip) -> ctx.getOverseerrClient()
                            .getAllRequests(take, skip, null, null, userId)
                            .getResults())
                    .stream().map(request -> new Request(ctx, request)).iterator();
        };
    }

    public Iterable<Watches> watches() {
        // ToDo
        return null;
    }
}
