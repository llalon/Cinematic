package de.llalon.cinematic.domain;

import org.jetbrains.annotations.NotNull;

public abstract class DomainModel {

    protected final ClientContext ctx;

    protected DomainModel(@NotNull ClientContext ctx) {
        this.ctx = ctx;
    }
}
