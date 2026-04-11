// File: BorrowTicket.java
package com.github.chaunguyentruongan.warehouse_cdnsg.borrow_return;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.chaunguyentruongan.warehouse_cdnsg.material.Material;
import jakarta.persistence.*;
import lombok.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "borrow_ticket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLDelete(sql = "UPDATE borrow_ticket SET deleted = true WHERE id=?")
@Where(clause = "deleted = false")
public class BorrowTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String borrowCode; // Mã mượn

    @Column(unique = true)
    private String returnCode; // Mã trả (Chỉ tạo ra khi đã xác nhận mượn)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @NotFound(action = NotFoundAction.IGNORE)
    private Material material; // Liên kết tới Vật tư

    private int quantity; // Số lượng mượn
    private String borrowerName; // Tên người mượn
    private String department; // Phòng ban
    private String email; // Email (Cập nhật khi quét mã)

    @Enumerated(EnumType.STRING)
    private TicketStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime borrowTime; // Thời điểm xác nhận mượn
    private LocalDateTime returnTime; // Thời điểm thủ kho xác nhận trả

    @Column(columnDefinition = "TEXT")
    private String note; // Ghi chú (nếu thiếu/hư hỏng)

    @Column(nullable = false)
    private boolean deleted = false;
}