package com.finance.dashboard.dto;

import com.finance.dashboard.enums.Category;
import com.finance.dashboard.enums.RecordType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class FinancialRecordResponseDTO {
    private UUID id;
    private Double amount;
    private RecordType type;
    private Category category;
    private LocalDate date;
    private String description;
}