package de.llalon.cinematic.client.tautulli.dto;

import com.squareup.moshi.Json;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Generic wrapper for all Tautulli API responses.
 * All Tautulli API responses follow this structure with the actual data nested inside a "response" object.
 *
 * @param <T> The type of the data payload
 */
@Data
@AllArgsConstructor
public class TautulliResponse<T> {

    /**
     * The response object containing result, message, and data.
     */
    @Json(name = "response")
    private final ResponseWrapper<T> response;

    /**
     * Convenience method to check if the API call was successful.
     *
     * @return true is request was successful
     */
    public boolean isSuccess() {
        return response != null && response.isSuccess();
    }

    /**
     * Convenience method to get the data payload.
     *
     * @return response object data
     */
    public T getData() {
        return response != null ? response.getData() : null;
    }

    /**
     * Convenience method to get the response message.
     *
     * @return response message
     */
    public String getMessage() {
        return response != null ? response.getMessage() : null;
    }
}
