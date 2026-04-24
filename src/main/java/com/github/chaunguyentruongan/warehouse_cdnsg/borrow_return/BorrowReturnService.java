package com.github.chaunguyentruongan.warehouse_cdnsg.borrow_return;

import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BorrowReturnService {

    private final BorrowTicketRepository ticketRepository;
    private final JavaMailSender mailSender; // Xóa MaterialRepository

    @Transactional
    public BorrowTicket createTicket(BorrowRequestDTO request) {
        BorrowTicket ticket = BorrowTicket.builder()
                .borrowCode("M-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .items(request.getItems()) // Map danh sách items vào đây
                .borrowerName(request.getBorrowerName())
                .department(request.getDepartment())
                .status(TicketStatus.NEW)
                .createdAt(LocalDateTime.now())
                .deleted(false)
                .build();
        return ticketRepository.save(ticket);
    }

    @Transactional
    public BorrowTicket confirmBorrow(ConfirmBorrowDTO request) {
        BorrowTicket ticket = ticketRepository.findByBorrowCode(request.getBorrowCode())
                .orElseThrow(() -> new RuntimeException("Mã phiếu mượn không tồn tại"));

        if (ticket.getStatus() != TicketStatus.NEW) {
            throw new RuntimeException("Phiếu này đã được xác nhận mượn trước đó");
        }

        ticket.setEmail(request.getEmail());
        ticket.setStatus(TicketStatus.BORROWED);
        ticket.setBorrowTime(LocalDateTime.now());
        ticket.setReturnCode("T-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        BorrowTicket savedTicket = ticketRepository.save(ticket);
        sendHtmlEmail(savedTicket, "BORROW", true);

        return savedTicket;
    }

    public BorrowTicket getTicketByReturnCode(String returnCode) {
        return ticketRepository.findByReturnCode(returnCode)
                .orElseThrow(() -> new RuntimeException("Mã phiếu trả không tồn tại hoặc đã bị xóa"));
    }

    @Transactional
    public BorrowTicket confirmReturn(ConfirmReturnDTO request) {
        BorrowTicket ticket = ticketRepository.findByReturnCode(request.getReturnCode())
                .orElseThrow(() -> new RuntimeException("Mã phiếu trả không hợp lệ"));

        if (ticket.getStatus() != TicketStatus.BORROWED) {
            throw new RuntimeException("Trạng thái phiếu không hợp lệ để trả");
        }

        ticket.setReturnTime(LocalDateTime.now());

        if (request.isEnough()) {
            ticket.setStatus(TicketStatus.COMPLETED);
            // Đã xóa logic cộng lại tồn kho material
            sendHtmlEmail(ticket, "RETURN", true);
        } else {
            ticket.setStatus(TicketStatus.INCOMPLETE);
            ticket.setNote(request.getNote());
            sendHtmlEmail(ticket, "RETURN", false);
        }

        return ticketRepository.save(ticket);
    }

    @Transactional
    public BorrowTicket resolveIncomplete(Long ticketId) {
        BorrowTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu"));

        if (ticket.getStatus() != TicketStatus.INCOMPLETE) {
            throw new RuntimeException("Phiếu này không ở trạng thái báo thiếu");
        }

        ticket.setStatus(TicketStatus.COMPLETED);
        ticket.setNote(ticket.getNote() + " | [Hệ thống]: Đã thu hồi đủ đồ bổ sung.");


        sendHtmlEmail(ticket, "RETURN", true);
        return ticketRepository.save(ticket);
    }

    public Page<BorrowTicket> findAll(String keyword, TicketStatus status, Pageable pageable) {
        return ticketRepository.searchWithFilter(keyword, status, pageable);
    }

    private void sendHtmlEmail(BorrowTicket ticket, String type, boolean isEnough) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setTo(ticket.getEmail());

            String subject;
            String title;
            String statusText;
            String qrContent;
            String extraNote = "";

            if (type.equals("BORROW")) {
                subject = "Xác nhận mượn vật tư - " + ticket.getBorrowCode();
                title = "XÁC NHẬN MƯỢN VẬT TƯ";
                statusText = "Bạn đã xác nhận mượn vật phẩm thành công. Vui lòng giữ Mã Trả Đồ (QR) bên dưới để hoàn trả sau khi sử dụng xong.";
                qrContent = ticket.getReturnCode();
            } else {
                if (isEnough) {
                    subject = "Hoàn tất trả vật tư - " + ticket.getReturnCode();
                    title = "HOÀN TẤT TRẢ VẬT TƯ";
                    statusText = "Cảm ơn bạn, hệ thống ghi nhận bạn đã hoàn tất việc trả vật phẩm đầy đủ và nguyên vẹn.";
                    qrContent = ticket.getReturnCode();
                } else {
                    subject = "Cảnh báo: Trả thiếu/Hư hỏng vật tư - " + ticket.getReturnCode();
                    title = "CẢNH BÁO: TRẢ THIẾU VẬT TƯ";
                    statusText = "Hệ thống ghi nhận bạn đã trả vật phẩm nhưng bị THIẾU hoặc HƯ HỎNG. Vui lòng liên hệ thủ kho để bổ sung/xử lý sớm nhất.";
                    extraNote = "<div style='background-color: #fee2e2; padding: 12px; border-left: 4px solid #ef4444; margin-bottom: 15px;'><strong style='color: #b91c1c;'>Ghi chú từ thủ kho:</strong> <span style='color: #7f1d1d;'>"
                            + ticket.getNote() + "</span></div>";
                    qrContent = ticket.getReturnCode();
                }
            }

            helper.setSubject(subject);
            String qrUrl = "https://api.qrserver.com/v1/create-qr-code/?size=180x180&data=" + qrContent;
            String itemsStr = ticket.getItems().stream()
                    .map(i -> i.getItemName() + " (x" + i.getQuantity() + ")")
                    .collect(java.util.stream.Collectors.joining(", "));
            String htmlContent = String.format(
                    "<div style='font-family: Arial, Helvetica, sans-serif; max-width: 600px; margin: auto; border: 1px solid #e2e8f0; border-radius: 16px; overflow: hidden; box-shadow: 0 4px 6px rgba(0,0,0,0.05);'>"
                            + "  <div style='background-color: #1a237e; padding: 25px; text-align: center; color: white;'>"
                            + "    <h2 style='margin: 0; font-size: 20px; letter-spacing: 1px;'>%s</h2>"
                            + "  </div>"
                            + "  <div style='padding: 30px; line-height: 1.6; color: #334155;'>"
                            + "    <p style='font-size: 16px;'>Xin chào <strong>%s</strong>,</p>"
                            + "    <p style='font-size: 15px;'>%s</p>"
                            + "    %s"
                            + "    <div style='background-color: #f8fafc; border: 1px solid #e2e8f0; border-radius: 12px; padding: 20px; margin-top: 20px;'>"
                            + "      <table style='width: 100%%; border-collapse: collapse;'>"
                            + "        <tr><td style='padding: 10px 0; border-bottom: 1px dashed #cbd5e1; color: #64748b;'>Vật phẩm mượn:</td><td style='text-align: right; font-weight: bold; font-size: 15px;'>%s</td></tr>"
                            + "        <tr><td style='padding: 10px 0; border-bottom: 1px dashed #cbd5e1; color: #64748b;'>Số lượng:</td><td style='text-align: right; font-weight: bold; font-size: 15px; color: #4338ca;'>%d</td></tr>"
                            + "        <tr><td style='padding: 10px 0; color: #64748b;'>Mã giao dịch:</td><td style='text-align: right; font-weight: 900; font-size: 16px; color: #1a237e;'>%s</td></tr>"
                            + "      </table>"
                            + "    </div>"
                            + "    <div style='text-align: center; margin-top: 30px;'>"
                            + "      <p style='font-size: 13px; color: #94a3b8; margin-bottom: 12px; text-transform: uppercase; font-weight: bold; letter-spacing: 0.5px;'>Mã QR xác thực của bạn</p>"
                            + "      <img src='%s' alt='QR Code' style='border: 1px solid #e2e8f0; border-radius: 12px; padding: 10px; background: white;'/>"
                            + "    </div>"
                            + "  </div>"
                            + "</div>",
                    title, ticket.getBorrowerName(), statusText, extraNote,
                    itemsStr, // Thay ticket.getMaterial().getName() bằng ticket.getItemName()
                    ticket.getItems().size(), qrContent, qrUrl);

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (Exception e) {
            System.out.println("Lỗi gửi mail: " + e.getMessage());
        }
    }

    @Transactional
    public BorrowTicket updateTicket(Long id, UpdateBorrowTicketDTO request) {
        BorrowTicket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy phiếu"));

        if (request.getBorrowerName() != null)
            ticket.setBorrowerName(request.getBorrowerName());
        if (request.getDepartment() != null)
            ticket.setDepartment(request.getDepartment());
        if (request.getNote() != null)
            ticket.setNote(request.getNote());
        if (request.getStatus() != null)
            ticket.setStatus(request.getStatus());

        switch (request.getStatus()) {
            case COMPLETED:
                sendHtmlEmail(ticket, "RETURN", true);
                break;
            case INCOMPLETE:
                sendHtmlEmail(ticket, "RETURN", false);
                break;
        }

        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Long id){
        ticketRepository.deleteById(id);
    }
}