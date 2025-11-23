package com.skillmate.skillmate.modules.goals.useCases;

import java.util.Map;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillmate.skillmate.modules.goals.dto.AIGoalSuggestionDTO;

@Service
public class GetAIGoalSuggestionUseCase {

  private final OllamaChatModel chatModel;
  private final ObjectMapper objectMapper;

  public GetAIGoalSuggestionUseCase(OllamaChatModel chatModel, ObjectMapper objectMapper) {
    this.chatModel = chatModel;
    this.objectMapper = objectMapper;
  }

  public AIGoalSuggestionDTO execute(String userExperience, String desiredSkill) {
    String promptTemplate = """
        Você é um assistente especializado em criar metas de aprendizado personalizadas.

        IMPORTANTE: Use APENAS as informações fornecidas pelo usuário. NÃO invente ou adicione detalhes que não foram mencionados.

        Experiência do usuário: "{userExperience}"
        Habilidade desejada: "{desiredSkill}"

        Crie uma sugestão de meta de aprendizado estruturada. Responda APENAS em formato JSON válido com a seguinte estrutura:
        {{
          "title": "Título da meta de aprendizado",
          "experience": "Descrição baseada EXATAMENTE na experiência informada pelo usuário, sem adicionar informações não mencionadas",
          "suggestedHoursPerDay": número_inteiro,
          "suggestedDaysPerWeek": número_inteiro,
          "reasoning": "Explicação do porquê dessas recomendações baseada apenas nas informações fornecidas"
        }}

        Regras importantes:
        - Use APENAS as informações fornecidas em "{userExperience}" e "{desiredSkill}"
        - NÃO mencione outras tecnologias, linguagens ou ferramentas que não foram citadas
        - NÃO invente detalhes sobre a experiência do usuário
        - Horas diárias: entre 1 e 4 horas
        - Dias por semana: entre 3 e 7 dias
        """;

    PromptTemplate template = new PromptTemplate(promptTemplate);
    Prompt prompt = template.create(Map.of(
        "userExperience", userExperience != null ? userExperience : "",
        "desiredSkill", desiredSkill != null ? desiredSkill : ""));

    String response = chatModel.call(prompt).getResult().getOutput().getContent();

    return parseAIResponse(response, desiredSkill);
  }

  private AIGoalSuggestionDTO parseAIResponse(String response, String desiredSkill) {
    AIGoalSuggestionDTO suggestion = new AIGoalSuggestionDTO();

    try {
      String jsonContent = extractJsonFromResponse(response);

      JsonNode jsonNode = objectMapper.readTree(jsonContent);

      suggestion.setTitle(
          jsonNode.has("title") ? jsonNode.get("title").asText() : "Learning Goal for " + desiredSkill);
      suggestion.setExperience(
          jsonNode.has("experience") ? jsonNode.get("experience").asText()
              : "Based on your experience, this goal will help you master " + desiredSkill
                  + " through structured learning.");
      suggestion.setSuggestedHoursPerDay(
          jsonNode.has("suggestedHoursPerDay") ? jsonNode.get("suggestedHoursPerDay").asInt() : 2);
      suggestion.setSuggestedDaysPerWeek(
          jsonNode.has("suggestedDaysPerWeek") ? jsonNode.get("suggestedDaysPerWeek").asInt() : 5);
      suggestion.setReasoning(
          jsonNode.has("reasoning") ? jsonNode.get("reasoning").asText()
              : "Suggested based on your experience level and the complexity of " + desiredSkill + ".");

    } catch (Exception e) {
      suggestion.setTitle("Learning Goal for " + desiredSkill);
      suggestion.setExperience(
          "Based on your experience, this goal will help you master " + desiredSkill + " through structured learning.");
      suggestion.setSuggestedHoursPerDay(2);
      suggestion.setSuggestedDaysPerWeek(5);
      suggestion.setReasoning("Suggested based on your experience level.");
    }

    return suggestion;
  }

  private String extractJsonFromResponse(String response) {
    response = response.replaceAll("```json", "").replaceAll("```", "").trim();

    int start = response.indexOf('{');
    int end = response.lastIndexOf('}');

    if (start >= 0 && end > start) {
      return response.substring(start, end + 1);
    }

    return response;
  }
}
