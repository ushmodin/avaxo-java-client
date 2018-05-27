package com.artmark.avaxo.ping;

import com.artmark.avaxo.config.Settings;
import com.google.common.collect.ImmutableMap;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * @author Ushmodin N.
 * @since 13.01.2016 15:59
 */

@Service
class PingService {
	private final AmqpTemplate amqpTemplate;
	private final Settings settings;

	@Autowired
	PingService(AmqpTemplate amqpTemplate, Settings settings) {
		this.amqpTemplate = amqpTemplate;
		this.settings = settings;
	}


	@Scheduled(cron = "0 */5 * * * *")
	public void ping() {
		if (settings.isReady()) {
			amqpTemplate.convertAndSend(settings.getHeartbeatQueue(), ImmutableMap.of("agentId", settings.getId()));
		}
	}

}
