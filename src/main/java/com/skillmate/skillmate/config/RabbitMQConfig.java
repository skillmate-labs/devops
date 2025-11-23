package com.skillmate.skillmate.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  public static final String GOAL_CREATED_QUEUE = "goal.created";
  public static final String GOAL_UPDATED_QUEUE = "goal.updated";
  public static final String USER_CREATED_QUEUE = "user.created";

  @Bean
  public Queue goalCreatedQueue() {
    return new Queue(GOAL_CREATED_QUEUE, false);
  }

  @Bean
  public Queue goalUpdatedQueue() {
    return new Queue(GOAL_UPDATED_QUEUE, false);
  }

  @Bean
  public Queue userCreatedQueue() {
    return new Queue(USER_CREATED_QUEUE, false);
  }

  @Bean
  public MessageConverter jsonMessageConverter() {
    return new Jackson2JsonMessageConverter();
  }

  @Bean
  public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
    RabbitTemplate template = new RabbitTemplate(connectionFactory);
    template.setMessageConverter(jsonMessageConverter());
    return template;
  }

  @Bean
  public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
      ConnectionFactory connectionFactory) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setMessageConverter(jsonMessageConverter());
    return factory;
  }
}
