package com.artmark.avaxo.command.model;

/**
 * @author Ushmodin N.
 * @since 12.02.2016 10:19
 */

public class ScriptModel {
	public enum Language {
		GROOVY
	}
	private Language language = Language.GROOVY;
	private String content;

	public Language getLanguage() {
		return language;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
