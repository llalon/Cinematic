package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.tautulli.dto.History;
import lombok.experimental.Delegate;
import org.jetbrains.annotations.NotNull;

public class Watches extends DomainModel {

    @Delegate
    @NotNull
    private final History tautulliHistory;

    Watches(@NotNull ClientContext ctx, @NotNull History tautulliHistory) {
        super(ctx);
        this.tautulliHistory = tautulliHistory;
    }

    @NotNull
    public User user() {
        return new User(ctx, tautulliUserById(this.tautulliHistory.getUserId()));
    }
}
