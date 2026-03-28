package com.github.chaunguyentruongan.warehouse_cdnsg.uniform_core;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/uniforms")
@RequiredArgsConstructor
@Tag(name = "Quản lý Danh mục Đồng phục", description = "CRUD các loại đồng phục (Quần, Áo...) và Size")
public class UniformController {
    private final UniformService uniformService;

    @Operation(summary = "Thêm loại đồng phục mới")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody UniformRequestCreate request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(uniformService.create(request));
    }

    @Operation(summary = "Lấy toàn bộ danh sách")
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(uniformService.findAll());
    }

    @Operation(summary = "Xóa loại đồng phục")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        uniformService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}