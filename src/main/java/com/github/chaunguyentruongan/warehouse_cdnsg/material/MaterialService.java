package com.github.chaunguyentruongan.warehouse_cdnsg.material;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MaterialService {
    private final MaterialRepository materialRepository;
    private final UnitService unitService;

    public Material create(MaterialRequestCreate request) {
        Material newMaterial = new Material();
        newMaterial.setName(request.getName());
        newMaterial.setUnit(unitService.findById(request.getUnit_id()));
        return materialRepository.save(newMaterial);
    }

}
