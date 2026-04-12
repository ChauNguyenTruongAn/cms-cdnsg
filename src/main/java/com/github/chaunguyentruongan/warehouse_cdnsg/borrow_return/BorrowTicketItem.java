package com.github.chaunguyentruongan.warehouse_cdnsg.borrow_return;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BorrowTicketItem {
    private String itemName;
    private int quantity;
}