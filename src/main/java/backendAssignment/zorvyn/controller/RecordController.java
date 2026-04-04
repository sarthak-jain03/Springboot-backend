package backendAssignment.zorvyn.controller;


import backendAssignment.zorvyn.dto.RecordRequestDTO;
import backendAssignment.zorvyn.dto.RecordResponseDTO;
import backendAssignment.zorvyn.entity.RecordType;
import backendAssignment.zorvyn.service.RecordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequestMapping("/api/records")
@RequiredArgsConstructor
@Tag(name = "Financial Records", description = "CRUD operations for financial Records")
public class RecordController {

    private final RecordService recordService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Create a financial record", description = "Creates a new financial record(Admin only)")
    public ResponseEntity<RecordResponseDTO>createRecord(@Valid @RequestBody RecordRequestDTO recordRequestDTO, Authentication authentication){
        RecordResponseDTO response = recordService.createRecord(recordRequestDTO, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    @Operation(summary = "Get all records", description = "Returns paginated and filterable financial records(Analyst, Admin)")
    public ResponseEntity<Page<RecordResponseDTO>>getRecords(@RequestParam(required = false)RecordType type,
                                                             @RequestParam(required = false)String category,
                                                             @RequestParam(required = false) LocalDate startDate,
                                                             @RequestParam(required = false) LocalDate endDate,
                                                             @RequestParam(defaultValue = "0") int page,
                                                             @RequestParam(defaultValue = "10") int size,
                                                             @RequestParam(defaultValue = "date") String sortBy,
                                                             @RequestParam(defaultValue = "desc") String sortDir){

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<RecordResponseDTO>records = recordService.getRecords(type, category, startDate, endDate, pageable);
        return ResponseEntity.ok(records);
    }


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ANALYST', 'ADMIN')")
    @Operation(summary = "Get record by ID", description = "Returns a single financial record(Analyst, Admin)")
    public ResponseEntity<RecordResponseDTO>getRecordById(@PathVariable Long id){
        return ResponseEntity.ok(recordService.getRecordById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update a financial record", description = "Updates an existing financial record(Admin only)")
    public ResponseEntity<RecordResponseDTO>updateRecord(@PathVariable Long id, @Valid @RequestBody RecordRequestDTO recordRequestDTO){
        return ResponseEntity.ok(recordService.updateRecord(id, recordRequestDTO));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a financial record", description = "Soft-deletes a financial record(Admin only)")
    public ResponseEntity<Void>deleteRecord(@PathVariable Long id){
        recordService.deleteRecord(id);
        return ResponseEntity.noContent().build();
    }




}
