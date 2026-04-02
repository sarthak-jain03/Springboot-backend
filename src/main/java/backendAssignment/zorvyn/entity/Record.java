package backendAssignment.zorvyn.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "financial_records", indexes = {
        @Index(name = "idx_type", columnList = "type"),
        @Index(name = "idx_category", columnList = "category"),
        @Index(name = "idx_user_id", columnList = "user_id"),
        @Index(name = "idx_deleted", columnList = "deleted")
})
@EntityListeners(AuditingEntityListener.class)
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private double amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RecordType type;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 500)
    private String description;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Builder.Default
    @Column(nullable = false)
    private boolean deleted = false;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
