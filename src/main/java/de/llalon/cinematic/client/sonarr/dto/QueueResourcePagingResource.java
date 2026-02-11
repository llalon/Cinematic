package de.llalon.cinematic.client.sonarr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Represents a paginated response of queue resources from Sonarr.
 */
@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueueResourcePagingResource {

    @JsonProperty("page")
    private final Integer page;

    @JsonProperty("pageSize")
    private final Integer pageSize;

    @JsonProperty("sortKey")
    private final String sortKey;

    @JsonProperty("sortDirection")
    private final String sortDirection;

    @JsonProperty("totalRecords")
    private final Integer totalRecords;

    @JsonProperty("records")
    private final List<QueueResource> records;
}
