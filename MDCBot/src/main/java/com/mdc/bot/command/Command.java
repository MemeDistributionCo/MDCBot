package com.mdc.bot.command;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface Command {
	public static final String COMMAND_PREFIX = "--";
	/**
	 * Used to verify whether a command is valid
	 * @param args CommandSet for this command
	 * @return true, if valid command
	 */
	public boolean called(CommandSet s);
	/**
	 * Used to run a command. Should be called after {@link #called(String[], MessageReceivedEvent)} returns true.
	 * @param s CommandSet for this command
	 */
	public void action(CommandSet s);
	/**
	 * Get the help message for this command.
	 * @return HELP
	 */
	public String getHelpMessage();
	
	
	/**
	 * Parse text for command, return the command given or null if no command
	 * @param message Discord message content
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
