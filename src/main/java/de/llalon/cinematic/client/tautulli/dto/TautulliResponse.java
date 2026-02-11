package de.llalon.cinematic.client.tautulli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
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
@JsonIgnoreProperties(ignoreUnknown = true)
public class TautulliResponse<T> {

    /**
     * The response object containing result, message, and data.
     */
    @JsonProperty("response")
    private final ResponseWrapper<T> response;

    /**
     * Convenience method to check if the API call was successful.
     */
    public boolean isSuccess() {
        return response != null && response.isSuccess();
    }

    /**
     * Convenience method to get the data payload.
     */
    public T getData() {
        return response != null ? response.getData() : null;
    }

    /**
     * Convenience method to get the response message.
     */
    public String getMessage() {
        return response != null ? response.getMessage() : null;
    }
}
