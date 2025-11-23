package com.skillmate.skillmate.modules.roles.useCases;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.skillmate.skillmate.modules.roles.RoleEntity;
import com.skillmate.skillmate.modules.roles.RoleRepository;
import com.skillmate.skillmate.modules.roles.dto.RoleDTO;
import com.skillmate.skillmate.modules.roles.mapper.RoleMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListRolesUseCase {

  private final RoleRepository roleRepository;
  private final RoleMapper roleMapper;

  @Cacheable(value = "roles", key = "'all'")
  public List<RoleDTO> executeAll() {
    List<RoleEntity> roles = roleRepository.findAll();
    return roles.stream()
        .map(roleMapper::toDTO)
        .collect(Collectors.toList());
  }

  @Cacheable(value = "roles", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
  public Page<RoleDTO> execute(Pageable pageable) {
    Page<RoleEntity> roles = roleRepository.findAll(pageable);
    return roles.map(roleMapper::toDTO);
  }
}
