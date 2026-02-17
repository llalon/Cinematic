package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.tautulli.dto.History;

public class Watches extends DomainModel {

    private final History tautulliHistory;

    Watches(ClientContext ctx, History tautulliHistory) {
        super(ctx);
        this.tautulliHistory = tautulliHistory;
    }
}
