// File: TicketStatus.java
package com.github.chaunguyentruongan.warehouse_cdnsg.borrow_return;

public enum TicketStatus {
    NEW, // Mới tạo (Chờ người dùng quét và nhập mail)
    BORROWED, // Đang cho mượn (Đã nhập mail xác nhận)
    COMPLETED, // Đã trả đủ đồ
    INCOMPLETE // Trả thiếu/hư hỏng đồ
}