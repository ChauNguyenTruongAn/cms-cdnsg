package com.github.chaunguyentruongan.warehouse_cdnsg.material;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.github.chaunguyentruongan.warehouse_cdnsg.exception.ResourceNotFoundException;
import com.github.chaunguyentruongan.warehouse_cdnsg.exception.SqlDuplicateException;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialService {
    private final MaterialRepository materialRepository;
    private final UnitService unitService;

    public Material create(MaterialRequestCreate request) {
        try {
            Material newMaterial = new Material();
            newMaterial.setName(request.getName());
            newMaterial.setUnit(unitService.findById(request.getUnit_id()));
            newMaterial.setInventory(0);
            return materialRepository.save(newMaterial);
        } catch (Exception e) {
            throw new SqlDuplicateException(e.getMessage());
        }
    }

    public Material findById(Long id) {
        return materialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found material id: " + id));
    }

    public Page<Material> findAll(Pageable pageable) {
        return materialRepository.findAll(pageable);
    }

    public Material update(Long id, MaterialRequestCreate update) {
        Material material = findById(id);

        if (update.getUnit_id() != material.getUnit().getId()) {
            Unit unit = unitService.findById(update.getUnit_id());
            material.setUnit(unit);
        }
        material.setName(update.getName());
        return materialRepository.save(material);
    }

    public void delete(Long id) {
        materialRepository.deleteById(id);
    }

    @Transactional
    public void updateStock(Long materialId, int quantityChange) {
        // Sử dụng hàm có Lock để chống đụng độ dữ liệu khi nhiều người thao tác cùng
        // lúc
        Material material = materialRepository.findByIdWithLock(materialId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found material id: " + materialId));

        int newQuantity = material.getInventory() + quantityChange;

        if (newQuantity < 0) {
            throw new IllegalArgumentException("Tồn kho không đủ cho vật tư: " + material.getName());
        }

        material.setInventory(newQuantity);
        materialRepository.save(material);
    }

    // Thay thế đoạn findAll cũ bằng đoạn này
    public Page<Material> findAll(String keyword, String status, Pageable pageable) {
        return materialRepository.searchWithFilter(keyword, status, pageable);
    }

    public java.util.Map<String, Object> getMaterialStats() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("totalTypes", materialRepository.count()); // Tổng số loại
        stats.put("totalItems", materialRepository.sumTotalInventory()); // Tổng số lượng tồn
        stats.put("lowStockCount", materialRepository.countLowStock()); // Sắp hết
        stats.put("okStockCount", materialRepository.countOkStock()); // Ổn định
        return stats;
    }

}
