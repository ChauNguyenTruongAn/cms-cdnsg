package com.github.chaunguyentruongan.warehouse_cdnsg.projector_core;

public enum ProjectorStatus {
    AVAILABLE, // Sẵn sàng cho mượn
    BORROWED, // Đang được mượn
    UNDER_MAINTENANCE, // Đang bảo trì/sửa chữa
    BROKEN // Đã hỏng, không thể sử dụng
}