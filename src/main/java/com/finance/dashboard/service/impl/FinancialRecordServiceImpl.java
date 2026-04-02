package com.finance.dashboard.service.impl;

import com.finance.dashboard.dto.FinancialRecordRequestDTO;
import com.finance.dashboard.dto.FinancialRecordResponseDTO;
import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.repository.FinancialRecordRepository;
import com.finance.dashboard.repository.UserRepository;
import com.finance.dashboard.service.FinancialRecordService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FinancialRecordServiceImpl implements FinancialRecordService {

    private static final Logger log = LoggerFactory.getLogger(FinancialRecordServiceImpl.class);

    private final FinancialRecordRepository repository;
    private final UserRepository userRepository;

    @Override
    public FinancialRecordResponseDTO createRecord(FinancialRecordRequestDTO request) {

        log.info("Creating financial record");

        // ⚠️ TEMP: Fetch first user (will replace with JWT user in next step)
        User user = userRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No user found"));

        FinancialRecord record = FinancialRecord.builder()
                .amount(request.getAmount())
                .type(request.getType())
                .category(request.getCategory())
                .date(request.getDate())
                .description(request.getDescription())
                .user(user)
                .isDeleted(false)
                .deletedAt(null)
                .build();

        repository.save(record);

        log.info("Financial record created with id: {}", record.getId());

        return mapToResponse(record);
    }

    @Override
    public Page<FinancialRecordResponseDTO> getRecords(Pageable pageable) {

        log.info("Fetching financial records with pagination");

        // ⚠️ TEMP: Fetch first user (will replace with JWT user in next step)
        User user = userRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No user found"));

        return repository
                .findByUserIdAndIsDeletedFalse(user.getId(), pageable)
                .map(this::mapToResponse);
    }

    // 🔁 Mapper
    private FinancialRecordResponseDTO mapToResponse(FinancialRecord record) {
        return FinancialRecordResponseDTO.builder()
                .id(record.getId())
                .amount(record.getAmount())
                .type(record.getType())
                .category(record.getCategory())
                .date(record.getDate())
                .description(record.getDescription())
                .build();
    }
}