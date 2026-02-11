package de.llalon.cinematic.domain;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Delegate;

@RequiredArgsConstructor
public class TorrentFile {
    @Delegate
    private final de.llalon.cinematic.client.qbittorrent.dto.TorrentFile torrentFile;
}
