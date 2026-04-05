package com.github.chaunguyentruongan.warehouse_cdnsg.projector_maintenance;

import com.github.chaunguyentruongan.warehouse_cdnsg.projector_core.ProjectorStatus;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CompleteMaintenanceRequest {
    private LocalDate completionDate;
    private List<MaintenanceResult> results;

    @Getter
    @Setter
    public static class MaintenanceResult {
        private Long projectorId;
        private ProjectorStatus status; // Trạng thái sau bảo trì (AVAILABLE hoặc BROKEN)
    }
}