package com.skillmate.skillmate.modules.roles.useCases;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillmate.skillmate.modules.roles.RoleEntity;
import com.skillmate.skillmate.modules.roles.RoleRepository;
import com.skillmate.skillmate.modules.roles.dto.RoleDTO;
import com.skillmate.skillmate.modules.roles.mapper.RoleMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateRoleUseCase {

  private final RoleRepository roleRepository;
  private final RoleMapper roleMapper;

  @Transactional
  @CacheEvict(value = { "roles" }, allEntries = true)
  @CachePut(value = "roles", key = "#id")
  public RoleDTO execute(String id, RoleDTO roleDTO) {
    RoleEntity role = roleRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Role not found"));

    roleRepository.findByAcronym(roleDTO.getAcronym())
        .ifPresent(existingRole -> {
          if (!existingRole.getId().equals(id)) {
            throw new RuntimeException("Role acronym already exists");
          }
        });

    roleRepository.findByName(roleDTO.getName())
        .ifPresent(existingRole -> {
          if (!existingRole.getId().equals(id)) {
            throw new RuntimeException("Role name already exists");
          }
        });

    role.setName(roleDTO.getName());
    role.setAcronym(roleDTO.getAcronym());

    RoleEntity savedRole = roleRepository.save(role);
    return roleMapper.toDTO(savedRole);
  }
}
