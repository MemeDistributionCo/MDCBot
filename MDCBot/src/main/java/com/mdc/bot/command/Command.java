package com.mdc.bot.command;

import com.mdc.bot.MDCBot;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * Command interface for all commands under a single name.
 * @author xDestx
 *
 */
public interface Command {
	public static final String COMMAND_PREFIX = "--";
	/**
	 * Used to verify whether a command is valid
	 * @param s CommandSet for this command
	 * @param b The bot
	 * @return true, if valid command
	 */
	public boolean called(CommandSet s, MDCBot b);
	/**
	 * Used to run a command. Should be called after {@link #called(CommandSet, MDCBot)} returns true.
	 * @param s CommandSet for this command
	 * @param b The bot
	 */
	public void action(CommandSet s, MDCBot b);
	/**
	 * Get the help message for this command.
	 * @return HELP
	 */
	public String getHelpMessage();
	
	
	/**
	 * Get all child commands of this command. Ex /duel *accept* @user, accept is the child.
	 * @return Sub-commands of this command
	 */
	public Command[] getChildCommands();
	
	/**
	 * Tells whether this is the top level of a command tree
	 * @return true, if there are is no parent command
	 */
	public boolean isRootCommand();
	
	
	/**
	 * Get the parent command, if it exists. 
	 * @return The parent command, itself otherwise
	 */
	public Command getParentCommand();
	
	/**
	 * Parse text for command, return the command given or null if no command
	 * @param message Discord message content
	 * @param e The message event
	 * @return Command or null
	 */
	public static CommandSet parseCommand(String message, MessageReceivedEvent e) {
		if(message.startsWith(Command.COMMAND_PREFIX)) {
			String actualCommand = message.substring(2);
			String[] commandArgs = actualCommand.split(" ");
			if(commandArgs.length < 1) {
				return null;
			}
			String commandLabel = commandArgs[0];
			
			String[] temp = new String[commandArgs.length-1];
			for(int i = 1; i < commandArgs.length; i++) {
				temp[i-1] = commandArgs[i];
			}
			commandArgs = temp;
			for(CommandLabel label : CommandLabel.values()) {
				if(label.getLabel().equalsIgnoreCase(commandLabel)) {
					Class<?> commandClass = label.getCommandClass();
					try {
						Object commandObj = commandClass.newInstance();
						CommandSet cs = new CommandSet(commandLabel,commandArgs,(Command)commandObj, e);
						return cs;
					} catch (InstantiationException | IllegalAccessException er) {
						er.printStackTrace();
					}
				}
			}
			return null;
		} else {
			return null;
		}
	}
}
