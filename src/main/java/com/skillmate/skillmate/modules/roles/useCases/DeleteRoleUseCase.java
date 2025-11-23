package com.skillmate.skillmate.modules.roles.useCases;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillmate.skillmate.modules.roles.RoleRepository;
import com.skillmate.skillmate.modules.users.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteRoleUseCase {

  private final RoleRepository roleRepository;
  private final UserRepository userRepository;

  @Transactional
  @CacheEvict(value = { "roles" }, allEntries = true, key = "#id")
  public void execute(String id) {
    if (!roleRepository.existsById(id)) {
      throw new RuntimeException("Role not found");
    }

    long usersCount = userRepository.countByRoleId(id);
    if (usersCount > 0) {
      throw new RuntimeException("Cannot delete role: " + usersCount + " user(s) are using this role");
    }

    roleRepository.deleteById(id);
  }
}
