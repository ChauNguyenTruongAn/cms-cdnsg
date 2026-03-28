package com.github.chaunguyentruongan.warehouse_cdnsg.export_receipt;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "export_receipt")
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExportReceipt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate exportDate;
    private String note;
    private String department;
    private String recipient;

    @OneToMany(mappedBy = "exportReceipt", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ExportItem> exportItems = new ArrayList<>();
}