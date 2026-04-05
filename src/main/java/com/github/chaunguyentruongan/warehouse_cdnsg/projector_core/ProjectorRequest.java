package com.github.chaunguyentruongan.warehouse_cdnsg.projector_core;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectorRequest {
    private String name;
    private String serialNumber;
    private ProjectorStatus status;
}