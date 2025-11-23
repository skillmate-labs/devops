package com.skillmate.skillmate.modules.database.useCases;

import org.springframework.stereotype.Service;

import com.skillmate.skillmate.modules.database.dto.JsonResponseDTO;
import com.skillmate.skillmate.modules.database.repository.DatabaseFunctionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalculateGoalCompatibilityUseCase {

  private final DatabaseFunctionRepository databaseFunctionRepository;

  public JsonResponseDTO execute(String userId, String goalId) {
    try {
      String json = databaseFunctionRepository.calculateGoalCompatibility(userId, goalId);

      if (json == null || json.trim().isEmpty()) {
        throw new RuntimeException("Função retornou resultado vazio para user ID: " + userId + " e goal ID: " + goalId);
      }

      if (json.contains("\"error\"")) {
        throw new RuntimeException("Erro na função do banco: " + json);
      }

      return new JsonResponseDTO(json);
    } catch (Exception e) {
      throw new RuntimeException("Erro ao calcular compatibilidade: " + e.getMessage(), e);
    }
  }
}
