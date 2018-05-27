package com.artmark.avaxo.command;

import com.artmark.avaxo.command.model.ForwardModel;
import com.artmark.forward.OutOutExchanger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutorService;

/**
 * @author Ushmodin N.
 * @since 12.02.2016 10:46
 */
@Component
class ForwardService  {
	private final static Logger log = LoggerFactory.getLogger(ForwardService.class);
	@Autowired
	private ExecutorService executor;

	public void forward(ForwardModel param) {
		OutOutExchanger exchanger = new OutOutExchanger(executor, param.getTarget(), param.getManager(), param.getTimeout());
		executor.execute(exchanger);
	}
}
