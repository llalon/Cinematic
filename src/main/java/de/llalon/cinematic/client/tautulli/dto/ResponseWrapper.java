package de.llalon.cinematic.client.tautulli.dto;

import com.squareup.moshi.Json;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Nested response wrapper containing result status, message, and actual data.
 */
@Data
@AllArgsConstructor
public class ResponseWrapper<T> {

    /**
     * Result status: typically "success" or "error".
     */
    @Json(name = "result")
    private final String result;

    /**
     * Optional message providing additional context.
     */
    @Json(name = "message")
    private final String message;

    /**
     * The actual data payload.
     */
    @Json(name = "data")
    private final T data;

    /**
     * Check if the response indicates success.
     */
    public boolean isSuccess() {
        return "success".equalsIgnoreCase(result);
    }
}
