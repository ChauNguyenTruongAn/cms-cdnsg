package com.github.chaunguyentruongan.warehouse_cdnsg.fire_extinguisher;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "fire_extinguisher")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FireExtinguisher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String location; // Vị trí (VD: Hành lang lầu 1, Phòng máy tính...)

    @Column(nullable = false)
    private String type; // Loại bình (VD: Bột BC, CO2, Foam...)

    private String weight; // Trọng lượng (VD: 4kg, 8kg...)

    private Integer quantity; // Số lượng tại vị trí đó

    private LocalDate lastRechargeDate; // Ngày nạp gần nhất

    private LocalDate nextRechargeDate; // Ngày nạp tiếp theo (Dự kiến)

    // Trạng thái để nhắc nhở (VD: OK, SẮP HẾT HẠN, QUÁ HẠN)
    @Enumerated(EnumType.STRING)
    private MaintenanceStatus status;
}

enum MaintenanceStatus {
    OK, WARNING, EXPIRED
}