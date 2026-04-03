package backendAssignment.zorvyn.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyTrendDTO {
    private int year;
    private int month;
    private double totalIncome;
    private double totalExpense;
    private double net;
    private Long count;
}
