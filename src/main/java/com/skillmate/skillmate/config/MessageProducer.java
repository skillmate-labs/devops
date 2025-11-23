package com.skillmate.skillmate.config;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.skillmate.skillmate.modules.goals.dto.GoalDTO;
import com.skillmate.skillmate.modules.users.dto.UserDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MessageProducer {

  private final RabbitTemplate rabbitTemplate;

  public void sendGoalCreated(GoalDTO goal) {
    try {
      rabbitTemplate.convertAndSend(RabbitMQConfig.GOAL_CREATED_QUEUE, goal);
      log.info("Goal created message sent: {}", goal.getId());
    } catch (Exception e) {
      log.error("Error sending goal created message", e);
    }
  }

  public void sendGoalUpdated(GoalDTO goal) {
    try {
      rabbitTemplate.convertAndSend(RabbitMQConfig.GOAL_UPDATED_QUEUE, goal);
      log.info("Goal updated message sent: {}", goal.getId());
    } catch (Exception e) {
      log.error("Error sending goal updated message", e);
    }
  }

  public void sendUserCreated(UserDTO user) {
    try {
      rabbitTemplate.convertAndSend(RabbitMQConfig.USER_CREATED_QUEUE, user);
      log.info("User created message sent: {}", user.getId());
    } catch (Exception e) {
      log.error("Error sending user created message", e);
    }
  }
}
