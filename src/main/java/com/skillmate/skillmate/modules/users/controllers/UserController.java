package com.skillmate.skillmate.modules.users.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillmate.skillmate.modules.users.dto.UserDTO;
import com.skillmate.skillmate.modules.users.useCases.CreateUserUseCase;
import com.skillmate.skillmate.modules.users.useCases.DeleteUserUseCase;
import com.skillmate.skillmate.modules.users.useCases.GetUserUseCase;
import com.skillmate.skillmate.modules.users.useCases.ListUsersUseCase;
import com.skillmate.skillmate.modules.users.useCases.UpdateUserUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
public class UserController {

  private final CreateUserUseCase createUserUseCase;
  private final GetUserUseCase getUserUseCase;
  private final ListUsersUseCase listUsersUseCase;
  private final UpdateUserUseCase updateUserUseCase;
  private final DeleteUserUseCase deleteUserUseCase;

  @PostMapping("/register")
  public ResponseEntity<UserDTO> create(@Valid @RequestBody UserDTO userDTO) {
    UserDTO created = createUserUseCase.execute(userDTO);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @GetMapping("/{id}")
  public ResponseEntity<UserDTO> getById(@PathVariable String id) {
    UserDTO user = getUserUseCase.execute(id);
    return ResponseEntity.status(HttpStatus.OK).body(user);
  }

  @GetMapping
  public ResponseEntity<Page<UserDTO>> list(
      @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
    Page<UserDTO> users = listUsersUseCase.execute(pageable);
    return ResponseEntity.status(HttpStatus.OK).body(users);
  }

  @PutMapping("/{id}")
  public ResponseEntity<UserDTO> update(@PathVariable String id,
      @Valid @RequestBody UserDTO userDTO) {
    UserDTO updated = updateUserUseCase.execute(id, userDTO);
    return ResponseEntity.status(HttpStatus.OK).body(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    deleteUserUseCase.execute(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }
}
