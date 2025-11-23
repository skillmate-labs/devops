package com.skillmate.skillmate.modules.roles.controllers;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillmate.skillmate.modules.roles.dto.RoleDTO;
import com.skillmate.skillmate.modules.roles.useCases.CreateRoleUseCase;
import com.skillmate.skillmate.modules.roles.useCases.DeleteRoleUseCase;
import com.skillmate.skillmate.modules.roles.useCases.GetRoleUseCase;
import com.skillmate.skillmate.modules.roles.useCases.ListRolesUseCase;
import com.skillmate.skillmate.modules.roles.useCases.UpdateRoleUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
@Validated
public class RoleController {

  private final CreateRoleUseCase createRoleUseCase;
  private final GetRoleUseCase getRoleUseCase;
  private final ListRolesUseCase listRolesUseCase;
  private final UpdateRoleUseCase updateRoleUseCase;
  private final DeleteRoleUseCase deleteRoleUseCase;

  @PostMapping
  @PreAuthorize("hasRole('ADM')")
  public ResponseEntity<RoleDTO> create(@Valid @RequestBody RoleDTO roleDTO) {
    RoleDTO created = createRoleUseCase.execute(roleDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADM')")
  public ResponseEntity<RoleDTO> getById(@PathVariable String id) {
    RoleDTO role = getRoleUseCase.execute(id);
    return ResponseEntity.status(HttpStatus.OK).body(role);
  }

  @GetMapping
  @PreAuthorize("hasRole('ADM')")
  public ResponseEntity<List<RoleDTO>> listAll() {
    List<RoleDTO> roles = listRolesUseCase.executeAll();
    return ResponseEntity.status(HttpStatus.OK).body(roles);
  }

  @GetMapping("/paginated")
  @PreAuthorize("hasRole('ADM')")
  public ResponseEntity<Page<RoleDTO>> list(
      @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
    Page<RoleDTO> roles = listRolesUseCase.execute(pageable);
    return ResponseEntity.status(HttpStatus.OK).body(roles);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADM')")
  public ResponseEntity<RoleDTO> update(@PathVariable String id,
      @Valid @RequestBody RoleDTO roleDTO) {
    RoleDTO updated = updateRoleUseCase.execute(id, roleDTO);
    return ResponseEntity.status(HttpStatus.OK).body(updated);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADM')")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    deleteRoleUseCase.execute(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
