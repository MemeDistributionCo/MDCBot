package com.mdc.bot.command;

import com.mdc.bot.command.game.DuelCommand;

public enum CommandLabel {
	HELLO("hello", HelloCommand.class),SHUTDOWN("shutdown",ShutdownCommand.class),COOL("cool",CoolCommand.class),TTS("tts",TTSCommand.class),HELP("help",HelpCommand.class),FSpeak("fspeak",FSpeakCommand.class),ListCommand("commands",ListCommand.class),DUEL("duel",DuelCommand.class),VERSION("version",VersionCommand.class);
	
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
