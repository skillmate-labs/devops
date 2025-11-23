package com.skillmate.skillmate.modules.users.useCases;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.skillmate.skillmate.modules.users.UserEntity;
import com.skillmate.skillmate.modules.users.UserRepository;
import com.skillmate.skillmate.modules.users.dto.UserDTO;
import com.skillmate.skillmate.modules.users.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetUserUseCase {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Cacheable(value = "users", key = "#id")
  public UserDTO execute(String id) {
    UserEntity user = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("User not found"));
    return userMapper.toDTO(user);
  }
}
