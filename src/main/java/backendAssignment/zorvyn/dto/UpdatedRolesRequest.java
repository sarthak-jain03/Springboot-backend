package backendAssignment.zorvyn.dto;


import backendAssignment.zorvyn.entity.Role;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class UpdatedRolesRequest {

    @NotEmpty(message = "Give atleast one role")
    private Set<Role> roles;
}
