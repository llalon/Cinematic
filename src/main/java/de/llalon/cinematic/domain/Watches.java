package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.tautulli.dto.History;
import lombok.experimental.Delegate;

public class Watches extends DomainModel {

    @Delegate
    private final History tautulliHistory;

    Watches(ClientContext ctx, History tautulliHistory) {
        super(ctx);
        this.tautulliHistory = tautulliHistory;
    }
}
