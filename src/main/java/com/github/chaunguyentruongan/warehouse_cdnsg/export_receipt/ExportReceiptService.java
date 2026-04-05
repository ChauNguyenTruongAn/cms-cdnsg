package com.github.chaunguyentruongan.warehouse_cdnsg.export_receipt;

import com.github.chaunguyentruongan.warehouse_cdnsg.enums.ReceiptStatus;
import com.github.chaunguyentruongan.warehouse_cdnsg.exception.ResourceNotFoundException;
import com.github.chaunguyentruongan.warehouse_cdnsg.material.Material;
import com.github.chaunguyentruongan.warehouse_cdnsg.material.MaterialService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExportReceiptService {
    private final ExportReceiptRepository exportReceiptRepository;
    private final MaterialService materialService;

    public ExportReceipt findById(Long id) {
        return exportReceiptRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found export receipt by id: " + id));
    }

    public Page<ExportReceipt> getAll(LocalDate fromDate, LocalDate toDate, String note, String department,
            Pageable pageable) {
        return exportReceiptRepository.searchAndFilter(fromDate, toDate, note, department, pageable);
    }

    @Transactional
    public ExportReceipt create(ExportReceiptRequest request) {
        ExportReceipt exportReceipt = new ExportReceipt();
        exportReceipt.setExportDate(request.getExportDate());
        exportReceipt.setNote(request.getNote());
        exportReceipt.setDepartment(request.getDepartment());
        exportReceipt.setRecipient(request.getRecipient());

        long countToday = exportReceiptRepository.countByExportDate(request.getExportDate());
        String dateStr = request.getExportDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String receiptCode = String.format("PX-%s-%03d", dateStr, countToday + 1);
        exportReceipt.setReceiptCode(receiptCode);

        // 2. THIẾT LẬP NGƯỜI LẬP PHIẾU & TRẠNG THÁI
        exportReceipt.setCreatedBy("Admin (Hệ thống)"); // Tạm fix cứng, sau này map với Security Context
        exportReceipt.setStatus(ReceiptStatus.COMPLETED);

        // 3. XỬ LÝ DANH SÁCH CHI TIẾT (Export Items) & TRỪ TỒN KHO
        List<ExportItem> items = new ArrayList<>();

        if (request.getExportItemRequests() != null) {
            for (ExportItemRequest itemReq : request.getExportItemRequests()) {
                ExportItem item = new ExportItem();

                // Lấy thông tin vật tư
                Material material = materialService.findById(itemReq.getMaterialId());
                item.setMaterial(material);
                item.setQuantity(itemReq.getQuantity());
                item.setExportReceipt(exportReceipt); // Set quan hệ 2 chiều

                items.add(item);

                // TRỪ TỒN KHO: Truyền số âm (-) vào hàm updateStock
                materialService.updateStock(material.getId(), -itemReq.getQuantity());
            }
        }

        exportReceipt.setExportItems(items);

        // Lưu vào cơ sở dữ liệu
        return exportReceiptRepository.save(exportReceipt);
    }

    @Transactional
    public ExportReceipt update(Long id, ExportReceiptRequest request) {
        ExportReceipt existing = findById(id);

        if (existing.getStatus() == ReceiptStatus.CANCELLED) {
            throw new RuntimeException("Không thể chỉnh sửa phiếu xuất đã bị hủy!");
        }

        // 1. Hoàn tác số lượng cũ: CỘNG lại vào kho (+)
        for (ExportItem oldItem : existing.getExportItems()) {
            materialService.updateStock(oldItem.getMaterial().getId(), oldItem.getQuantity());
        }

        existing.getExportItems().clear();

        existing.setExportDate(request.getExportDate());
        existing.setNote(request.getNote());
        existing.setDepartment(request.getDepartment());
        existing.setRecipient(request.getRecipient());

        // 2. Áp dụng số lượng mới: TRỪ tồn kho (-)
        for (ExportItemRequest itemReq : request.getExportItemRequests()) {
            materialService.updateStock(itemReq.getMaterialId(), -itemReq.getQuantity());

            ExportItem newItem = ExportItem.builder()
                    .exportReceipt(existing)
                    .material(materialService.findById(itemReq.getMaterialId()))
                    .quantity(itemReq.getQuantity())
                    .build();
            existing.getExportItems().add(newItem);
        }

        return exportReceiptRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        ExportReceipt existing = findById(id);

        if (existing.getStatus() == ReceiptStatus.CANCELLED) {
            throw new RuntimeException("Phiếu xuất này đã bị hủy từ trước!");
        }

        existing.setStatus(ReceiptStatus.CANCELLED);

        // Xóa phiếu xuất: CỘNG lại tồn kho (+)
        for (ExportItem item : existing.getExportItems()) {
            materialService.updateStock(item.getMaterial().getId(), item.getQuantity());
        }

        exportReceiptRepository.save(existing);
    }
}