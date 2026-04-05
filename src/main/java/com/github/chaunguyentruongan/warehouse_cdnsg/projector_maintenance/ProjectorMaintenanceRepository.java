package com.github.chaunguyentruongan.warehouse_cdnsg.projector_maintenance;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectorMaintenanceRepository extends JpaRepository<ProjectorMaintenance, Long> {

    // Hàm này giúp lấy toàn bộ lịch sử sửa chữa của 1 máy chiếu, sắp xếp từ mới
    // nhất đến cũ nhất
    List<ProjectorMaintenance> findByProjectorIdOrderByMaintenanceDateDesc(Long projectorId);

    @Query("SELECT m FROM ProjectorMaintenance m JOIN FETCH m.projector p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
            "LOWER(p.serialNumber) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
            "LOWER(m.technician) LIKE LOWER(CONCAT('%', :kw, '%'))")
    Page<ProjectorMaintenance> searchHistory(@Param("kw") String kw, Pageable pageable);
}