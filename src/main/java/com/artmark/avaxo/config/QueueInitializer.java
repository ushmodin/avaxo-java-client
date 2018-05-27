package com.artmark.avaxo.config;

import com.artmark.avaxo.command.CommandService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * Created by nikolay on 29.10.17.
 */
@Component
public class QueueInitializer {
	private static final Logger log = LoggerFactory.getLogger(QueueInitializer.class);
	private final Settings settings;
	private final ConnectionFactory connectionFactory;
	private final CommandService commandService;

	public QueueInitializer(Settings settings, ConnectionFactory connectionFactory, CommandService commandService) {
		this.settings = settings;
		this.connectionFactory = connectionFactory;
		this.commandService = commandService;
	}

	public void initQueues(Settings settings) {
		BeanUtils.copyProperties(settings, this.settings);
		settings.setReady(true);
		final SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer(connectionFactory);
		listenerContainer.addQueueNames(settings.getCommandQueue());
		listenerContainer.setMessageListener(commandService);
		listenerContainer.start();
		log.debug("Command queue listener started");
	}
}
