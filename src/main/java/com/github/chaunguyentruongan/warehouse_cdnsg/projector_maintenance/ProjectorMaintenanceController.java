package com.github.chaunguyentruongan.warehouse_cdnsg.projector_maintenance;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projector-maintenances")
@RequiredArgsConstructor
@Tag(name = "Quản lý Bảo trì Máy chiếu", description = "Các API tạo phiếu bảo trì nhiều máy, cập nhật tiến độ bảo trì")
public class ProjectorMaintenanceController {

    private final ProjectorMaintenanceService maintenanceService;

    @Operation(summary = "Tạo phiếu bảo trì (Hỗ trợ nhiều máy chiếu)")
    @PostMapping
    public ResponseEntity<MaintenanceTicket> createTicket(@RequestBody MaintenanceTicketRequest request) {
        return ResponseEntity.ok(maintenanceService.createTicket(request));
    }

    @Operation(summary = "Lấy danh sách các đợt bảo trì")
    @GetMapping
    public ResponseEntity<List<MaintenanceTicket>> getAllTickets() {
        return ResponseEntity.ok(maintenanceService.getAllTickets());
    }

    @Operation(summary = "Lấy lịch sử bảo trì của 1 máy chiếu")
    @GetMapping("/projector/{projectorId}")
    public ResponseEntity<List<ProjectorMaintenanceDetail>> getHistoryByProjector(@PathVariable Long projectorId) {
        return ResponseEntity.ok(maintenanceService.getHistoryByProjectorId(projectorId));
    }

    @Operation(summary = "Hoàn tất phiếu bảo trì", description = "Cho phép đánh giá trạng thái từng máy sau bảo trì")
    @PostMapping("/{id}/complete")
    public ResponseEntity<MaintenanceTicket> completeTicket(
            @PathVariable Long id,
            @RequestBody CompleteMaintenanceRequest request) { // SỬA ĐỔI Ở ĐÂY
        return ResponseEntity.ok(maintenanceService.completeTicket(id, request));
    }

    @Operation(summary = "Cập nhật phiếu bảo trì đang diễn ra")
    @PutMapping("/{id}")
    public ResponseEntity<MaintenanceTicket> updateTicket(
            @PathVariable Long id,
            @RequestBody MaintenanceTicketRequest request) {
        return ResponseEntity.ok(maintenanceService.updateTicket(id, request));
    }
}