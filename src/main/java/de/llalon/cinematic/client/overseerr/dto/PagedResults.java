package de.llalon.cinematic.client.overseerr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Generic wrapper for paginated results from Overseerr API.
 *
 * @param <T> The type of results in the page
 */
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PagedResults<T> {
    private final PageInfo pageInfo;
    private final List<T> results;
}
