package com.artmark.avaxo.command;

import com.artmark.avaxo.config.QueueInitializer;
import com.artmark.avaxo.config.Settings;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Created by nikolay on 29.10.17.
 */
@Component
public class RegistrationService implements Runnable {
	private static final Logger log = LoggerFactory.getLogger(RegistrationService.class);
	private final AmqpTemplate amqpTemplate;
	private final AmqpAdmin amqpAdmin;
	private final QueueInitializer queueInitializer;
	private final String name;


	@Autowired
	public RegistrationService(@Value("${agent.name}") String name, AmqpTemplate amqpTemplate, AmqpAdmin amqpAdmin, QueueInitializer queueInitializer) {
		this.amqpTemplate = amqpTemplate;
		this.name = name;
		this.amqpAdmin = amqpAdmin;
		this.queueInitializer = queueInitializer;
	}

	@Override
	public void run() {
		for (;;) {
			String callbackQueue = createTemporaryQueue();
			amqpTemplate.convertAndSend("", "avaxo.greetings", ImmutableMap.of("from", name, "callbackQueue", callbackQueue));
			log.debug("Send greeting");
			final int timeout = 30 * 1000;
			Message response = amqpTemplate.receive(callbackQueue, timeout);
			if (response == null) {
				log.warn("Timeout waiting greeting response");
				continue;
			}
			try {
				Settings greetingResponse = new ObjectMapper().readValue(response.getBody(), Settings.class);
				if (isValidResponse(greetingResponse)) {
					log.warn("Invalid greeting response {}", greetingResponse);
					continue;
				}
				log.debug("Greeting response {}", greetingResponse);
				queueInitializer.initQueues(greetingResponse);
				break;
			} catch (Exception e) {
				log.error("Error while parse response", e);
			}
		}
	}

	private boolean isValidResponse(Settings greetingResponse) {
		return greetingResponse.getId() == null
				|| Strings.isNullOrEmpty(greetingResponse.getCommandQueue())
				|| Strings.isNullOrEmpty(greetingResponse.getHeartbeatQueue());
	}

	private String createTemporaryQueue() {
		String callbackQueue;
		for (;;) {
			try {
				callbackQueue = amqpAdmin.declareQueue(new Queue("", false, false, true));
				log.debug("Temporary {} queue created", callbackQueue);
				break;
			} catch (Exception e) {
				log.error("Error while create temporary queue", e);
				final int timeout = 60 * 1000;
				try {
					Thread.sleep(timeout);
				} catch (InterruptedException e1) {
				}
			}
		}
		return callbackQueue;
	}
}
