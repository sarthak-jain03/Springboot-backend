package backendAssignment.zorvyn.controller;


import backendAssignment.zorvyn.dto.LoginRequestDTO;
import backendAssignment.zorvyn.dto.LoginResponseDTO;
import backendAssignment.zorvyn.dto.SignupRequestDTO;
import backendAssignment.zorvyn.dto.SignupResponseDTO;
import backendAssignment.zorvyn.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDTO>signup(@Valid @RequestBody SignupRequestDTO signupRequestDTO){
        SignupResponseDTO signupResponseDTO = authService.signup(signupRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(signupResponseDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO){
        return ResponseEntity.ok(authService.login(loginRequestDTO));
    }


}
