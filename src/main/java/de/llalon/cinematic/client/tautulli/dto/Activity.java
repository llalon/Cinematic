package de.llalon.cinematic.client.tautulli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 * Represents the current activity on the Plex Media Server as reported by Tautulli.
 * This includes all active streaming sessions and bandwidth information.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Activity {

    /**
     * Current LAN bandwidth usage in bytes.
     */
    @JsonProperty("lan_bandwidth")
    private Long lanBandwidth;

    /**
     * Current WAN bandwidth usage in bytes.
     */
    @JsonProperty("wan_bandwidth")
    private Long wanBandwidth;

    /**
     * Total number of active stream sessions.
     */
    @JsonProperty("stream_count")
    private Integer streamCount;

    /**
     * Total number of transcode sessions.
     */
    @JsonProperty("stream_count_transcode")
    private Integer streamCountTranscode;

    /**
     * Total number of direct play sessions.
     */
    @JsonProperty("stream_count_direct_play")
    private Integer streamCountDirectPlay;

    /**
     * Total number of direct stream sessions.
     */
    @JsonProperty("stream_count_direct_stream")
    private Integer streamCountDirectStream;

    /**
     * List of all active streaming sessions.
     */
    @JsonProperty("sessions")
    private List<Session> sessions;
}
