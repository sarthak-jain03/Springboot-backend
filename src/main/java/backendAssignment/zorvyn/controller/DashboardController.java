package backendAssignment.zorvyn.controller;


import backendAssignment.zorvyn.dto.CategorySummaryDTO;
import backendAssignment.zorvyn.dto.DashboardSummaryDTO;
import backendAssignment.zorvyn.dto.MonthlyTrendDTO;
import backendAssignment.zorvyn.dto.RecordResponseDTO;
import backendAssignment.zorvyn.service.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard", description = "Summary and analytics endpoints for the finance dashboard")
public class DashboardController {

    private DashboardService dashboardService;

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    @Operation(summary = "Get dashboard summary", description = "Returns total income, total expenses, net balance, & total record count(Analyst, Admin)")
    public ResponseEntity<DashboardSummaryDTO>getSummary(){
        return ResponseEntity.ok(dashboardService.getSummary());
    }


    @GetMapping("/category-summary")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    @Operation(summary = "Get category-wise summary", description = "Returns income and expense totals grouped by category(Analyst, Admin)")
    public ResponseEntity<List<CategorySummaryDTO>>getCategorySummary(){
        return ResponseEntity.ok(dashboardService.getCategorySummary());
    }

    @GetMapping("/monthly-trend")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    @Operation(summary = "Get monthly trends", description = "Returns month-by-month income/expense breakdown for a given year(Analyst, Admin)")
    public ResponseEntity<List<MonthlyTrendDTO>>getMonthlyTrends(@RequestParam(required = false) Integer year){
        int trendYear = (year != null ? year : LocalDate.now().getYear());
        return ResponseEntity.ok(dashboardService.getMonthlyTrend(trendYear));
    }


    @GetMapping("/recent-activity")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    @Operation(summary = "Get recent activity", description = "Returns the N most recent financial records(All authenticated users)")
    public ResponseEntity<List<RecordResponseDTO>>getRecentActivity(@RequestParam(defaultValue = "10") int limit){
        return ResponseEntity.ok(dashboardService.getRecentActivity(limit));
    }
}
