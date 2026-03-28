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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UniformType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UniformSize size;

    private Long stock;
}