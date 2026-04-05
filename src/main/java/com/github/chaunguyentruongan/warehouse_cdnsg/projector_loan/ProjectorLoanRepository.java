package com.github.chaunguyentruongan.warehouse_cdnsg.projector_loan;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectorLoanRepository extends JpaRepository<ProjectorLoan, Long> {
    // Truy xuất toàn bộ lịch sử mượn của 1 máy chiếu cụ thể
    List<ProjectorLoan> findByProjectorIdOrderByBorrowDateDesc(Long projectorId);

    // Tìm các phiếu đang mượn (chưa trả)
    Page<ProjectorLoan> findByStatus(LoanStatus status, Pageable pageable);
}