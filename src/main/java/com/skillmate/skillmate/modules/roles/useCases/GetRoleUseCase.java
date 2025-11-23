package com.skillmate.skillmate.modules.roles.useCases;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.skillmate.skillmate.modules.roles.RoleEntity;
import com.skillmate.skillmate.modules.roles.RoleRepository;
import com.skillmate.skillmate.modules.roles.dto.RoleDTO;
import com.skillmate.skillmate.modules.roles.mapper.RoleMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetRoleUseCase {

  private final RoleRepository roleRepository;
  private final RoleMapper roleMapper;

  @Cacheable(value = "roles", key = "#id")
  public RoleDTO execute(String id) {
    RoleEntity role = roleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Role not found"));
    return roleMapper.toDTO(role);
  }

  @Cacheable(value = "roles", key = "'acronym-' + #acronym")
  public RoleDTO findByAcronym(String acronym) {
    RoleEntity role = roleRepository.findByAcronym(acronym)
        .orElseThrow(() -> new RuntimeException("Role not found"));
    return roleMapper.toDTO(role);
  }

  @Cacheable(value = "roles", key = "'name-' + #name")
  public RoleDTO findByName(String name) {
    RoleEntity role = roleRepository.findByName(name)
        .orElseThrow(() -> new RuntimeException("Role not found"));
    return roleMapper.toDTO(role);
  }
}
