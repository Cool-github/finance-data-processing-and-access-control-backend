package com.finance.dashboard.controller;

import com.finance.dashboard.dto.ApiResponse;
import com.finance.dashboard.dto.FinancialRecordRequestDTO;
import com.finance.dashboard.dto.FinancialRecordResponseDTO;
import com.finance.dashboard.service.FinancialRecordService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Pageable;

@RestController
@RequestMapping("/records")
@RequiredArgsConstructor
public class FinancialRecordController {

    private final FinancialRecordService service;

    @PostMapping
    public ApiResponse<FinancialRecordResponseDTO> create(@RequestBody FinancialRecordRequestDTO request) {
        return ApiResponse.<FinancialRecordResponseDTO>builder()
                .success(true)
                .message("Record created")
                .data(service.createRecord(request))
                .build();
    }

    @GetMapping
    public ApiResponse<Page<FinancialRecordResponseDTO>> get(Pageable pageable) {
        return ApiResponse.<Page<FinancialRecordResponseDTO>>builder()
                .success(true)
                .message("Records fetched")
                .data(service.getRecords(pageable))
                .build();
    }
}