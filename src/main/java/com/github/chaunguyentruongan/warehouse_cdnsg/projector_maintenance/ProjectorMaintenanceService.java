package com.github.chaunguyentruongan.warehouse_cdnsg.projector_maintenance;

import com.github.chaunguyentruongan.warehouse_cdnsg.exception.ResourceNotFoundException;
import com.github.chaunguyentruongan.warehouse_cdnsg.projector_core.Projector;
import com.github.chaunguyentruongan.warehouse_cdnsg.projector_core.ProjectorRepository;
import com.github.chaunguyentruongan.warehouse_cdnsg.projector_core.ProjectorService;
import com.github.chaunguyentruongan.warehouse_cdnsg.projector_core.ProjectorStatus;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProjectorMaintenanceService {

    private final ProjectorMaintenanceRepository maintenanceRepository;
    private final ProjectorRepository projectorRepository;
    private final ProjectorService projectorService;

    @Transactional
    public ProjectorMaintenance logMaintenance(MaintenanceRequest request) {
        Projector projector = projectorRepository.findById(request.getProjectorId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy máy chiếu!"));

        // Tạo log bảo trì
        ProjectorMaintenance log = ProjectorMaintenance.builder()
                .projector(projector)
                .maintenanceDate(
                        request.getMaintenanceDate() != null ? request.getMaintenanceDate() : java.time.LocalDate.now())
                .description(request.getDescription())
                .technician(request.getTechnician())
                .cost(request.getCost())
                .build();

        // Cập nhật trạng thái máy chiếu nếu có yêu cầu (Ví dụ: sửa xong thì rảnh, hỏng
        // nặng thì BROKEN)
        if (request.getUpdateProjectorStatusTo() != null) {
            projector.setStatus(request.getUpdateProjectorStatusTo());
            projectorRepository.save(projector);
        }

        return maintenanceRepository.save(log);
    }

    public Page<ProjectorMaintenance> getAllMaintenances(String keyword, Pageable pageable) {
        if (keyword != null && !keyword.trim().isEmpty()) {
            return maintenanceRepository.searchHistory(keyword.trim(), pageable);
        }
        return maintenanceRepository.findAll(pageable);
    }

    @Transactional
    public Projector updateStatus(Long id, ProjectorStatus status) {
        Projector projector = projectorService.findById(id);
        projector.setStatus(status);
        return projectorRepository.save(projector);
    }
}