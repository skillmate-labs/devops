package com.skillmate.skillmate.modules.users.useCases;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillmate.skillmate.modules.roles.RoleEntity;
import com.skillmate.skillmate.modules.roles.RoleRepository;
import com.skillmate.skillmate.modules.users.UserEntity;
import com.skillmate.skillmate.modules.users.UserRepository;
import com.skillmate.skillmate.modules.users.dto.UserDTO;
import com.skillmate.skillmate.modules.users.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateUserUseCase {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;

  @Transactional
  @CacheEvict(value = { "users" }, allEntries = true)
  @CachePut(value = "users", key = "#id")
  public UserDTO execute(String id, UserDTO userDTO) {
    UserEntity user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));

    userRepository.findByEmail(userDTO.getEmail())
        .ifPresent(existingUser -> {
          if (!existingUser.getId().equals(id)) {
            throw new RuntimeException("Email already exists");
          }
        });

    user.setName(userDTO.getName());
    user.setEmail(userDTO.getEmail());

    if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
      user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    }

    if (userDTO.getRoleId() != null && !userDTO.getRoleId().isEmpty()) {
      RoleEntity role = roleRepository.findById(userDTO.getRoleId())
          .orElseThrow(() -> new RuntimeException("Role not found"));
      user.setRole(role);
    }

    UserEntity savedUser = userRepository.save(user);
    return userMapper.toDTO(savedUser);
  }
}
