package de.llalon.cinematic.client.tautulli.dto;

import com.squareup.moshi.Json;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents the current activity on the Plex Media Server as reported by Tautulli.
 * This includes all active streaming sessions and bandwidth information.
 */
@Data
@AllArgsConstructor
public class Activity {

    /**
     * Current LAN bandwidth usage in bytes.
     */
    @Json(name = "lan_bandwidth")
    private final Long lanBandwidth;

    /**
     * Current WAN bandwidth usage in bytes.
     */
    @Json(name = "wan_bandwidth")
    private final Long wanBandwidth;

    /**
     * Total number of active stream sessions.
     */
    @Json(name = "stream_count")
    private final Integer streamCount;

    /**
     * Total number of transcode sessions.
     */
    @Json(name = "stream_count_transcode")
    private final Integer streamCountTranscode;

    /**
     * Total number of direct play sessions.
     */
    @Json(name = "stream_count_direct_play")
    private final Integer streamCountDirectPlay;

    /**
     * Total number of direct stream sessions.
     */
    @Json(name = "stream_count_direct_stream")
    private final Integer streamCountDirectStream;

    /**
     * List of all active streaming sessions.
     */
    @Json(name = "sessions")
    private final List<Session> sessions;
}
