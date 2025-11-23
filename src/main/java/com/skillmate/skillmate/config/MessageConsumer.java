package com.skillmate.skillmate.config;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.skillmate.skillmate.modules.goals.dto.GoalDTO;
import com.skillmate.skillmate.modules.users.dto.UserDTO;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class MessageConsumer {

  @RabbitListener(queues = RabbitMQConfig.GOAL_CREATED_QUEUE)
  public void receiveGoalCreated(GoalDTO goal) {
    log.info("Received goal created message: {}", goal.getId());
  }

  @RabbitListener(queues = RabbitMQConfig.GOAL_UPDATED_QUEUE)
  public void receiveGoalUpdated(GoalDTO goal) {
    log.info("Received goal updated message: {}", goal.getId());
  }

  @RabbitListener(queues = RabbitMQConfig.USER_CREATED_QUEUE)
  public void receiveUserCreated(UserDTO user) {
    log.info("Received user created message: {}", user.getId());
  }
}
