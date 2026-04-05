package com.github.chaunguyentruongan.warehouse_cdnsg.projector_loan;

import com.github.chaunguyentruongan.warehouse_cdnsg.exception.ResourceNotFoundException;
import com.github.chaunguyentruongan.warehouse_cdnsg.projector_core.Projector;
import com.github.chaunguyentruongan.warehouse_cdnsg.projector_core.ProjectorRepository;
import com.github.chaunguyentruongan.warehouse_cdnsg.projector_core.ProjectorStatus;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ProjectorLoanService {
    private final ProjectorLoanRepository loanRepository;
    private final ProjectorRepository projectorRepository;

    // NGHIỆP VỤ MƯỢN MÁY CHIẾU
    @Transactional
    public ProjectorLoan borrowProjector(ProjectorLoanRequest request) {
        // 1. Tìm và Khóa dòng máy chiếu lại (chống Race Condition)
        Projector projector = projectorRepository.findByIdWithLock(request.getProjectorId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy máy chiếu!"));

        // 2. Validate trạng thái
        if (projector.getStatus() != ProjectorStatus.AVAILABLE) {
            throw new IllegalArgumentException(
                    "Máy chiếu này không rảnh. Trạng thái hiện tại: " + projector.getStatus());
        }

        // 3. Đổi trạng thái máy chiếu thành ĐANG MƯỢN
        projector.setStatus(ProjectorStatus.BORROWED);
        projectorRepository.save(projector);

        // 4. Tạo phiếu mượn (Nhật ký)
        ProjectorLoan loan = ProjectorLoan.builder()
                .projector(projector)
                .borrower(request.getBorrower())
                .borrowDate(request.getBorrowDate() != null ? request.getBorrowDate() : LocalDate.now())
                .status(LoanStatus.BORROWING)
                .note(request.getNote())
                .build();

        return loanRepository.save(loan);
    }

    // NGHIỆP VỤ TRẢ MÁY CHIẾU
    @Transactional
    public ProjectorLoan returnProjector(Long loanId, String returnNote) {
        // 1. Tìm phiếu mượn
        ProjectorLoan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy phiếu mượn!"));

        if (loan.getStatus() == LoanStatus.RETURNED) {
            throw new IllegalArgumentException("Phiếu này đã được hoàn tất trả máy từ trước!");
        }

        // 2. Cập nhật phiếu mượn
        loan.setStatus(LoanStatus.RETURNED);
        loan.setReturnDate(LocalDate.now());
        loan.setNote(loan.getNote() + " | Ghi chú khi trả: " + returnNote);

        // 3. Trả lại trạng thái RẢNH cho máy chiếu
        Projector projector = loan.getProjector();
        projector.setStatus(ProjectorStatus.AVAILABLE);
        projectorRepository.save(projector);

        return loanRepository.save(loan);
    }

    public Page<ProjectorLoan> getAllLoans(Pageable pageable) {
        return loanRepository.findAll(pageable); // Spring Data JPA đã có sẵn hàm findAll(Pageable)
    }
}