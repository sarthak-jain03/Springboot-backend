package backendAssignment.zorvyn.dto;


import backendAssignment.zorvyn.entity.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SignupRequestDTO {

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Name is required")
    private String name;

    private Set<Role>roles;

}
