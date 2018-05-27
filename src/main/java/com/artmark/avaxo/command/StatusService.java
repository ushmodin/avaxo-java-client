package com.artmark.avaxo.command;

import com.artmark.avaxo.config.Settings;
import com.google.common.collect.ImmutableMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Ushmodin N.
 * @since 12.02.2016 10:38
 */

@Component
class StatusService {
	private static final Logger log = LoggerFactory.getLogger(StatusService.class);
	private final AmqpTemplate amqpTemplate;
	private final Settings settings;

	@Autowired
	StatusService(AmqpTemplate amqpTemplate, Settings settings) {
		this.amqpTemplate = amqpTemplate;
		this.settings = settings;
	}

	public void sendStatus() {
		amqpTemplate.convertAndSend(settings.getHeartbeatQueue(),
				ImmutableMap.of("agentId", settings.getId(),
						"os", ImmutableMap.of("name", System.getProperty("os.name"))
				)
		);
		log.debug("Status was send");
	}
}
