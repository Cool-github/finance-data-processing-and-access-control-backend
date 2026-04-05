package com.finance.dashboard.service.impl;

import com.finance.dashboard.dto.DashboardResponseDTO;
import com.finance.dashboard.dto.FinancialRecordRequestDTO;
import com.finance.dashboard.dto.FinancialRecordResponseDTO;
import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.entity.User;
import com.finance.dashboard.enums.Category;
import com.finance.dashboard.enums.RecordType;
import com.finance.dashboard.exception.ResourceNotFoundException;
import com.finance.dashboard.exception.UnauthorizedException;
import com.finance.dashboard.repository.FinancialRecordRepository;
import com.finance.dashboard.repository.UserRepository;
import com.finance.dashboard.service.FinancialRecordService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FinancialRecordServiceImpl implements FinancialRecordService {

    private static final Logger log = LoggerFactory.getLogger(FinancialRecordServiceImpl.class);

    private final FinancialRecordRepository repository;
    private final UserRepository userRepository;

    @Override
    public FinancialRecordResponseDTO createRecord(FinancialRecordRequestDTO request) {

        log.info("Creating financial record");

        UUID uid = getCurrentUserId();

        User user = userRepository.findById(uid)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

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
    public Page<FinancialRecordResponseDTO> getRecords(
            RecordType type,
            Category category,
            Pageable pageable) {

        log.info("Fetching financial records with pagination");

        UUID uid = getCurrentUserId();

        if (type != null && category != null) {
            return repository
                    .findByUserIdAndTypeAndCategoryAndIsDeletedFalse(uid, type, category, pageable)
                    .map(this::mapToResponse);
        } else if (type != null) {
            return repository
                    .findByUserIdAndTypeAndIsDeletedFalse(uid, type, pageable)
                    .map(this::mapToResponse);
        } else if (category != null) {
            return repository
                    .findByUserIdAndCategoryAndIsDeletedFalse(uid, category, pageable)
                    .map(this::mapToResponse);
        } else {
            return repository
                    .findByUserIdAndIsDeletedFalse(uid, pageable)
                    .map(this::mapToResponse);
        }
    }

    // Mapper
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

    private UUID getCurrentUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            throw new UnauthorizedException("User not authenticated");
        }

        return UUID.fromString(auth.getName());
    }

    @Override
    public DashboardResponseDTO getDashboard() {

        UUID uid = getCurrentUserId();

        Double income = repository.getTotalIncome(uid);
        Double expense = repository.getTotalExpense(uid);

        Double net = income - expense;

        var categoryData = repository.getCategorySummary(uid);
        Map<String, Double> categoryMap = new HashMap<>();

        for (Object[] row : categoryData) {
            categoryMap.put(row[0].toString(), (Double) row[1]);
        }

        // Recent Transactions
        var recent = repository
                .findTop5ByUserIdAndIsDeletedFalseOrderByDateDesc(uid)
                .stream()
                .map(this::mapToResponse)
                .toList();

        return DashboardResponseDTO.builder()
                .totalIncome(income)
                .totalExpense(expense)
                .netBalance(net)
                .categorySummary(categoryMap)
                .recentTransactions(recent)
                .build();
    }

    @Override
    public void deleteRecord(UUID id) {

        log.info("Updating record: {}", id);

        UUID uid = getCurrentUserId();

        FinancialRecord record = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found"));

        if (record.isDeleted()) {
            throw new ResourceNotFoundException("Record not found");
        }
        // Ensure user owns record
        if (!uid.equals(record.getUser().getId())) {
            throw new UnauthorizedException("Access denied");
        }

        record.setDeleted(true);
        record.setDeletedAt(java.time.LocalDateTime.now());

        repository.save(record);
    }

    @Override
    public FinancialRecordResponseDTO updateRecord(UUID id, FinancialRecordRequestDTO request) {

        log.info("Deleting record: {}", id);

        UUID uid = getCurrentUserId();

        FinancialRecord record = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Record not found"));

        if (record.isDeleted()) {
            throw new ResourceNotFoundException("Record not found");
        }

        if (!record.getUser().getId().equals(uid)) {
            throw new UnauthorizedException("Access denied");
        }

        record.setAmount(request.getAmount());
        record.setType(request.getType());
        record.setCategory(request.getCategory());
        record.setDate(request.getDate());
        record.setDescription(request.getDescription());

        repository.save(record);

        return mapToResponse(record);
    }
}