package com.github.chaunguyentruongan.warehouse_cdnsg.projector_core;

import jakarta.persistence.LockModeType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProjectorRepository extends JpaRepository<Projector, Long> {
    boolean existsBySerialNumber(String serialNumber);

    // Dùng để khóa dòng dữ liệu khi có thao tác mượn/trả
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Projector p WHERE p.id = :id")
    Optional<Projector> findByIdWithLock(@Param("id") Long id);

    // Tìm kiếm theo tên hoặc số serial
    Page<Projector> findByNameContainingIgnoreCaseOrSerialNumberContainingIgnoreCase(String name, String serialNumber,
            Pageable pageable);

    // Đếm số lượng máy chiếu theo trạng thái (Dùng cho báo cáo thống kê sau này)
    long countByStatus(ProjectorStatus status);
}