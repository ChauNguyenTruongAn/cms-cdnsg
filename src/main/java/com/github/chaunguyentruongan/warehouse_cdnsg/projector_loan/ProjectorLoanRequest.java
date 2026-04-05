package com.github.chaunguyentruongan.warehouse_cdnsg.projector_loan;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class ProjectorLoanRequest {
    private Long projectorId;
    private String borrower;
    private LocalDate borrowDate;
    private String note;
}