package com.github.chaunguyentruongan.warehouse_cdnsg.fire_extinguisher;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FireExtinguisherRequest {
    private String location;
    private String type;
    private String weight;
    private Integer quantity;
}