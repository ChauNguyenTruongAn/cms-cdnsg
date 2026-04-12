package com.github.chaunguyentruongan.warehouse_cdnsg.borrow_return;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/borrow-return")
@RequiredArgsConstructor
@Tag(name = "Borrow & Return Management", description = "Các API quản lý luồng mượn và trả vật tư bằng mã QR")
public class BorrowReturnController {

    private final BorrowReturnService service;

    @Operation(summary = "1. Tạo phiếu mượn vật tư", description = "Thủ kho tạo phiếu mượn mới độc lập không phụ thuộc vật tư có sẵn. API trả về thông tin phiếu kèm mã borrowCode để Frontend vẽ mã QR.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tạo phiếu thành công")
    })
    @PostMapping("/create")
    public ResponseEntity<BorrowTicket> createTicket(@RequestBody BorrowRequestDTO request) {
        return ResponseEntity.ok(service.createTicket(request));
    }

    @Operation(summary = "2. Người dùng xác nhận mượn", description = "Được gọi khi người dùng quét mã QR mượn, nhập email và bấm xác nhận. Hệ thống sẽ đổi trạng thái thành BORROWED và sinh ra returnCode gửi qua email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Xác nhận mượn thành công"),
            @ApiResponse(responseCode = "404", description = "Không tìm thấy mã phiếu mượn")
    })
    @PostMapping("/confirm-borrow")
    public ResponseEntity<BorrowTicket> confirmBorrow(@RequestBody ConfirmBorrowDTO request) {
        return ResponseEntity.ok(service.confirmBorrow(request));
    }

    @Operation(summary = "3. Lấy thông tin phiếu trả qua mã QR", description = "Được gọi khi thủ kho quét mã QR trả đồ. API trả về chi tiết phiếu để hiển thị lên màn hình kiểm tra trước khi xác nhận.")
    @Parameter(name = "returnCode", description = "Mã trả đồ (được gửi trong email của người mượn)", required = true)
    @GetMapping("/scan-return/{returnCode}")
    public ResponseEntity<BorrowTicket> getReturnTicketInfo(@PathVariable String returnCode) {
        // Gọi service để lấy thông tin (Bạn cần thêm hàm getTicketByReturnCode vào
        // BorrowReturnService)
        BorrowTicket ticket = service.getTicketByReturnCode(returnCode);
        return ResponseEntity.ok(ticket);
    }

    @Operation(summary = "4. Thủ kho xác nhận trả đồ", description = "Thủ kho sau khi kiểm tra hiện trạng vật tư sẽ chọn Đủ đồ (isEnough=true) hoặc Thiếu đồ (isEnough=false, kèm ghi chú).")
    @PostMapping("/confirm-return")
    public ResponseEntity<BorrowTicket> confirmReturn(@RequestBody ConfirmReturnDTO request) {
        return ResponseEntity.ok(service.confirmReturn(request));
    }

    @Operation(summary = "5. Admin xác nhận đã nhận đủ đồ bị thiếu", description = "Được gọi khi người dùng mang vật tư bị thiếu/hỏng đến trả bù. Hệ thống sẽ đổi từ INCOMPLETE sang COMPLETED và cộng lại tồn kho.")
    @PutMapping("/resolve-incomplete/{id}")
    public ResponseEntity<BorrowTicket> resolveIncompleteReturn(@PathVariable Long id) {
        return ResponseEntity.ok(service.resolveIncomplete(id));
    }

    @Operation(summary = "6. Lấy danh sách phiếu mượn trả", description = "Lấy toàn bộ danh sách phiếu có phân trang và lọc.")
    @GetMapping("/all")
    public ResponseEntity<Page<BorrowTicket>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword, // THÊM DÒNG NÀY
            @RequestParam(required = false) TicketStatus status) {

        // Truyền keyword vào service
        return ResponseEntity.ok(
                service.findAll(keyword, status, PageRequest.of(page, size, Sort.by("createdAt").descending())));
    }

    @Operation(summary = "7. Cập nhật thông tin phiếu", description = "Admin sửa thông tin (Tên, phòng ban, ghi chú, trạng thái).")
    @PutMapping("/{id}")
    public ResponseEntity<BorrowTicket> updateTicket(@PathVariable Long id,
            @RequestBody UpdateBorrowTicketDTO request) {
        return ResponseEntity.ok(service.updateTicket(id, request));
    }

    @Operation(summary = "8. Xóa phiếu mượn", description = "Admin xóa mềm phiếu mượn.")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        service.deleteTicket(id); // Bạn thêm hàm deleteTicket vào Service dùng ticketRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}