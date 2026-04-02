package com.finance.dashboard.service;

import com.finance.dashboard.dto.FinancialRecordRequestDTO;
import com.finance.dashboard.dto.FinancialRecordResponseDTO;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface FinancialRecordService {

    FinancialRecordResponseDTO createRecord(FinancialRecordRequestDTO request);

    Page<FinancialRecordResponseDTO> getRecords(Pageable pageable);
}