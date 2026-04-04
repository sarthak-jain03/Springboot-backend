package backendAssignment.zorvyn.controller;


import backendAssignment.zorvyn.dto.LoginRequestDTO;
import backendAssignment.zorvyn.dto.LoginResponseDTO;
import backendAssignment.zorvyn.dto.SignupRequestDTO;
import backendAssignment.zorvyn.dto.SignupResponseDTO;
import backendAssignment.zorvyn.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name="Authentication", description = "user signup and login")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    @Operation(summary = "Signup", description = "New user registration")
    public ResponseEntity<SignupResponseDTO>signup(@Valid @RequestBody SignupRequestDTO signupRequestDTO){
        SignupResponseDTO signupResponseDTO = authService.signup(signupRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(signupResponseDTO);
    }

    @PostMapping("/login")
    @Operation(summary = "Login", description = "Login to existing account.")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO){
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }


}
