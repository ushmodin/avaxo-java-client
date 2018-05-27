package com.artmark.avaxo.command.model;

import com.artmark.forward.ConnectionParam;

/**
 * @author Ushmodin N.
 * @since 12.02.2016 10:19
 */

public class ForwardModel {
	private ConnectionParam target;
	private ConnectionParam manager;
	private int timeout = ConnectionParam.DEFAULT_TIMEOUT;

	public ConnectionParam getTarget() {
		return target;
	}

	public void setTarget(ConnectionParam target) {
		this.target = target;
	}

	public ConnectionParam getManager() {
		return manager;
	}

	public void setManager(ConnectionParam manager) {
		this.manager = manager;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}
}
