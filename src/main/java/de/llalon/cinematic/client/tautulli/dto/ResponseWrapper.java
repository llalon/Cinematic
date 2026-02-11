package de.llalon.cinematic.client.tautulli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Nested response wrapper containing result status, message, and actual data.
 */
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseWrapper<T> {

    /**
     * Result status: typically "success" or "error".
     */
    @JsonProperty("result")
    private final String result;

    /**
     * Optional message providing additional context.
     */
    @JsonProperty("message")
    private final String message;

    /**
     * The actual data payload.
     */
    @JsonProperty("data")
    private final T data;

    /**
     * Check if the response indicates success.
     */
    public boolean isSuccess() {
        return "success".equalsIgnoreCase(result);
    }
}
