package com.github.chaunguyentruongan.warehouse_cdnsg.projector_core;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projectors")
@RequiredArgsConstructor
@Tag(name = "Quản lý Danh mục Máy chiếu", description = "Các API thêm mới, xem danh sách và cập nhật trạng thái máy chiếu")
public class ProjectorController {

    private final ProjectorService projectorService;

    @Operation(summary = "Thêm máy chiếu mới", description = "Đăng ký một máy chiếu mới vào kho. Trạng thái mặc định sẽ là AVAILABLE.")
    @PostMapping
    public ResponseEntity<Projector> createProjector(@RequestBody ProjectorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectorService.create(request));
    }

    @Operation(summary = "Lấy danh sách máy chiếu (Phân trang)", description = "Xem máy chiếu có hỗ trợ tìm kiếm và phân trang")
    @GetMapping
    public ResponseEntity<Page<Projector>> getAllProjectors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) String keyword) {

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(projectorService.getAll(keyword, pageable));
    }

    @Operation(summary = "Lấy chi tiết 1 máy chiếu", description = "Xem thông tin chi tiết của máy chiếu bằng ID, bao gồm cả lịch sử mượn và bảo trì.")
    @GetMapping("/{id}")
    public ResponseEntity<Projector> getProjectorById(@PathVariable Long id) {
        return ResponseEntity.ok(projectorService.findById(id));
    }

    @Operation(summary = "Cập nhật thông tin máy chiếu")
    @PutMapping("/{id}")
    public ResponseEntity<Projector> updateProjector(@PathVariable Long id, @RequestBody ProjectorRequest request) {
        return ResponseEntity.ok(projectorService.update(id, request));
    }

    @Operation(summary = "Xóa máy chiếu")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProjector(@PathVariable Long id) {
        projectorService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Cập nhật trạng thái máy chiếu")
    @PatchMapping("/{id}/status")
    public ResponseEntity<Projector> updateStatus(
            @PathVariable Long id,
            @RequestParam ProjectorStatus status) {
        // Bây giờ service đã có hàm updateStatus, gọi sẽ không còn lỗi
        return ResponseEntity.ok(projectorService.updateStatus(id, status));
    }

    @Operation(summary = "Thống kê máy chiếu")
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getProjectorStats() {
        Map<String, Object> stats = new HashMap<>();

        // SỬA TẠI ĐÂY: Gọi trực tiếp hàm countByStatus từ service
        long available = projectorService.countByStatus(ProjectorStatus.AVAILABLE);
        long borrowed = projectorService.countByStatus(ProjectorStatus.BORROWED);
        long maintenance = projectorService.countByStatus(ProjectorStatus.UNDER_MAINTENANCE);
        long broken = projectorService.countByStatus(ProjectorStatus.BROKEN);

        stats.put("total", available + borrowed + maintenance + broken);
        stats.put("available", available);
        stats.put("borrowed", borrowed);
        stats.put("under_maintenance", maintenance);
        stats.put("broken", broken);

        return ResponseEntity.ok(stats);
    }
}