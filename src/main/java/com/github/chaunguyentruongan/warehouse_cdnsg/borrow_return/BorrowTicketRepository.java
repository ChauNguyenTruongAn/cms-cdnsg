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

        @Query("SELECT t FROM BorrowTicket t LEFT JOIN FETCH t.material WHERE " +
                        "(:kw IS NULL OR :kw = '' OR " +
                        "LOWER(t.borrowerName) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
                        "LOWER(t.borrowCode) LIKE LOWER(CONCAT('%', :kw, '%')) OR " +
                        "LOWER(t.returnCode) LIKE LOWER(CONCAT('%', :kw, '%'))) " +
                        "AND (:status IS NULL OR t.status = :status)")
        Page<BorrowTicket> searchWithFilter(@Param("kw") String kw, @Param("status") TicketStatus status,
                        Pageable pageable);
}