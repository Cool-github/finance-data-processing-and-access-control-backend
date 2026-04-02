package com.finance.dashboard.dto;

import com.finance.dashboard.enums.Category;
import com.finance.dashboard.enums.RecordType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FinancialRecordRequestDTO {
    private Double amount;
    private RecordType type;
    private Category category;
    private LocalDate date;
    private String description;
}