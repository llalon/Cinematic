package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.qbittorrent.dto.TorrentInfo;
import lombok.experimental.Delegate;

public class Torrent extends DomainModel {

    @Delegate
    private final TorrentInfo torrentInfo;

    Torrent(ClientContext ctx, TorrentInfo torrentInfo) {
        super(ctx);
        this.torrentInfo = torrentInfo;
    }

    public void addTag(String tag) {
        // ToDo: Add the tag to this torrent
        final String torrentHash = torrentInfo.getHash();
    }

    public void addTag(Tag tag) {
        addTag(tag.getName());
    }
}
