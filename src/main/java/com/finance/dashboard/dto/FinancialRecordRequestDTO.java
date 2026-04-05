package com.finance.dashboard.dto;

import com.finance.dashboard.enums.Category;
import com.finance.dashboard.enums.RecordType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FinancialRecordRequestDTO {

    @NotNull
    @Positive
    private Double amount;

    @NotNull
    private RecordType type;

    @NotNull
    private Category category;

    @NotNull
    private LocalDate date;

    private String description;
}