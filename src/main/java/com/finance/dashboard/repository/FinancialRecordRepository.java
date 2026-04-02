package com.finance.dashboard.repository;

import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.enums.Category;
import com.finance.dashboard.enums.RecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface FinancialRecordRepository extends JpaRepository<FinancialRecord, UUID> {

    Page<FinancialRecord> findByUserIdAndTypeAndCategoryAndIsDeletedFalse(
            UUID userId,
            RecordType type,
            Category category,
            Pageable pageable
    );

    Page<FinancialRecord> findByUserIdAndIsDeletedFalse(
            UUID userId,
            Pageable pageable
    );
}
