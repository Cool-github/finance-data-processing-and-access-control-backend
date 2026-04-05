package com.finance.dashboard.repository;

import com.finance.dashboard.entity.FinancialRecord;
import com.finance.dashboard.enums.Category;
import com.finance.dashboard.enums.RecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
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

    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FinancialRecord f WHERE f.user.id = :userId AND f.type = 'INCOME' AND f.isDeleted = false")
    Double getTotalIncome(UUID userId);

    @Query("SELECT COALESCE(SUM(f.amount), 0) FROM FinancialRecord f WHERE f.user.id = :userId AND f.type = 'EXPENSE' AND f.isDeleted = false")
    Double getTotalExpense(UUID userId);

    @Query("SELECT f.category, SUM(f.amount) FROM FinancialRecord f WHERE f.user.id = :userId AND f.isDeleted = false GROUP BY f.category")
    List<Object[]> getCategorySummary(UUID userId);

    List<FinancialRecord> findTop5ByUserIdAndIsDeletedFalseOrderByDateDesc(UUID userId);

    Page<FinancialRecord> findByUserIdAndTypeAndIsDeletedFalse(
            UUID userId,
            RecordType type,
            Pageable pageable
    );

    Page<FinancialRecord> findByUserIdAndCategoryAndIsDeletedFalse(
            UUID userId,
            Category category,
            Pageable pageable
    );

}
