package com.github.chaunguyentruongan.warehouse_cdnsg.projector_maintenance;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "maintenance_ticket")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class MaintenanceTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String ticketCode; // Mã phiếu (VD: BT-2024-001)

    @Column(nullable = false)
    private LocalDate startDate; // Ngày bắt đầu bảo trì

    private LocalDate completionDate; // Ngày hoàn tất

    private String technician; // Người thực hiện

    private String generalNote;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("ticket")
    @Builder.Default
    private List<ProjectorMaintenanceDetail> details = new ArrayList<>();
}