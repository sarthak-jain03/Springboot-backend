package backendAssignment.zorvyn.repository;

import backendAssignment.zorvyn.entity.FinancialRecord;
import backendAssignment.zorvyn.entity.RecordType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

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

}
