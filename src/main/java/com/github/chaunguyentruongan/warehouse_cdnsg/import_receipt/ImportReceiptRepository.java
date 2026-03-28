package com.github.chaunguyentruongan.warehouse_cdnsg.import_receipt;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface ImportReceiptRepository extends JpaRepository<ImportReceipt, Long> {

    List<ImportReceipt> findByImportDateBetween(LocalDate from, LocalDate to);


    // Truy vấn kết hợp Lọc theo ngày, Tìm kiếm theo Note và Phân trang
    @Query("SELECT i FROM ImportReceipt i WHERE " +
           "(:fromDate IS NULL OR i.importDate >= :fromDate) AND " +
           "(:toDate IS NULL OR i.importDate <= :toDate) AND " +
           "(:note IS NULL OR LOWER(i.note) LIKE LOWER(CONCAT('%', :note, '%')))")
    Page<ImportReceipt> searchAndFilter(
            @Param("fromDate") LocalDate fromDate,
            @Param("toDate") LocalDate toDate,
            @Param("note") String note,
            Pageable pageable);
}
