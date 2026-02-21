package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.tautulli.dto.History;
import org.jetbrains.annotations.NotNull;

public class Watches extends DomainModel {

    private final History tautulliHistory;

    Watches(@NotNull ClientContext ctx, @NotNull History tautulliHistory) {
        super(ctx);
        this.tautulliHistory = tautulliHistory;
    }
}
