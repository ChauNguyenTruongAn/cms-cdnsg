package com.github.chaunguyentruongan.warehouse_cdnsg.material;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UnitService {
    private final UnitRepository unitRepository;

    public Unit findById(Long id) {
        return unitRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found unit by id: " + id));
    }

    public List<Unit> findAllUnit() {
        return unitRepository.findAll();
    }

    public Unit create(UnitRequest request) {
        Unit unit = new Unit();
        unit.setName(request.getName());
        return unitRepository.save(unit);
    }

    public Unit updateName(Long id, UnitRequest request) {
        Unit unit = unitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found unit by id: " + id));
        unit.setName(request.getName());
        return unit;
    }

    public void delete(Long id) {
        unitRepository.deleteById(id);
    }
}
