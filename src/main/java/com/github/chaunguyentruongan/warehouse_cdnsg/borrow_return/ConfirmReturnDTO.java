package com.github.chaunguyentruongan.warehouse_cdnsg.borrow_return;

import lombok.Data;

// File: ConfirmReturnDTO.java
@Data
public class ConfirmReturnDTO {
    private String returnCode;
    private boolean isEnough; // true: Đủ đồ, false: Không đủ
    private String note; // Bắt buộc nếu isEnough = false
}