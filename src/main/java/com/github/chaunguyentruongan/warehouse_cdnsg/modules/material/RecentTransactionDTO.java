package com.github.chaunguyentruongan.warehouse_cdnsg.modules.material;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecentTransactionDTO {
    private Long materialId;
    private String materialName;
    private String unitName;
    private int quantity;
    private LocalDate transactionDate; // Ngày nhập/xuất
    private String receiptCode; // Mã phiếu
    private String type; // "IMPORT" hoặc "EXPORT"
}