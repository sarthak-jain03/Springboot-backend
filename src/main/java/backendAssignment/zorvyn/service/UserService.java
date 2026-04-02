package backendAssignment.zorvyn.service;


import backendAssignment.zorvyn.dto.UpdatedRolesRequest;
import backendAssignment.zorvyn.dto.UpdatedStatusRequestDTO;
import backendAssignment.zorvyn.dto.UserResponseDTO;
import backendAssignment.zorvyn.entity.User;
import backendAssignment.zorvyn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Pageable;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Page<UserResponseDTO> getAllUsers(Pageable pageable){
        return userRepository.findAll(pageable).map(this::maptoUserResponse);
    }

    private UserResponseDTO maptoUserResponse(User user){
        return UserResponseDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .name(user.getName())
                .roles(user.getRoles())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }


    public UserResponseDTO getUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new RuntimeException("User not found!"));
        return maptoUserResponse(user);
    }

    public UserResponseDTO updateRoles(Long id, UpdatedRolesRequest updatedRolesRequest){
        User user = userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found!"));
        user.setRoles(updatedRolesRequest.getRoles());
        User updatedUser = userRepository.save(user);
        log.info("roles updated successfully");
        return maptoUserResponse(updatedUser);
    }

    public UserResponseDTO updateStatus(Long id, UpdatedStatusRequestDTO updatedStatusRequestDTO){
        User user = userRepository.findById(id).orElseThrow(()->new RuntimeException("User not found!"));
        user.setActive(updatedStatusRequestDTO.isActive());
        User updatedUser = userRepository.save(user);
        log.info("status updated successfully.");
        return maptoUserResponse(updatedUser);
    }

    public String deleteUserById(Long id){
        User user = userRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("User not found!"));
        userRepository.delete(user);
        return "User with id: "+ id + " is deleted successfully.";
    }

}

