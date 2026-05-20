package com.github.chaunguyentruongan.warehouse_cdnsg.modules.import_receipt;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.github.chaunguyentruongan.warehouse_cdnsg.modules.material.RecentTransactionDTO;

public interface ImportItemRepository extends JpaRepository<ImportItem, Long> {

    @Query("SELECT new com.github.chaunguyentruongan.warehouse_cdnsg.modules.material.RecentTransactionDTO(" +
            "m.id, m.name, u.name, ii.quantity, r.importDate, r.id, r.receiptCode, 'IMPORT') " +
            "FROM ImportItem ii " +
            "JOIN ii.material m " +
            "LEFT JOIN m.unit u " +
            "JOIN ii.importReceipt r " +
            "WHERE r.status = com.github.chaunguyentruongan.warehouse_cdnsg.enums.ReceiptStatus.COMPLETED " +
            "ORDER BY r.importDate DESC, r.id DESC")
    List<RecentTransactionDTO> findRecentImports(Pageable pageable);

    @Query("SELECT new com.github.chaunguyentruongan.warehouse_cdnsg.modules.material.RecentTransactionDTO(" +
            "m.id, m.name, u.name, ii.quantity, r.importDate, r.id, r.receiptCode, 'IMPORT') " +
            "FROM ImportItem ii " +
            "JOIN ii.material m " +
            "LEFT JOIN m.unit u " +
            "JOIN ii.importReceipt r " +
            "WHERE m.id = :materialId AND r.status = com.github.chaunguyentruongan.warehouse_cdnsg.enums.ReceiptStatus.COMPLETED "
            +
            "ORDER BY r.importDate DESC, r.id DESC")
    List<RecentTransactionDTO> findImportHistoryByMaterialId(@Param("materialId") Long materialId);
}