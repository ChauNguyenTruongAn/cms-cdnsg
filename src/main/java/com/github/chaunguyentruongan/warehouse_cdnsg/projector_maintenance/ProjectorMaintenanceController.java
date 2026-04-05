package com.github.chaunguyentruongan.warehouse_cdnsg.projector_maintenance;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.github.chaunguyentruongan.warehouse_cdnsg.projector_core.Projector;
import com.github.chaunguyentruongan.warehouse_cdnsg.projector_core.ProjectorStatus;

import java.util.List;

@RestController
@RequestMapping("/api/projector-maintenances")
@RequiredArgsConstructor
@Tag(name = "Quản lý Bảo trì Máy chiếu", description = "Ghi nhận lịch sử bảo trì, sửa chữa và thay thế linh kiện máy chiếu")
public class ProjectorMaintenanceController {

    private final ProjectorMaintenanceService maintenanceService;
    private final ProjectorMaintenanceRepository maintenanceRepository;

    @Operation(summary = "Ghi nhận 1 lần bảo trì", description = "Tạo một log bảo trì mới. Có thể cập nhật luôn trạng thái của máy chiếu sau khi bảo trì xong (VD: chuyển thành AVAILABLE hoặc BROKEN).")
    @PostMapping
    public ResponseEntity<ProjectorMaintenance> logMaintenance(@RequestBody MaintenanceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(maintenanceService.logMaintenance(request));
    }

    @Operation(summary = "Xem lịch sử bảo trì của 1 máy chiếu", description = "Truyền vào ID của máy chiếu để xem toàn bộ lịch sử nó đã được đem đi sửa chữa những lần nào.")
    @GetMapping("/projector/{projectorId}")
    public ResponseEntity<List<ProjectorMaintenance>> getMaintenanceHistory(@PathVariable Long projectorId) {
        List<ProjectorMaintenance> history = maintenanceRepository
                .findByProjectorIdOrderByMaintenanceDateDesc(projectorId);
        return ResponseEntity.ok(history);
    }

    @Operation(summary = "Lấy toàn bộ lịch sử bảo trì", description = "Dành cho tab quản lý bảo trì tổng quát")
    @GetMapping
    public ResponseEntity<Page<ProjectorMaintenance>> getAllMaintenances(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "maintenanceDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String keyword) { // Thêm tham số keyword ở đây

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        // Truyền keyword vào service
        return ResponseEntity.ok(maintenanceService.getAllMaintenances(keyword, pageable));
    }

    @Operation(summary = "Cập nhật trạng thái máy chiếu", description = "Cập nhật nhanh trạng thái máy chiếu (Sẵn sàng, Đang bảo trì, Hỏng...)")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Projector> updateStatus(
            @PathVariable Long id,
            @RequestParam ProjectorStatus status) {
        return ResponseEntity.ok(maintenanceService.updateStatus(id, status));
    }
}