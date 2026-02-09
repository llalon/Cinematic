package de.llalon.cinematic.client.tautulli.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 * Paginated table response for history, library items, and other tables.
 * Used by Tautulli's table-based API endpoints.
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class TableResponse<T> {

    /**
     * Draw counter for DataTables compatibility.
     */
    @JsonProperty("draw")
    private Integer draw;

    /**
     * Total number of records (before filtering).
     */
    @JsonProperty("recordsTotal")
    private Integer recordsTotal;

    /**
     * Number of records after filtering.
     */
    @JsonProperty("recordsFiltered")
    private Integer recordsFiltered;

    /**
     * The actual data rows.
     */
    @JsonProperty("data")
    private List<T> data;

    /**
     * Total duration across all records (history tables only).
     */
    @JsonProperty("total_duration")
    private String totalDuration;

    /**
     * Total duration for filtered records (history tables only).
     */
    @JsonProperty("filter_duration")
    private String filterDuration;

    /**
     * Total file size across all records (media info tables only).
     */
    @JsonProperty("total_file_size")
    private Long totalFileSize;

    /**
     * Total file size for filtered records (media info tables only).
     */
    @JsonProperty("filtered_file_size")
    private Long filteredFileSize;

    /**
     * Timestamp of when the table was last refreshed (media info tables only).
     */
    @JsonProperty("last_refreshed")
    private Long lastRefreshed;
}
