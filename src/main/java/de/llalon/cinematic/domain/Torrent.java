package de.llalon.cinematic.domain;

import de.llalon.cinematic.client.qbittorrent.dto.TorrentInfo;
import java.util.List;
import lombok.experimental.Delegate;

public class Torrent extends DomainModel {

    @Delegate
    private final TorrentInfo torrentInfo;

    Torrent(ClientContext ctx, TorrentInfo torrentInfo) {
        super(ctx);
        this.torrentInfo = torrentInfo;
    }

    public void addTag(String tag) {
        if (tag != null && !tag.isBlank()) {
            final String torrentHash = torrentInfo.getHash();
            // Ensure the tag exists, then attach it to this torrent
            ctx.getQbittorrentClient().createTags(List.of(tag));
            ctx.getQbittorrentClient().addTorrentTags(List.of(torrentHash), List.of(tag));
        }
    }

    public void addTag(Tag tag) {
        addTag(tag.getName());
    }
}
