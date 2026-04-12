package com.github.chaunguyentruongan.warehouse_cdnsg.borrow_return;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BorrowTicketRepository extends JpaRepository<BorrowTicket, Long> {

        Optional<BorrowTicket> findByBorrowCode(String borrowCode);

        Optional<BorrowTicket> findByReturnCode(String returnCode);

        // Đã sửa lại logic tìm kiếm trong danh sách items bằng EXISTS
        @Query("SELECT t FROM BorrowTicket t WHERE " +
                        "(:status IS NULL OR t.status = :status) AND " +
                        "(:kw IS NULL OR :kw = '' OR " +
                        "LOWER(t.borrowerName) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
                        "LOWER(t.borrowCode) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
                        "LOWER(t.returnCode) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
                        "EXISTS (SELECT 1 FROM t.items item WHERE LOWER(item.itemName) LIKE LOWER(CONCAT('%', :kw, '%'))))")
        Page<BorrowTicket> searchWithFilter(
                        @Param("kw") String kw,
                        @Param("status") TicketStatus status,
                        Pageable pageable);
}