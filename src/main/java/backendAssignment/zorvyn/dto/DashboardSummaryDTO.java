package backendAssignment.zorvyn.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardSummaryDTO {
    private double totalIncome;
    private double totalExpense;
    private double netBalance;
    private Long totalRecords;
}
