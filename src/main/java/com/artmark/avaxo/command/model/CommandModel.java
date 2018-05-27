package com.artmark.avaxo.command.model;

/**
 * @author Ushmodin N.
 * @since 12.02.2016 10:19
 */

public class CommandModel {
	private ForwardModel forward;
	private ScriptModel script;
	private Object status;

	public ForwardModel getForward() {
		return forward;
	}

	public void setForward(ForwardModel forward) {
		this.forward = forward;
	}

	public ScriptModel getScript() {
		return script;
	}

	public void setScript(ScriptModel script) {
		this.script = script;
	}

	public Object getStatus() {
		return status;
	}

	public void setStatus(Object status) {
		this.status = status;
	}
}
