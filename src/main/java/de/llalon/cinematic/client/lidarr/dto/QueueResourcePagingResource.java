package de.llalon.cinematic.client.lidarr.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/** Lidarr QueueResourcePagingResource API resource. */
@Data
@Builder
@AllArgsConstructor
public class QueueResourcePagingResource {
    private final Integer page;
    private final Integer pageSize;
    private final String sortKey;
    private final String sortDirection;
    private final Integer totalRecords;
    private final List<LidarrQueue> records;
}
