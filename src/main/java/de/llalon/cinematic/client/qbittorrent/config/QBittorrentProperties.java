package de.llalon.cinematic.client.qbittorrent.config;

import lombok.Data;

/**
 * Configuration properties for qBittorrent WebUI API.
 *
 * Properties are loaded from application.yaml with prefix "services.qbittorrent".
 * Values can be overridden via environment variables.
 */
@Data
public class QBittorrentProperties {

    /**
     * Base URL of the qBittorrent WebUI.
     * Example: http://localhost:8080
     */
    private String url;

    /**
     * Username for WebUI authentication.
     */
    private String username;

    /**
     * Password for WebUI authentication.
     */
    private String password;
}
