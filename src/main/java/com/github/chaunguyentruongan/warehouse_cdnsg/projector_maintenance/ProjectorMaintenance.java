package com.github.chaunguyentruongan.warehouse_cdnsg.projector_maintenance;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.github.chaunguyentruongan.warehouse_cdnsg.projector_core.Projector;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Table(name = "projector_maintenance")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProjectorMaintenance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "projector_id", nullable = false)
    @JsonBackReference
    private Projector projector;

    @Column(nullable = false)
    private LocalDate maintenanceDate;

    // Chi tiết bảo trì (VD: "Thay bóng đèn chiếu", "Vệ sinh ống kính")
    @Column(nullable = false)
    private String description;

    // Người thực hiện bảo trì (VD: "IT Support", "Công ty ngoài")
    private String technician;

    // Chi phí sửa chữa (nếu có)
    private Double cost;
}