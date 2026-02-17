package de.llalon.cinematic.client.overseerr.dto;

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
public class PagedResults<T> {
    private final PageInfo pageInfo;
    private final List<T> results;
}
