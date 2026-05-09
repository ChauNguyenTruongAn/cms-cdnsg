package com.github.chaunguyentruongan.warehouse_cdnsg.modules.borrow_return;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/borrow-return/user")
@RequiredArgsConstructor
@Tag(name = "User Borrow & Return", description = "Các API dành riêng cho user để xem lịch sử và dashboard cá nhân")
public class UserBorrowController {

    private final UserBorrowService userBorrowService;

    @Operation(summary = "Xem Dashboard cá nhân", description = "Lấy tổng số lần mượn, số vật phẩm đang cầm, số lần quá hạn dựa theo email")
    @GetMapping("/dashboard")
    public ResponseEntity<Map<String, Object>> getUserDashboard(@RequestParam String email) {
        return ResponseEntity.ok(userBorrowService.getUserDashboard(email));
    }

    @Operation(summary = "Xem lịch sử mượn trả của user", description = "Lọc theo email và status")
    @GetMapping("/history")
    public ResponseEntity<Page<BorrowTicket>> getUserHistory(
            @RequestParam String email,
            @RequestParam(required = false) TicketStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(userBorrowService.getUserHistory(email, status, PageRequest.of(page, size, Sort.by("createdAt").descending())));
    }
}
