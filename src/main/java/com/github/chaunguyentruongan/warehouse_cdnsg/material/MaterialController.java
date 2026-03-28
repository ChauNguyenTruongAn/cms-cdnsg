package com.github.chaunguyentruongan.warehouse_cdnsg.material;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
@Tag(name = "Quản lý Vật tư", description = "Các API thêm, sửa, xóa, lấy danh sách thông tin vật tư (hàng hóa)")
public class MaterialController {

    private final MaterialService materialService;

    @Operation(summary = "Tạo mới vật tư", description = "Thêm một loại vật tư mới vào danh mục. Tồn kho mặc định ban đầu sẽ là 0.")
    @PostMapping
    public ResponseEntity<?> create(@RequestBody MaterialRequestCreate request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(materialService.create(request));
    }

    @Operation(summary = "Lấy chi tiết vật tư", description = "Truy vấn thông tin chi tiết của một vật tư cụ thể dựa trên ID.")
    @GetMapping
    public ResponseEntity<?> get(@RequestParam Long id) {
        return ResponseEntity.ok(materialService.findById(id));
    }

    @Operation(summary = "Lấy toàn bộ vật tư", description = "Trả về danh sách tất cả các vật tư đang có trong hệ thống.")
    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(materialService.findAll());
    }

    @Operation(summary = "Cập nhật vật tư", description = "Chỉnh sửa thông tin (tên, đơn vị tính) của vật tư đang tồn tại theo ID.")
    @PutMapping
    public ResponseEntity<?> update(@RequestParam Long id, @RequestBody MaterialRequestCreate requestCreate) {
        return ResponseEntity.status(HttpStatus.OK).body(materialService.update(id, requestCreate));
    }

    @Operation(summary = "Xóa vật tư", description = "Xóa hoàn toàn một vật tư khỏi hệ thống dựa trên ID.")
    @DeleteMapping
    public ResponseEntity<?> delete(@RequestParam Long id) {
        // Đã fix lỗi logic: Gọi service để thực sự xóa dưới Database
        materialService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Delete materials id: " + id);
    }
}