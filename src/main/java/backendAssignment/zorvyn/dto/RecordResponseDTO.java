package backendAssignment.zorvyn.dto;


import backendAssignment.zorvyn.entity.RecordType;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecordResponseDTO {
    private Long id;
    private double amount;
    private RecordType type;
    private String category;
    private LocalDate date;
    private String description;
    private Long userId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
