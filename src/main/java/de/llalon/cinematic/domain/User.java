package de.llalon.cinematic.domain;

import de.llalon.cinematic.util.collections.OffsetPagedIterable;
import java.util.Optional;
import lombok.Getter;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class User extends DomainModel {

    @Getter
    private final String email;

    @Nullable // ToDo: Lazy load if Plex user is provided. Also possible to be null if the overseer user doesn't exist.
    @Delegate
    private final de.llalon.cinematic.client.overseerr.dto.User overseerrUser;

    User(@NotNull ClientContext ctx, @NotNull de.llalon.cinematic.client.overseerr.dto.User overseerrUser) {
        super(ctx);
        this.email = overseerrUser.getEmail();
        this.overseerrUser = overseerrUser;
    }

    User(@NotNull ClientContext ctx, @NotNull String email) {
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

    /**
     * Returns the media requests made by this user in Overseerr.
     *
     * @return an iterable of Request objects
     */
    @NotNull
    public Iterable<Request> requests() {
        return () -> {
            final Integer userId = this.getOverseerrUserId().get();
            return new OffsetPagedIterable<>((take, skip) -> ctx.getOverseerrClient()
                            .getAllRequests(take, skip, null, null, userId)
                            .getResults())
                    .stream().map(request -> new Request(ctx, request)).iterator();
        };
    }

    /**
     * Returns the watch history for this user.
     *
     * @return an iterable of Watches objects
     */
    @NotNull
    public Iterable<Watches> watches() {
        throw new RuntimeException("Not implemented yet!");
    }
}
