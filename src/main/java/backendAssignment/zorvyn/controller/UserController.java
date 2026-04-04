package backendAssignment.zorvyn.controller;


import backendAssignment.zorvyn.dto.UpdatedRolesRequest;
import backendAssignment.zorvyn.dto.UpdatedStatusRequestDTO;
import backendAssignment.zorvyn.dto.UserResponseDTO;
import backendAssignment.zorvyn.repository.UserRepository;
import backendAssignment.zorvyn.service.UserService;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
@Tag(name="User Management", description = "User Management managed by ADMIN")
public class UserController {

    private final UserService userService;


    @GetMapping
    @Operation(summary = "Get All Users", description = "It will fetch all the users in the form of pagination.")
    public ResponseEntity<Page<UserResponseDTO>>getAllUsers(@RequestParam(defaultValue = "0")int page,
                                                           @RequestParam(defaultValue = "10")int size,
                                                           @RequestParam(defaultValue = "createdAt")String sortBy,
                                                           @RequestParam(defaultValue = "desc")String sortDir){
        Sort sort =sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ResponseEntity.ok(userService.getAllUsers(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by Id", description = "It will fetch the user with the given id.")
    public ResponseEntity<UserResponseDTO>getUserById(@PathVariable Long id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @PatchMapping("/{id}/roles")
    @Operation(summary = "Update Roles", description = "It will update the roles of the user.")
    public ResponseEntity<UserResponseDTO>updateRoles(@PathVariable Long id, @RequestBody UpdatedRolesRequest updatedRolesRequest){
        return ResponseEntity.ok(userService.updateRoles(id, updatedRolesRequest));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Update Status", description = "It will update the status of the user.")
    public ResponseEntity<UserResponseDTO>updateStatus(@PathVariable Long id, @RequestBody UpdatedStatusRequestDTO updatedStatusRequestDTO){
        return ResponseEntity.ok(userService.updateStatus(id, updatedStatusRequestDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user by Id", description = "It will delete the user with the given id.")
    public ResponseEntity<Void>deleteUserById(@PathVariable Long id){
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }







}
