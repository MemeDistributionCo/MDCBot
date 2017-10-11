package com.mdc.bot.command;

public enum CommandLabel {
	HELLO("hello", HelloCommand.class);
	
	private String label;
	private Class<?> commandClass;
	
	CommandLabel(String label, Class<?> commandClass) {
		this.label = label;
		this.commandClass = commandClass;
	}
	
	String getLabel() {
		return this.label;
	}
	
	Class<?> getCommandClass() {
		return this.commandClass;
	}
	
}
