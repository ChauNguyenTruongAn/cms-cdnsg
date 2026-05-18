package com.github.chaunguyentruongan.warehouse_cdnsg.modules.export_receipt;

import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.github.chaunguyentruongan.warehouse_cdnsg.modules.material.RecentTransactionDTO;

public interface ExportItemRepository extends JpaRepository<ExportItem, Long> {

    @Query("SELECT new com.github.chaunguyentruongan.warehouse_cdnsg.modules.material.RecentTransactionDTO(" +
            "m.id, m.name, u.name, ei.quantity, r.exportDate, r.receiptCode, 'EXPORT') " +
            "FROM ExportItem ei " +
            "JOIN ei.material m " +
            "LEFT JOIN m.unit u " +
            "JOIN ei.exportReceipt r " +
            "WHERE r.status = com.github.chaunguyentruongan.warehouse_cdnsg.enums.ReceiptStatus.COMPLETED " +
            "ORDER BY r.exportDate DESC, r.id DESC")
    List<RecentTransactionDTO> findRecentExports(Pageable pageable);
}