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
public class CreateRoleUseCase {

  private final RoleRepository roleRepository;
  private final RoleMapper roleMapper;

  @Transactional
  @CacheEvict(value = { "roles" }, allEntries = true)
  @CachePut(value = "roles", key = "#result.id")
  public RoleDTO execute(RoleDTO roleDTO) {
    if (roleRepository.findByAcronym(roleDTO.getAcronym()).isPresent()) {
      throw new RuntimeException("Role acronym already exists");
    }
    if (roleRepository.findByName(roleDTO.getName()).isPresent()) {
      throw new RuntimeException("Role name already exists");
    }

    RoleEntity role = roleMapper.toEntity(roleDTO);
    RoleEntity savedRole = roleRepository.save(role);
    return roleMapper.toDTO(savedRole);
  }
}
