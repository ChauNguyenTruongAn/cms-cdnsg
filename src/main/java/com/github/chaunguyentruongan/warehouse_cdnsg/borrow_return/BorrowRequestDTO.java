package com.github.chaunguyentruongan.warehouse_cdnsg.borrow_return;

import lombok.Data;

@Data
public class BorrowRequestDTO {
    private Long materialId;
    private int quantity;
    private String borrowerName;
    private String department;
}
