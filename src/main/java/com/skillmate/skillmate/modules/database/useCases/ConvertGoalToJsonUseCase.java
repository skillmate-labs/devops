package com.skillmate.skillmate.modules.database.useCases;

import org.springframework.stereotype.Service;

import com.skillmate.skillmate.modules.database.dto.JsonResponseDTO;
import com.skillmate.skillmate.modules.database.repository.DatabaseFunctionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConvertGoalToJsonUseCase {

  private final DatabaseFunctionRepository databaseFunctionRepository;

  public JsonResponseDTO execute(String goalId) {
    try {
      String json = databaseFunctionRepository.convertGoalToJson(goalId);

      if (json == null || json.trim().isEmpty()) {
        throw new RuntimeException("Função retornou resultado vazio para o goal ID: " + goalId);
      }

      if (json.contains("\"error\"")) {
        throw new RuntimeException("Erro na função do banco: " + json);
      }

      return new JsonResponseDTO(json);
    } catch (Exception e) {
      throw new RuntimeException("Erro ao converter goal para JSON: " + e.getMessage(), e);
    }
  }
}
