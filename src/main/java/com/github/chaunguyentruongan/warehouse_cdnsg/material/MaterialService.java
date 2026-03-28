package com.github.chaunguyentruongan.warehouse_cdnsg.material;

import java.util.List;

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

    public List<Material> findAll() {
        return materialRepository.findAll();
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
        Material material = findById(materialId);

        // Tính toán số lượng mới
        int newQuantity = material.getInventory() + quantityChange;

        // Validate để đảm bảo tồn kho không bị âm (rất quan trọng khi xuất hàng hoặc
        // xóa phiếu nhập)
        if (newQuantity < 0) {
            throw new IllegalArgumentException("Tồn kho không đủ cho vật tư: " + material.getName());
        }

        material.setInventory(newQuantity);
        materialRepository.save(material);
    }

}
