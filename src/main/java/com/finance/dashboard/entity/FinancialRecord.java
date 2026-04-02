package com.finance.dashboard.entity;

import com.finance.dashboard.enums.Category;
import com.finance.dashboard.enums.RecordType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

    @Entity
    @Table(name = "financial_records")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class FinancialRecord extends BaseEntity {

        private Double amount;

        @Enumerated(EnumType.STRING)
        private RecordType type;

        @Enumerated(EnumType.STRING)
        private Category category;

        private LocalDate date;

        private String description;

        // Owner
        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        // Soft delete
        private boolean isDeleted;
        private java.time.LocalDateTime deletedAt;
}
