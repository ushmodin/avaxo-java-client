package com.artmark.avaxo.config;

import com.artmark.util.Utils;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.support.RetryTemplate;

/**
 * @author Ushmodin N.
 * @since 11.01.2016 15:01
 */

@Configuration
public class MQConfig {


	@Bean
	public ConnectionFactory connectionFactory(@Value("${spring.rabbitmq.host:localhost}")String host,
											   @Value("${spring.rabbitmq.port:-1}")int port,
											   @Value("${spring.rabbitmq.username:}")String username,
											   @Value("${spring.rabbitmq.password:}")String password) {
		CachingConnectionFactory factory = new CachingConnectionFactory(host, port);
		if (!Utils.isEmpty(username)) {
			factory.setUsername(username);
		}
		if (!Utils.isEmpty(password)) {
			factory.setPassword(password);
		}
		return factory;
	}


	@Bean
	public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
		return rabbitTemplate;
	}

	@Bean
	public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
		return new RabbitAdmin(connectionFactory);
	}
}
