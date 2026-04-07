package com.github.chaunguyentruongan.warehouse_cdnsg.export_receipt;

import com.github.chaunguyentruongan.warehouse_cdnsg.enums.ReceiptStatus;
import com.github.chaunguyentruongan.warehouse_cdnsg.exception.ResourceNotFoundException;
import com.github.chaunguyentruongan.warehouse_cdnsg.material.MaterialService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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

    public Page<ExportReceipt> getAll(LocalDate fromDate, LocalDate toDate, String keyword, Pageable pageable) {
        return exportReceiptRepository.searchAndFilter(fromDate, toDate, keyword, pageable);
    }

    @Transactional
    public ExportReceipt create(ExportReceiptRequest request) {
        ExportReceipt exportReceipt = new ExportReceipt();
        exportReceipt.setExportDate(request.getExportDate());
        exportReceipt.setNote(request.getNote());
        exportReceipt.setDepartment(request.getDepartment());
        exportReceipt.setRecipient(request.getRecipient());

        String dateStr = request.getExportDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        long countToday = exportReceiptRepository.countByExportDate(request.getExportDate());

        // LOGIC MỚI ÁP DỤNG MÃ TỰ NHẬP
        if (request.getInvoiceCode() != null && !request.getInvoiceCode().trim().isEmpty() && !request.getInvoiceCode().equalsIgnoreCase("")) {
            exportReceipt.setReceiptCode(request.getInvoiceCode());
        } else {
            String receiptCode = String.format("PX-%s-%03d", dateStr, countToday + 1);
            exportReceipt.setReceiptCode(receiptCode);
        }

        exportReceipt.setExportItems(new ArrayList<>());

        for (ExportItemRequest itemReq : request.getExportItemRequests()) {
            materialService.updateStock(itemReq.getMaterialId(), -itemReq.getQuantity());

            ExportItem item = ExportItem.builder()
                    .exportReceipt(exportReceipt)
                    .material(materialService.findById(itemReq.getMaterialId()))
                    .quantity(itemReq.getQuantity())
                    .build();
            exportReceipt.getExportItems().add(item);
        }

        return exportReceiptRepository.save(exportReceipt);
    }

    @Transactional
    public ExportReceipt update(Long id, ExportReceiptRequest request) {
        ExportReceipt existing = findById(id);

        if (existing.getStatus() == ReceiptStatus.CANCELLED) {
            throw new RuntimeException("Phiếu xuất này đã bị hủy từ trước!");
        }

        for (ExportItem item : existing.getExportItems()) {
            materialService.updateStock(item.getMaterial().getId(), item.getQuantity());
        }

        existing.getExportItems().clear();

        existing.setExportDate(request.getExportDate());
        existing.setNote(request.getNote());
        existing.setDepartment(request.getDepartment());
        existing.setRecipient(request.getRecipient());

        // CẬP NHẬT LẠI MÃ NẾU NGƯỜI DÙNG SỬA
        if (request.getInvoiceCode() != null && !request.getInvoiceCode().trim().isEmpty()) {
            existing.setReceiptCode(request.getInvoiceCode());
        }

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

        for (ExportItem item : existing.getExportItems()) {
            materialService.updateStock(item.getMaterial().getId(), item.getQuantity());
        }

        exportReceiptRepository.save(existing);
    }
}