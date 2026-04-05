package com.github.chaunguyentruongan.warehouse_cdnsg.fire_extinguisher;

import java.time.LocalDate;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FireExtinguisherResponse {
    private Long id;
    private String location;
    private String type;
    private String weight;
    private Integer quantity;
    private LocalDate lastRechargeDate;
    private LocalDate nextRechargeDate;
    private String status; // OK, WARNING, EXPIRED
}