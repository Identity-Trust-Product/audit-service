package com.identityos.audit_service.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

  @Bean
  TopicExchange auditExchange(AuditProperties properties) {
    return new TopicExchange(properties.getExchangeName(), true, false);
  }

  @Bean
  Queue auditQueue(AuditProperties properties) {
    return new Queue(properties.getAuditQueue(), true);
  }

  @Bean
  Binding auditBinding(AuditProperties properties, Queue auditQueue, TopicExchange auditExchange) {
    return BindingBuilder.bind(auditQueue).to(auditExchange).with(properties.getRoutingKey());
  }

  @Bean
  MessageConverter messageConverter() {
    return new JacksonJsonMessageConverter();
  }

  @Bean
  SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
      ConnectionFactory connectionFactory, MessageConverter messageConverter) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setMessageConverter(messageConverter);
    factory.setDefaultRequeueRejected(true);
    factory.setPrefetchCount(1);
    return factory;
  }
}
