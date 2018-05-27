package com.artmark.avaxo.command;

import com.artmark.avaxo.command.model.CommandModel;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author Ushmodin N.
 * @since 12.02.2016 10:18
 */
@Component
public class CommandService implements MessageListener {
	private final static Logger log = LoggerFactory.getLogger(CommandService.class);
	@Autowired
	private StatusService statusService;
	@Autowired
	private ForwardService forwardService;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public void onMessage(Message message) {
		try {
			CommandModel command = objectMapper.readValue(message.getBody(), CommandModel.class);
			if (command.getForward() != null) {
				forwardService.forward(command.getForward());
			}
			if (command.getStatus() != null) {
				statusService.sendStatus();
			}
		} catch (Exception e) {
			log.error(null, e);
		}
	}


}
