package com.github.chaunguyentruongan.warehouse_cdnsg.uniform_core;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UniformRepository extends JpaRepository<Uniform, Long> {

    // Khóa bi quan chống đụng độ (Race Condition) khi nhiều người thao tác kho
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT u FROM Uniform u WHERE u.id = :id")
    Optional<Uniform> findByIdWithLock(@Param("id") Long id);
}