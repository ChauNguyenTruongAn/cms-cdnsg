package com.github.chaunguyentruongan.warehouse_cdnsg.uniform_import;

import com.github.chaunguyentruongan.warehouse_cdnsg.enums.ReceiptStatus;
import com.github.chaunguyentruongan.warehouse_cdnsg.exception.ResourceNotFoundException;
import com.github.chaunguyentruongan.warehouse_cdnsg.uniform_core.UniformService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UniformImportService {
    private final UniformImportRepository importRepository;
    private final UniformService uniformService;

    public UniformImport findById(Long id) {
        return importRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found import id: " + id));
    }

    public Page<UniformImport> getAll(LocalDate from, LocalDate to, String note, String name, Pageable page) {
        return importRepository.searchAndFilter(from, to, note, name, page);
    }

    @Transactional
    public UniformImport create(UniformImportRequest request) {
        UniformImport uniformImport = new UniformImport();
        uniformImport.setDate(request.getDate());
        uniformImport.setNote(request.getNote());
        uniformImport.setNameResponse(request.getNameResponse());

        List<UniformImportDetail> details = request.getDetails().stream().map(itemReq -> {
            uniformService.updateStock(itemReq.getUniformId(), itemReq.getQuantity()); // Cộng (+) kho
            return UniformImportDetail.builder()
                    .uniform(uniformService.findById(itemReq.getUniformId()))
                    .uniformImport(uniformImport)
                    .quantity(itemReq.getQuantity())
                    .build();
        }).collect(Collectors.toList());

        uniformImport.setDetails(details);
        return importRepository.save(uniformImport);
    }

    @Transactional
    public UniformImport update(Long id, UniformImportRequest request) {
        UniformImport existing = findById(id);

        for (UniformImportDetail oldItem : existing.getDetails()) {
            uniformService.updateStock(oldItem.getUniform().getId(), -oldItem.getQuantity()); // Hoàn tác: Trừ (-)
        }
        existing.getDetails().clear();

        existing.setDate(request.getDate());
        existing.setNote(request.getNote());
        existing.setNameResponse(request.getNameResponse());

        for (UniformImportRequest.Detail itemReq : request.getDetails()) {
            uniformService.updateStock(itemReq.getUniformId(), itemReq.getQuantity()); // Áp dụng: Cộng (+)
            UniformImportDetail newItem = UniformImportDetail.builder()
                    .uniformImport(existing)
                    .uniform(uniformService.findById(itemReq.getUniformId()))
                    .quantity(itemReq.getQuantity())
                    .build();
            existing.getDetails().add(newItem);
        }
        return importRepository.save(existing);
    }

    @Transactional
    public void delete(Long id) {
        UniformImport existing = findById(id);
        // 1. Hoàn tác kho
        for (UniformImportDetail item : existing.getDetails()) {
            uniformService.updateStock(item.getUniform().getId(), -item.getQuantity());
        }
        // 2. Đổi trạng thái thay vì delete
        existing.setStatus(ReceiptStatus.CANCELLED);
        importRepository.save(existing);
    }
}