package com.skillmate.skillmate.modules.database.useCases;

import org.springframework.stereotype.Service;

import com.skillmate.skillmate.modules.database.dto.JsonResponseDTO;
import com.skillmate.skillmate.modules.database.repository.DatabaseFunctionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ExportDatasetToJsonUseCase {

  private final DatabaseFunctionRepository databaseFunctionRepository;

  public JsonResponseDTO execute() {
    try {
      String json = databaseFunctionRepository.exportDatasetToJson();

      if (json == null || json.trim().isEmpty()) {
        throw new RuntimeException("Procedure retornou resultado vazio");
      }

      if (json.contains("\"error\"")) {
        throw new RuntimeException("Erro na procedure do banco: " + json);
      }

      return new JsonResponseDTO(json);
    } catch (Exception e) {
      throw new RuntimeException("Erro ao exportar dataset: " + e.getMessage(), e);
    }
  }
}
