package backendAssignment.zorvyn.service;


import backendAssignment.zorvyn.dto.CategorySummaryDTO;
import backendAssignment.zorvyn.dto.DashboardSummaryDTO;
import backendAssignment.zorvyn.dto.MonthlyTrendDTO;
import backendAssignment.zorvyn.dto.RecordResponseDTO;
import backendAssignment.zorvyn.entity.FinancialRecord;
import backendAssignment.zorvyn.entity.RecordType;
import backendAssignment.zorvyn.repository.RecordRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashboardService {

    private final RecordRepository recordRepository;

    public DashboardSummaryDTO getSummary(){
        Double totalExpense = recordRepository.sumAmountByTypeAndDeletedFalse(RecordType.EXPENSE);
        Double totalIncome = recordRepository.sumAmountByTypeAndDeletedFalse(RecordType.INCOME);
        Long totalRecords = recordRepository.countByDeletedFalse();

        double income = totalIncome != null ? totalIncome : 0.0;
        double expense = totalExpense != null ? totalExpense : 0.0;

        return DashboardSummaryDTO.builder()
                .totalExpense(expense)
                .totalIncome(income)
                .netBalance(income-expense)
                .totalRecords(totalRecords != null ? totalRecords : 0L)
                .build();
    }


    public List<CategorySummaryDTO> getCategorySummary() {
        List<Object[]>result = recordRepository.getCategorySummaryData();
        Map<String, CategorySummaryDTO>categoryMap = new HashMap<>();

        for (Object[] row: result){
            String category = (String)row[0];
            RecordType type = (RecordType) row[1];
            Double amount = (Double)row[2];
            Long count = (Long)row[3];

            CategorySummaryDTO summary = categoryMap.computeIfAbsent(category, k->CategorySummaryDTO.builder()
                    .category(k)
                    .totalIncome(0)
                    .totalExpense(0)
                    .net(0)
                    .count(0L)
                    .build()
            );

            if (RecordType.INCOME.name().equals(type)){
                summary.setTotalIncome(summary.getTotalIncome() + (amount != null ? amount : 0.0));
            }
            else{
                summary.setTotalExpense(summary.getTotalExpense() + (amount != null ? amount : 0.0));
            }

            summary.setCount(summary.getCount() + (count != null ? count : 0L));
            summary.setNet(summary.getTotalIncome()-summary.getTotalExpense());
        }

        return new ArrayList<>(categoryMap.values());
    }



    public List<MonthlyTrendDTO>getMonthlyTrend(int year){
        List<Object[]>result = recordRepository.getMonthlyTrendsData(year);
        Map<Integer, MonthlyTrendDTO>monthMap = new HashMap<>();


        for (Object[] row: result){
            Integer month = ((Number)row[0]).intValue();
            RecordType type = (RecordType)row[2];
            Double amount = (Double)row[3];
            Long count = (Long)row[4];

            MonthlyTrendDTO trend = monthMap.computeIfAbsent(month, m->MonthlyTrendDTO.builder()
                    .year(year)
                    .month(m)
                    .totalIncome(0)
                    .totalExpense(0)
                    .net(0)
                    .count(0L)
                    .build());

            if (RecordType.INCOME.equals(type)) {
                trend.setTotalIncome(trend.getTotalIncome() + (amount != null ? amount : 0));
            }
            else{
                trend.setTotalExpense(trend.getTotalExpense() + (amount != null ? amount : 0));
            }
            trend.setCount(trend.getCount() + (count != null ? count : 0));
            trend.setNet(trend.getTotalIncome()-trend.getTotalExpense());
        }

        List<MonthlyTrendDTO>trends = new ArrayList<>();
        for (int month=1; month<=12; month++){
            trends.add(monthMap.getOrDefault(month, MonthlyTrendDTO.builder()
                    .year(year)
                    .month(month)
                    .totalIncome(0)
                    .totalExpense(0)
                    .net(0)
                    .count(0L)
                    .build()));
        }
        return trends;
    }


    public List<RecordResponseDTO>getRecentActivity(int limit){
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "createdAt"));
        List<FinancialRecord>records = recordRepository.findByDeletedFalseOrderByCreatedAtDesc(pageable);

        return records.stream()
                .map(record->RecordResponseDTO.builder()
                        .id(record.getId())
                        .amount(record.getAmount())
                        .type(record.getType())
                        .category(record.getCategory())
                        .date(record.getDate())
                        .description(record.getDescription())
                        .userId(record.getUserId())
                        .createdAt(record.getCreatedAt())
                        .updatedAt(record.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());


    }
}
