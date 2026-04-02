package com.finance.dashboard.service;

import com.finance.dashboard.dto.DashboardResponseDTO;
import com.finance.dashboard.dto.FinancialRecordRequestDTO;
import com.finance.dashboard.dto.FinancialRecordResponseDTO;
import com.finance.dashboard.enums.Category;
import com.finance.dashboard.enums.RecordType;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FinancialRecordService {

    FinancialRecordResponseDTO createRecord(FinancialRecordRequestDTO request);

    Page<FinancialRecordResponseDTO> getRecords(
            RecordType type,
            Category category,
            Pageable pageable
    );

    DashboardResponseDTO getDashboard();

    void deleteRecord(UUID id);

    FinancialRecordResponseDTO updateRecord(UUID id, FinancialRecordRequestDTO request);
}