package com.github.chaunguyentruongan.warehouse_cdnsg.dashboards;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.github.chaunguyentruongan.warehouse_cdnsg.material.MaterialRepository;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
@Tag(name = "Thống kê Tổng quan", description = "Các API cung cấp số liệu cho màn hình Dashboard")
public class DashboardController {

    private final MaterialRepository materialRepository;
    // Inject thêm ImportReceiptRepository, ExportReceiptRepository nếu cần...

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummaryStats() {
        Map<String, Object> stats = new HashMap<>();

        // Tổng số loại vật tư đang quản lý
        long totalMaterials = materialRepository.count();
        stats.put("totalMaterials", totalMaterials);

        // Số vật tư sắp hết (tồn kho < 5) - Cần thêm hàm countByInventoryLessThan(int
        // qty) trong MaterialRepository
        // long lowStockAlerts = materialRepository.countByInventoryLessThan(5);
        // stats.put("lowStockAlerts", lowStockAlerts);

        // Tương tự, bạn có thể gọi repository count số phiếu mượn trễ hạn, số phiếu
        // xuất trong tháng...
        stats.put("totalExportsThisMonth", 128); // Tạm mock để giao diện hiển thị

        return ResponseEntity.ok(stats);
    }
}