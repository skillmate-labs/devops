package com.skillmate.skillmate.modules.auth.useCases;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillmate.skillmate.modules.auth.dto.AuthResponseDTO;
import com.skillmate.skillmate.modules.auth.dto.LoginDTO;
import com.skillmate.skillmate.modules.users.UserEntity;
import com.skillmate.skillmate.modules.users.UserRepository;
import com.skillmate.skillmate.security.JwtTokenProvider;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthUseCase {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;

  @Transactional
  public AuthResponseDTO login(LoginDTO loginDTO) {
    UserEntity user = userRepository.findByEmail(loginDTO.getEmail())
        .orElseThrow(() -> new RuntimeException("User not found"));

    if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) {
      throw new RuntimeException("Invalid password");
    }

    String token = jwtTokenProvider.generateToken(user);

    AuthResponseDTO response = new AuthResponseDTO();
    response.setToken(token);
    response.setUserId(user.getId());
    response.setEmail(user.getEmail());
    response.setRole(user.getRole() != null ? user.getRole().getAcronym() : null);

    return response;
  }
}
