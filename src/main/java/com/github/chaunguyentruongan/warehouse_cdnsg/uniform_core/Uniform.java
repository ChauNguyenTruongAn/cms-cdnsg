package com.github.chaunguyentruongan.warehouse_cdnsg.uniform_core;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "uniform", uniqueConstraints = {
        // Đảm bảo không có 2 dòng nào trùng cả Type và Size
        @UniqueConstraint(columnNames = { "type", "size" })
})
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Uniform {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private String size;

    private Long stock;
}