package de.llalon.cinematic.client.tautulli.dto;

import com.squareup.moshi.Json;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Paginated table response for history, library items, and other tables.
 * Used by Tautulli's table-based API endpoints.
 */
@Data
@AllArgsConstructor
public class TableResponse<T> {

    /**
     * Draw counter for DataTables compatibility.
     */
    @Json(name = "draw")
    private final Integer draw;

    /**
     * Total number of records (before filtering).
     */
    @Json(name = "recordsTotal")
    private final Integer recordsTotal;

    /**
     * Number of records after filtering.
     */
    @Json(name = "recordsFiltered")
    private final Integer recordsFiltered;

    /**
     * The actual data rows.
     */
    @Json(name = "data")
    private final List<T> data;

    /**
     * Total duration across all records (history tables only).
     */
    @Json(name = "total_duration")
    private final String totalDuration;

    /**
     * Total duration for filtered records (history tables only).
     */
    @Json(name = "filter_duration")
    private final String filterDuration;

    /**
     * Total file size across all records (media info tables only).
     */
    @Json(name = "total_file_size")
    private final Long totalFileSize;

    /**
     * Total file size for filtered records (media info tables only).
     */
    @Json(name = "filtered_file_size")
    private final Long filteredFileSize;

    /**
     * Timestamp of when the table was last refreshed (media info tables only).
     */
    @Json(name = "last_refreshed")
    private final Long lastRefreshed;
}
