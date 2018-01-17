package com.mdc.bot.command;

import com.mdc.bot.command.game.DuelCommand;

/**
 * An enumerated type which is where all valid commands are placed.
 * @author xDestx
 *
 */
public enum CommandLabel {
	HELLO("hello", HelloCommand.class),SHUTDOWN("shutdown",ShutdownCommand.class),COOL("cool",CoolCommand.class),TTS("tts",TTSCommand.class),HELP("help",HelpCommand.class),FSpeak("fspeak",FSpeakCommand.class),ListCommand("commands",ListCommand.class),DUEL("duel",DuelCommand.class),REQUESTGAME("game",GameReqCommand.class),VERSION("version",VersionCommand.class),TIMEOUT("timeout",TimeoutCommand.class),UPDANK("updank",UpdankCommand.class),DIVINEPUNISHMENT("divine",DivinePunishmentCommand.class);

	
	private String label;
	private Class<?> commandClass;
	
	/**
	 * Command label constructor
	 * @param label The label
	 * @param commandClass The class to execute
	 */
	CommandLabel(String label, Class<?> commandClass) {
		this.label = label;
		this.commandClass = commandClass;
	}
	
	/**
	 * Get the label for this command
	 * @return The label
	 */
	String getLabel() {
		return this.label;
	}
	
	/**
	 * Get the executing class for this command
	 * @return The class
	 */
	Class<?> getCommandClass() {
		return this.commandClass;
	}
	
}
