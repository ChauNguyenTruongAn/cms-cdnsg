package com.github.chaunguyentruongan.warehouse_cdnsg.export_receipt;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExportReceiptRepository extends JpaRepository<ExportReceipt, Long> {

    @Query("SELECT e FROM ExportReceipt e WHERE " +
            "(:fromDate IS NULL OR e.exportDate >= :fromDate) AND " +
            "(:toDate IS NULL OR e.exportDate <= :toDate) AND " +
            "(:note IS NULL OR LOWER(e.note) LIKE LOWER(CONCAT('%', :note, '%'))) AND " +
            "(:department IS NULL OR LOWER(e.department) LIKE LOWER(CONCAT('%', :department, '%')))")
    Page<ExportReceipt> searchAndFilter(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("note") String note,
            @Param("department") String department,
            Pageable pageable);
}