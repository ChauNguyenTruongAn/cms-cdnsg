package com.github.chaunguyentruongan.warehouse_cdnsg.fire_extinguisher;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/fire-extinguishers")
@RequiredArgsConstructor
@Tag(name = "Quản lý Phòng cháy chữa cháy")
public class FireExtinguisherController {
    private final FireExtinguisherService service;


    @Operation(summary = "Lấy chi tiết 1 bình chữa cháy")
    @GetMapping("/{id}")
    public ResponseEntity<FireExtinguisher> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @Operation(summary = "Xem lịch sử nạp của 1 bình")
    @GetMapping("/{id}/history")
    public ResponseEntity<List<ExtinguisherHistory>> getHistory(@PathVariable Long id) {
        return ResponseEntity.ok(service.getHistory(id));
    }

    @Operation(summary = "Cập nhật thông tin cơ bản của bình")
    @PutMapping("/{id}")
    public ResponseEntity<FireExtinguisher> update(@PathVariable Long id,
            @RequestBody FireExtinguisherRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @Operation(summary = "Xóa bình chữa cháy")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<Page<FireExtinguisher>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("nextRechargeDate").ascending());
        return ResponseEntity.ok(service.getAll(keyword, pageable));
    }

    @PostMapping
    public ResponseEntity<FireExtinguisher> create(@RequestBody FireExtinguisherRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
    }

    @PatchMapping("/{id}/recharge")
    @Operation(summary = "Ghi nhận nạp bình", description = "Chỉ cần truyền ngày nạp, hệ thống tự tính 6 tháng sau")
    public ResponseEntity<FireExtinguisher> recharge(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return ResponseEntity.ok(service.recharge(id, date));
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getStats() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("ok", service.countByStatus(MaintenanceStatus.OK));
        stats.put("warning", service.countByStatus(MaintenanceStatus.WARNING));
        stats.put("expired", service.countByStatus(MaintenanceStatus.EXPIRED));
        return ResponseEntity.ok(stats);
    }
}