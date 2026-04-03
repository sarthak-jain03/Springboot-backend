package backendAssignment.zorvyn.dto;


import backendAssignment.zorvyn.entity.RecordType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordRequestDTO {

    @NotNull(message = "Amount Required!")
    @Positive(message = "Amount should be greater than 0")
    private double amount;

    @NotNull(message = "Type Required! (INCOME/EXPENSE)")
    private RecordType type;

    @NotBlank(message = "Category required!")
    private String category;

    @NotNull(message = "Date required!")
    private LocalDate date;

    private String description;


}
