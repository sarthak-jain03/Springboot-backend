package backendAssignment.zorvyn.controller;


import backendAssignment.zorvyn.dto.CategorySummaryDTO;
import backendAssignment.zorvyn.dto.DashboardSummaryDTO;
import backendAssignment.zorvyn.dto.MonthlyTrendDTO;
import backendAssignment.zorvyn.dto.RecordResponseDTO;
import backendAssignment.zorvyn.service.DashboardService;
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
public class DashboardController {

    private DashboardService dashboardService;

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<DashboardSummaryDTO>getSummary(){
        return ResponseEntity.ok(dashboardService.getSummary());
    }


    @GetMapping("/category-summary")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    public ResponseEntity<List<CategorySummaryDTO>>getCategorySummary(){
        return ResponseEntity.ok(dashboardService.getCategorySummary());
    }

    @GetMapping("/monthly-trend")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<List<MonthlyTrendDTO>>getMonthlyTrends(@RequestParam(required = false) Integer year){
        int trendYear = (year != null ? year : LocalDate.now().getYear());
        return ResponseEntity.ok(dashboardService.getMonthlyTrend(trendYear));
    }


    @GetMapping("/recent-activity")
    @PreAuthorize("hasAnyRole('ADMIN', 'ANALYST')")
    public ResponseEntity<List<RecordResponseDTO>>getRecentActivity(@RequestParam(defaultValue = "10") int limit){
        return ResponseEntity.ok(dashboardService.getRecentActivity(limit));
    }
}
