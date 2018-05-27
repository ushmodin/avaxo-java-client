package com.artmark.avaxo.config;

/**
 * Created by nikolay on 29.10.17.
 */
public class Settings {
	private Long id;
	private String commandQueue;
	private String heartbeatQueue;
	private boolean ready = false;

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCommandQueue() {
		return commandQueue;
	}

	public void setCommandQueue(String commandQueue) {
		this.commandQueue = commandQueue;
	}

	public String getHeartbeatQueue() {
		return heartbeatQueue;
	}

	public void setHeartbeatQueue(String heartbeatQueue) {
		this.heartbeatQueue = heartbeatQueue;
	}

	@Override
	public String toString() {
		return "Settings{" +
				"id=" + id +
				", commandQueue='" + commandQueue + '\'' +
				", heartbeatQueue='" + heartbeatQueue + '\'' +
				", ready=" + ready +
				'}';
	}
}
