package backendAssignment.zorvyn.dto;


import backendAssignment.zorvyn.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    String jwt;
    String username;
    Long id;
    Set<Role> roles;
}
