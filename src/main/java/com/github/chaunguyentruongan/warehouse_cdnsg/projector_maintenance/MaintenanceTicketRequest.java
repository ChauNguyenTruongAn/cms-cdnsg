package com.github.chaunguyentruongan.warehouse_cdnsg.projector_maintenance;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class MaintenanceTicketRequest {
    private String ticketCode; // Có thể để hệ thống tự sinh hoặc người dùng tự nhập
    private LocalDate startDate;
    private String technician; // Tên thợ/đối tác bảo trì
    private String generalNote; // Ghi chú chung

    // Danh sách các máy chiếu nằm trong đợt bảo trì này
    private List<MaintenanceDetailRequest> details;
}