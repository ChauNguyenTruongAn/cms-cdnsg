package com.github.chaunguyentruongan.warehouse_cdnsg.projector_loan;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/projector-loans")
@RequiredArgsConstructor
@Tag(name = "Quản lý Mượn/Trả Máy chiếu", description = "Nghiệp vụ mượn, trả và xem lịch sử")
public class ProjectorLoanController {

    private final ProjectorLoanService loanService;

    @Operation(summary = "Mượn máy chiếu", description = "Máy chiếu phải đang ở trạng thái AVAILABLE")
    @PostMapping("/borrow")
    public ResponseEntity<ProjectorLoan> borrowProjector(@RequestBody ProjectorLoanRequest request) {
        return ResponseEntity.ok(loanService.borrowProjector(request));
    }

    @Operation(summary = "Trả máy chiếu", description = "Truyền ID của phiếu mượn để hoàn tất việc trả")
    @PostMapping("/{loanId}/return")
    public ResponseEntity<ProjectorLoan> returnProjector(
            @PathVariable Long loanId,
            @RequestParam(required = false, defaultValue = "") String returnNote) {
        return ResponseEntity.ok(loanService.returnProjector(loanId, returnNote));
    }

    @Operation(summary = "Lấy danh sách mượn/trả", description = "Xem lịch sử mượn trả của tất cả máy chiếu")
    @GetMapping
    public ResponseEntity<Page<ProjectorLoan>> getAllLoans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(loanService.getAllLoans(pageable));
    }
}