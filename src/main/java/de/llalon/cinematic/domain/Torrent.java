package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.qbittorrent.dto.TorrentInfo;

public class Torrent extends DomainModel {

    private final TorrentInfo torrentInfo;

    Torrent(ClientContext ctx, TorrentInfo torrentInfo) {
        super(ctx);
        this.torrentInfo = torrentInfo;
    }
}
