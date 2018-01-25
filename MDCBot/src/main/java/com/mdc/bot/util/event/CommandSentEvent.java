package com.mdc.bot.util.event;

import com.mdc.bot.MDCBot;
import com.mdc.bot.command.CommandSet;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Event for when a user sends a valid command
 * @author xDest
 *
 */
public class CommandSentEvent extends CEvent {

	private final CommandSet s;
	private final MessageReceivedEvent e;
	
	/**
	 * Create a new CommandSentEvent with all components
	 * @param c The command set
	 * @param e The message sent
	 * @param b The bot
	 */
	public CommandSentEvent(CommandSet c, MessageReceivedEvent e, MDCBot b) {
		super(b);
		this.s = c;
		this.e = e;
	}
	
	/**
	 * Retrieve the command set generated from this command
	 * @return The command set
	 */
	public CommandSet getCommandSet() {
		return this.s;
	}
	
	/**
	 * Get the message event which caused this event
	 * @return The Message received event
	 */
	public MessageReceivedEvent getMessageReceivedEvent() {
		return this.e;
	}

}
