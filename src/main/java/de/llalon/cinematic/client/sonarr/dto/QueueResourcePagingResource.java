package de.llalon.cinematic.client.sonarr.dto;

import com.squareup.moshi.Json;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a paginated response of queue resources from Sonarr.
 */
@Data
@AllArgsConstructor
public class QueueResourcePagingResource {

    @Json(name = "page")
    private final Integer page;

    @Json(name = "pageSize")
    private final Integer pageSize;

    @Json(name = "sortKey")
    private final String sortKey;

    @Json(name = "sortDirection")
    private final String sortDirection;

    @Json(name = "totalRecords")
    private final Integer totalRecords;

    @Json(name = "records")
    private final List<QueueResource> records;
}
