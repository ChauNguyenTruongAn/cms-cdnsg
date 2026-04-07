package com.github.chaunguyentruongan.warehouse_cdnsg.export_receipt;

import java.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ExportReceiptRepository extends JpaRepository<ExportReceipt, Long> {

        long countByExportDate(LocalDate exportDate);

        @Query("SELECT e FROM ExportReceipt e WHERE " +
                        "(:fromDate IS NULL OR e.exportDate >= :fromDate) AND " +
                        "(:toDate IS NULL OR e.exportDate <= :toDate) AND " +
                        "(:keyword IS NULL OR :keyword = '' OR " +
                        "LOWER(e.note) LIKE LOWER(CONCAT('%', :keyword, '%')) AND " +
                        "LOWER(e.department) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
                        "LOWER(e.receiptCode) LIKE LOWER(CONCAT('%', :keyword, '%')))")
        Page<ExportReceipt> searchAndFilter(
                        @Param("fromDate") LocalDate fromDate,
                        @Param("toDate") LocalDate toDate,
                        @Param("keyword") String keyword,
                        Pageable pageable);
}