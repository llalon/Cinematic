package de.llalon.cinematic.client.radarr.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 * Represents a paginated response of queue resources from Radarr.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueueResourcePagingResource {

    @JsonProperty("page")
    private Integer page;

    @JsonProperty("pageSize")
    private Integer pageSize;

    @JsonProperty("sortKey")
    private String sortKey;

    @JsonProperty("sortDirection")
    private String sortDirection;

    @JsonProperty("totalRecords")
    private Integer totalRecords;

    @JsonProperty("records")
    private List<QueueResource> records;
}
