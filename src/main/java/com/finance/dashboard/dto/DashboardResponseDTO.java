package com.finance.dashboard.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Map;
import java.util.List;

@Data
@Builder
public class DashboardResponseDTO {

    private Double totalIncome;
    private Double totalExpense;
    private Double netBalance;

    private Map<String, Double> categorySummary;

    private List<FinancialRecordResponseDTO> recentTransactions;
}