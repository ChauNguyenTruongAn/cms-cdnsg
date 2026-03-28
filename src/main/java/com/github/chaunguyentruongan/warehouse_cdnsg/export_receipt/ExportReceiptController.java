package com.github.chaunguyentruongan.warehouse_cdnsg.export_receipt;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/export-receipt")
@RequiredArgsConstructor
@Tag(name = "Quản lý Xuất Kho", description = "Các API thêm, sửa, xóa, tìm kiếm phiếu xuất hàng")
public class ExportReceiptController {

    private final ExportReceiptService exportReceiptService;

    @Operation(summary = "Tạo mới một phiếu xuất", description = "Ghi nhận xuất kho vật tư cho phòng ban/người nhận")
    @PostMapping
    public ResponseEntity<ExportReceipt> createExport(@RequestBody ExportReceiptRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(exportReceiptService.create(request));
    }

    @Operation(summary = "Lấy danh sách phiếu xuất", description = "Hỗ trợ phân trang, tìm kiếm theo ngày, ghi chú và phòng ban")
    @GetMapping
    public ResponseEntity<Page<ExportReceipt>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String note,
            @RequestParam(required = false) String department) {

        Sort sort = direction.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ExportReceipt> result = exportReceiptService.getAll(fromDate, toDate, note, department, pageable);
        return ResponseEntity.ok(result);
    }

    @Operation(summary = "Lấy chi tiết phiếu xuất", description = "Tìm kiếm phiếu xuất theo ID")
    @GetMapping("/{id}")
    public ResponseEntity<ExportReceipt> getById(@PathVariable Long id) {
        return ResponseEntity.ok(exportReceiptService.findById(id));
    }

    @Operation(summary = "Cập nhật phiếu xuất", description = "Cập nhật thông tin phiếu xuất. Tồn kho sẽ tự động được điều chỉnh lại.")
    @PutMapping("/{id}")
    public ResponseEntity<ExportReceipt> updateExport(@PathVariable Long id,
            @RequestBody ExportReceiptRequest request) {
        return ResponseEntity.ok(exportReceiptService.update(id, request));
    }

    @Operation(summary = "Xóa phiếu xuất", description = "Xóa phiếu xuất theo ID và cộng lại số lượng vật tư vào kho")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExport(@PathVariable Long id) {
        exportReceiptService.delete(id);
        return ResponseEntity.noContent().build();
    }
}