package com.skillmate.skillmate.modules.roles.mapper;

import org.springframework.stereotype.Component;

import com.skillmate.skillmate.modules.roles.RoleEntity;
import com.skillmate.skillmate.modules.roles.dto.RoleDTO;

@Component
public class RoleMapper {

  public RoleDTO toDTO(RoleEntity entity) {
    if (entity == null) {
      return null;
    }
    RoleDTO dto = new RoleDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setAcronym(entity.getAcronym());
    dto.setCreatedAt(entity.getCreatedAt());
    return dto;
  }

  public RoleEntity toEntity(RoleDTO dto) {
    if (dto == null) {
      return null;
    }
    RoleEntity entity = new RoleEntity();
    entity.setId(dto.getId());
    entity.setName(dto.getName());
    entity.setAcronym(dto.getAcronym());
    return entity;
  }
}
