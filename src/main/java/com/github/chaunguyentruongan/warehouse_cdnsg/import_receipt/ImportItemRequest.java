package com.github.chaunguyentruongan.warehouse_cdnsg.import_receipt;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImportItemRequest {
    private Long materialId;
    private Integer quantity;
}
