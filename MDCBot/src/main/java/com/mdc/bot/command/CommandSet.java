package com.mdc.bot.command;

import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CommandSet {
	
	private final String label;
	private final String[] args;
	private final Command c;
	private final MessageReceivedEvent e;
	private final User sender;
	
	public CommandSet(String label, String[] args, Command c, MessageReceivedEvent e) {
		this.label = label;
		this.sender = e.getAuthor();
		this.args = args;
		this.c = c;
		this.e = e;
	}
	
	public User getSender() {
		return this.sender;
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
