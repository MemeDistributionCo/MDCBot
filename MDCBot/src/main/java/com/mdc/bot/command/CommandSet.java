package com.mdc.bot.command;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * A wrapper class for a lot of used components in a command.
 * @author xDestx
 *
 */
public class CommandSet {
	
	private final String label;
	private final String[] args;
	private final Command c;
	private final MessageReceivedEvent e;
	private final User sender;
	private final Guild server;
	private final TextChannel channel;
	
	/**
	 * Construct a CommandSet
	 * @param label The label used
	 * @param args The arguments provided
	 * @param c The command instance
	 * @param e The actual message event which called the command
	 */
	public CommandSet(String label, String[] args, Command c, MessageReceivedEvent e) {
		this.label = label;
		this.sender = e.getAuthor();
		this.args = args;
		this.c = c;
		this.e = e;
		this.server = e.getGuild();
		this.channel = e.getTextChannel();
	}
	
	/**
	 * Get the text channel where the command was sent
	 * @return The text channel
	 */
	public TextChannel getTextChannel() {
		return this.channel;
	}
	
	/**
	 * Get the server in which the message was sent
	 * @return The server (Guild)
	 */
	public Guild getServer() {
		return this.server;
	}
	
	/**
	 * Get the user who sent the message
	 * @return The user instance
	 */
	public User getSender() {
		return this.sender;
	}
	
	/**
	 * Gets the message receieved event
	 * @return The message received event
	 */
	public MessageReceivedEvent getMessageReceivedEvent() {
		return this.e;
	}
	
	/**
	 * Gets the command instance
	 * @return The command instance
	 */
	public Command getCommandInstance() {
		return this.c;
	}
	
	/**
	 * Gets the command label
	 * @return The label
	 */
	public String getLabel() {
		return this.label;
	}
	
	/**
	 * Gets the command arguments (May be inaccurate when including names with spaces)
	 * @return The arguments
	 */
	public String[] getArgs() {
		return this.args;
	}
	
}
