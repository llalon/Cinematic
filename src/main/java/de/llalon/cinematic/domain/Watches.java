package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.tautulli.dto.History;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

@Slf4j
public class Watches extends DomainModel {

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
