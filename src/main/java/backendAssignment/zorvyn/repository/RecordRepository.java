package backendAssignment.zorvyn.repository;

import backendAssignment.zorvyn.entity.FinancialRecord;
import backendAssignment.zorvyn.entity.RecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RecordRepository extends JpaRepository<FinancialRecord, Long> {

    Optional<FinancialRecord> findByIdAndDeletedFalse(Long id);
    Page<FinancialRecord> findByDeletedFalse(Pageable pageable);
    Page<FinancialRecord> findByTypeAndDeletedFalse(RecordType type, Pageable pageable);
    Page<FinancialRecord> findByCategoryAndDeletedFalse(String category, Pageable pageable);
    Page<FinancialRecord> findByDateBetweenAndDeletedFalse(LocalDate start, LocalDate end, Pageable pageable);
    Page<FinancialRecord> findByTypeAndCategoryAndDeletedFalse(RecordType type, String category, Pageable pageable);
    Page<FinancialRecord> findByTypeAndDateBetweenAndDeletedFalse(RecordType type, LocalDate start, LocalDate end, Pageable pageable);
    Page<FinancialRecord> findByCategoryAndDateBetweenAndDeletedFalse(String category, LocalDate start, LocalDate end, Pageable pageable);
    Page<FinancialRecord> findByTypeAndCategoryAndDateBetweenAndDeletedFalse(
            RecordType type, String category, LocalDate start, LocalDate end, Pageable pageable);

    List<FinancialRecord> findByDeletedFalseOrderByCreatedAtDesc(Pageable pageable);
    List<FinancialRecord> findByDeletedFalse();



    @Query("SELECT COALESCE(SUM(r.amount), 0) FROM FinancialRecord r WHERE r.type = :type AND r.deleted = false")
    Double sumAmountByTypeAndDeletedFalse(@Param("type") RecordType type);

    @Query("SELECT COUNT(r) FROM FinancialRecord r WHERE r.deleted = false")
    Long countByDeletedFalse();

    @Query("SELECT r.category, r.type, SUM(r.amount), COUNT(r) FROM FinancialRecord r WHERE r.deleted = false GROUP BY r.category, r.type ORDER BY r.category")
    List<Object[]> getCategorySummaryData();

    @Query("SELECT MONTH(r.date), YEAR(r.date), r.type, SUM(r.amount), COUNT(r) FROM FinancialRecord r WHERE r.deleted = false AND YEAR(r.date) = :year GROUP BY MONTH(r.date), YEAR(r.date), r.type ORDER BY MONTH(r.date)")
    List<Object[]> getMonthlyTrendsData(@Param("year") int year);

}
