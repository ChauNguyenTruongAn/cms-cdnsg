package com.github.chaunguyentruongan.warehouse_cdnsg.export_receipt;

import com.github.chaunguyentruongan.warehouse_cdnsg.exception.ResourceNotFoundException;
import com.github.chaunguyentruongan.warehouse_cdnsg.material.MaterialService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

        List<ExportItem> exportItems = request.getExportItemRequests()
                .stream()
                .map(itemReq -> {
                    // Xuất kho: TRỪ tồn kho (-)
                    materialService.updateStock(itemReq.getMaterialId(), -itemReq.getQuantity());

                    return ExportItem.builder()
                            .material(materialService.findById(itemReq.getMaterialId()))
                            .exportReceipt(exportReceipt)
                            .quantity(itemReq.getQuantity())
                            .build();
                })
                .collect(Collectors.toList());

        exportReceipt.setExportItems(exportItems);
        return exportReceiptRepository.save(exportReceipt);
    }

    @Transactional
    public ExportReceipt update(Long id, ExportReceiptRequest request) {
        ExportReceipt existing = findById(id);

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

        // Xóa phiếu xuất: CỘNG lại tồn kho (+)
        for (ExportItem item : existing.getExportItems()) {
            materialService.updateStock(item.getMaterial().getId(), item.getQuantity());
        }

        exportReceiptRepository.delete(existing);
    }
}