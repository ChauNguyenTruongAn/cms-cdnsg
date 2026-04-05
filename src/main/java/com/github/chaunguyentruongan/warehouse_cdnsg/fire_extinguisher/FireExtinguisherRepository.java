package com.github.chaunguyentruongan.warehouse_cdnsg.fire_extinguisher;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface FireExtinguisherRepository extends JpaRepository<FireExtinguisher, Long> {

    @Query("SELECT f FROM FireExtinguisher f WHERE " +
            "LOWER(f.location) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
            "LOWER(f.type) LIKE LOWER(CONCAT('%', :kw, '%'))")
    Page<FireExtinguisher> search(@Param("kw") String kw, Pageable pageable);

    long countByStatus(MaintenanceStatus status);
}