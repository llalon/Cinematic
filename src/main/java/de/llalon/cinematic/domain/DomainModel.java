package de.llalon.cinematic.domain;

public abstract class DomainModel {

    protected final ClientContext ctx;

    protected DomainModel(ClientContext ctx) {
        this.ctx = ctx;
    }
}
