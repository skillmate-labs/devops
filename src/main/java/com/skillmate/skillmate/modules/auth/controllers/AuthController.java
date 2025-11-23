package com.skillmate.skillmate.modules.auth.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skillmate.skillmate.modules.auth.dto.AuthResponseDTO;
import com.skillmate.skillmate.modules.auth.dto.LoginDTO;
import com.skillmate.skillmate.modules.auth.useCases.AuthUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

  private final AuthUseCase authUseCase;

  @PostMapping("/login")
  public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginDTO loginDTO) {
    AuthResponseDTO response = authUseCase.login(loginDTO);
    return ResponseEntity.status(HttpStatus.OK).body(response);
  }
}
