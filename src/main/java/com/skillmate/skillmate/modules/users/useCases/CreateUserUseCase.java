package com.skillmate.skillmate.modules.users.useCases;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillmate.skillmate.config.MessageProducer;
import com.skillmate.skillmate.modules.roles.RoleEntity;
import com.skillmate.skillmate.modules.roles.RoleRepository;
import com.skillmate.skillmate.modules.users.UserEntity;
import com.skillmate.skillmate.modules.users.UserRepository;
import com.skillmate.skillmate.modules.users.dto.UserDTO;
import com.skillmate.skillmate.modules.users.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateUserUseCase {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final UserMapper userMapper;
  private final PasswordEncoder passwordEncoder;
  private final MessageProducer messageProducer;

  @Transactional
  @CacheEvict(value = { "users" }, allEntries = true)
  @CachePut(value = "users", key = "#result.id")
  public UserDTO execute(UserDTO userDTO) {
    if (userRepository.existsByEmail(userDTO.getEmail())) {
      throw new RuntimeException("Email already exists");
    }

    RoleEntity role = roleRepository.findById(userDTO.getRoleId())
        .orElseThrow(() -> new RuntimeException("Role not found"));

    UserEntity user = userMapper.toEntity(userDTO);
    user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
    user.setRole(role);

    UserEntity savedUser = userRepository.save(user);
    UserDTO savedUserDTO = userMapper.toDTO(savedUser);
    messageProducer.sendUserCreated(savedUserDTO);
    return savedUserDTO;
  }
}
