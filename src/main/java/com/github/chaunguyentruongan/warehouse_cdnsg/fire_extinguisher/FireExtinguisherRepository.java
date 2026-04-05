package com.github.chaunguyentruongan.warehouse_cdnsg.fire_extinguisher;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FireExtinguisherRepository extends JpaRepository<FireExtinguisher, Long> {

    // CẬP NHẬT: JOIN với location và zone để tìm kiếm
    @Query("SELECT f FROM FireExtinguisher f " +
            "JOIN f.location l " +
            "JOIN l.zone z " +
            "WHERE LOWER(f.type) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
            "LOWER(l.name) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
            "LOWER(z.name) LIKE LOWER(CONCAT('%', :kw, '%'))")
    Page<FireExtinguisher> search(@Param("kw") String kw, Pageable pageable);

    long countByStatus(MaintenanceStatus status);

    // BỔ SUNG: Hỗ trợ tìm theo Vị trí
    Page<FireExtinguisher> findByLocationId(Long locationId, Pageable pageable);

    // BỔ SUNG: Hỗ trợ tìm theo Khu vực
    Page<FireExtinguisher> findByLocationZoneId(Long zoneId, Pageable pageable);

    List<FireExtinguisher> findAllByLocationZoneId(Long zoneId);

    // Báo cáo thống kê: Gom nhóm theo Tên Khu và Loại bình
    @Query("SELECT new com.github.chaunguyentruongan.warehouse_cdnsg.fire_extinguisher.ZoneExtinguisherStatsResponse(" +
            "z.name, f.type, SUM(f.quantity), MAX(f.lastRechargeDate), MIN(f.nextRechargeDate)) " +
            "FROM FireExtinguisher f JOIN f.location l JOIN l.zone z " +
            "GROUP BY z.name, f.type")
    List<ZoneExtinguisherStatsResponse> getAdvancedStats();
}