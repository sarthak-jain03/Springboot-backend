package backendAssignment.zorvyn.service;

import backendAssignment.zorvyn.dto.LoginRequestDTO;
import backendAssignment.zorvyn.dto.LoginResponseDTO;
import backendAssignment.zorvyn.dto.SignupRequestDTO;
import backendAssignment.zorvyn.dto.SignupResponseDTO;
import backendAssignment.zorvyn.entity.Role;
import backendAssignment.zorvyn.entity.User;
import backendAssignment.zorvyn.repository.UserRepository;
import backendAssignment.zorvyn.security.UserPrincipal;
import backendAssignment.zorvyn.utils.AuthUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;

    public SignupResponseDTO signup(SignupRequestDTO signupRequestDTO){

        if (userRepository.existsByUsername(signupRequestDTO.getUsername())){
            throw new IllegalArgumentException("Username already exists!");
        }

        if (userRepository.existsByEmail(signupRequestDTO.getEmail())){
            throw new IllegalArgumentException("Email already exists!");
        }

        User user = User.builder()
                .username(signupRequestDTO.getUsername())
                .email(signupRequestDTO.getEmail())
                .password(passwordEncoder.encode(signupRequestDTO.getPassword()))
                .name(signupRequestDTO.getName())
                .roles(signupRequestDTO.getRoles())
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        log.info("User registered successfully: {}", savedUser.getUsername());

        return new SignupResponseDTO(
                savedUser.getUsername(),
                savedUser.getEmail()
        );
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO){
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequestDTO.getUsername(),
                            loginRequestDTO.getPassword()
                    )
            );

            UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

            if (!principal.getUser().isActive()) {
                throw new RuntimeException("User account is inactive");
            }

            String token = authUtil.generateAccessToken(principal);

            log.info("User logged in: {}", principal.getUsername());

            return new LoginResponseDTO(
                    token,
                    principal.getUsername(),
                    principal.getId(),
                    principal.getUser().getRoles()
            );

        } catch (BadCredentialsException e) {
            log.warn("Failed login attempt for username: {}", loginRequestDTO.getUsername());
            throw new RuntimeException("Invalid username or password");
        }
    }
}