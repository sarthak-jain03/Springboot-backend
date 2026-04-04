package backendAssignment.zorvyn.service;


import backendAssignment.zorvyn.dto.RecordRequestDTO;
import backendAssignment.zorvyn.dto.RecordResponseDTO;
import backendAssignment.zorvyn.entity.FinancialRecord;
import backendAssignment.zorvyn.entity.RecordType;
import backendAssignment.zorvyn.entity.User;
import backendAssignment.zorvyn.error.RecordNotFoundException;
import backendAssignment.zorvyn.repository.RecordRepository;
import backendAssignment.zorvyn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecordService {
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;

    public RecordResponseDTO createRecord(RecordRequestDTO recordRequestDTO, String username){
        User user = userRepository.findByUsername(username).orElseThrow(()->new UsernameNotFoundException("User not found"));

        FinancialRecord record = FinancialRecord.builder()
                .amount(recordRequestDTO.getAmount())
                .type(recordRequestDTO.getType())
                .category(recordRequestDTO.getCategory())
                .date(recordRequestDTO.getDate())
                .description(recordRequestDTO.getDescription())
                .userId(user.getId())
                .deleted(false)
                .build();

        FinancialRecord newRecord = recordRepository.save(record);
        log.info("Record created successfully.");
        return mapToRecordResponse(newRecord);

    }

    private RecordResponseDTO mapToRecordResponse(FinancialRecord record){
        return RecordResponseDTO.builder()
                .id(record.getId())
                .amount(record.getAmount())
                .type(record.getType())
                .category(record.getCategory())
                .date(record.getDate())
                .description(record.getDescription())
                .userId(record.getUserId())
                .createdAt(record.getCreatedAt())
                .updatedAt(record.getUpdatedAt())
                .build();
    }


    public RecordResponseDTO getRecordById(Long id){
        FinancialRecord record = recordRepository.findById(id).orElseThrow(()->new RecordNotFoundException("Record not found with id: "+id));
        return mapToRecordResponse(record);
    }

    public Page<RecordResponseDTO>getRecords(RecordType type, String category, LocalDate startDate, LocalDate endDate, Pageable pageable){
        Page<FinancialRecord>records;

        boolean hasType = type != null;
        boolean hasCategory = category != null && !category.isBlank();
        boolean hasDateRange = startDate != null && endDate != null;

        if (hasType && hasCategory && hasDateRange){
            records = recordRepository.findByTypeAndCategoryAndDateBetweenAndDeletedFalse(type, category, startDate, endDate, pageable);
        }
        else if (hasType && hasCategory){
            records = recordRepository.findByTypeAndCategoryAndDeletedFalse(type, category, pageable);
        }
        else if (hasType && hasDateRange){
            records = recordRepository.findByTypeAndDateBetweenAndDeletedFalse(type, startDate, endDate, pageable);
        }
        else if (hasCategory && hasDateRange){
            records = recordRepository.findByCategoryAndDateBetweenAndDeletedFalse(category, startDate, endDate, pageable);
        }

        else if (hasType){
            records = recordRepository.findByTypeAndDeletedFalse(type, pageable);
        }
        else if (hasCategory){
            records = recordRepository.findByCategoryAndDeletedFalse(category, pageable);
        }
        else if (hasDateRange){
            records = recordRepository.findByDateBetweenAndDeletedFalse(startDate, endDate, pageable);
        }
        else{
            records = recordRepository.findByDeletedFalse(pageable);
        }

        return records.map(this::mapToRecordResponse);
    }

    public RecordResponseDTO updateRecord(Long id, RecordRequestDTO recordRequestDTO){
        FinancialRecord record = recordRepository.findByIdAndDeletedFalse(id).orElseThrow(()->new RecordNotFoundException("Record not found with id: "+id));

        record.setAmount(recordRequestDTO.getAmount());
        record.setType(recordRequestDTO.getType());
        record.setCategory(recordRequestDTO.getCategory());
        record.setDate(recordRequestDTO.getDate());
        record.setDescription(recordRequestDTO.getDescription());

        FinancialRecord updatedRecord = recordRepository.save(record);
        log.info("Record updated successfully.");
        return mapToRecordResponse(updatedRecord);
    }

    public void deleteRecord(Long id){
        FinancialRecord record = recordRepository.findByIdAndDeletedFalse(id).orElseThrow(()->new RecordNotFoundException("Record not found with id: "+id));
        record.setDeleted(true);
        recordRepository.save(record);
    }
}
