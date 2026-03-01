package de.llalon.cinematic.client.qbittorrent.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Configuration properties for qBittorrent WebUI API.
 *
 * Properties are loaded from application.yaml with prefix "services.qbittorrent".
 * Values can be overridden via environment variables.
 */
@Data
@Builder
@AllArgsConstructor
public class QBittorrentProperties {

    private final String url;
    private final String username;
    private final String password;

    /**
     * Loads qBittorrent connection properties from the {@code QBITTORRENT_URL},
     * {@code QBITTORRENT_USERNAME}, and {@code QBITTORRENT_PASSWORD} environment variables.
     *
     * @return a {@code QBittorrentProperties} instance populated from environment
     */
    public static QBittorrentProperties fromEnvironment() {
        return QBittorrentProperties.builder()
                .url(System.getenv("QBITTORRENT_URL"))
                .username(System.getenv("QBITTORRENT_USERNAME"))
                .password(System.getenv("QBITTORRENT_PASSWORD"))
                .build();
    }
}
