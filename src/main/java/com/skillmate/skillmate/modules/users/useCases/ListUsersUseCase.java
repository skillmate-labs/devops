package com.skillmate.skillmate.modules.users.useCases;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.skillmate.skillmate.modules.users.UserEntity;
import com.skillmate.skillmate.modules.users.UserRepository;
import com.skillmate.skillmate.modules.users.dto.UserDTO;
import com.skillmate.skillmate.modules.users.mapper.UserMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListUsersUseCase {

  private final UserRepository userRepository;
  private final UserMapper userMapper;

  @Cacheable(value = "users", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
  public Page<UserDTO> execute(Pageable pageable) {
    Page<UserEntity> users = userRepository.findAll(pageable);
    return users.map(userMapper::toDTO);
  }
}
