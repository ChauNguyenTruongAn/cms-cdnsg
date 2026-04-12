package com.github.chaunguyentruongan.warehouse_cdnsg.borrow_return;

import java.util.List;
import lombok.Data;

@Data
public class BorrowRequestDTO {
    private List<BorrowTicketItem> items; // Nhận một mảng các món đồ
    private String borrowerName;
    private String department;
}