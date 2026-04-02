package backendAssignment.zorvyn.dto;


import backendAssignment.zorvyn.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDTO {
    private Long id;
    private String username;
    private String email;
    private String name;
    private Set<Role> roles;
    private boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
