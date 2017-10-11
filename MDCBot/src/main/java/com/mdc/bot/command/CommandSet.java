package com.mdc.bot.command;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandSet {
	
	private final String label;
	private final String[] args;
	private final Command c;
	private final MessageReceivedEvent e;
	
	public CommandSet(String label, String[] args, Command c, MessageReceivedEvent e) {
		this.label = label;
		this.args = args;
		this.c = c;
		this.e = e;
	}
	
	public MessageReceivedEvent getMessageReceivedEvent() {
		return this.e;
	}
	
	public Command getCommandInstance() {
		return this.c;
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public String[] getArgs() {
		return this.args;
	}
	
}
