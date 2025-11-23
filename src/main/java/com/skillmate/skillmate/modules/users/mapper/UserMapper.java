package com.skillmate.skillmate.modules.users.mapper;

import org.springframework.stereotype.Component;

import com.skillmate.skillmate.modules.users.UserEntity;
import com.skillmate.skillmate.modules.users.dto.UserDTO;

@Component
public class UserMapper {

  public UserDTO toDTO(UserEntity entity) {
    if (entity == null) {
      return null;
    }
    UserDTO dto = new UserDTO();
    dto.setId(entity.getId());
    dto.setName(entity.getName());
    dto.setEmail(entity.getEmail());
    dto.setRoleId(entity.getRole() != null ? entity.getRole().getId() : null);
    dto.setRoleName(entity.getRole() != null ? entity.getRole().getName() : null);
    return dto;
  }

  public UserEntity toEntity(UserDTO dto) {
    if (dto == null) {
      return null;
    }
    UserEntity entity = new UserEntity();
    entity.setId(dto.getId());
    entity.setName(dto.getName());
    entity.setEmail(dto.getEmail());
    entity.setPassword(dto.getPassword());
    return entity;
  }
}
