package com.github.chaunguyentruongan.warehouse_cdnsg.projector_loan;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class ProjectorLoanRequest {
    private List<Long> projectorIds; // THAY ĐỔI: Nhận nhiều máy chiếu cùng lúc
    private String borrower;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private String note;
}