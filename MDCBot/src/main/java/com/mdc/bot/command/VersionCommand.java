package com.mdc.bot.command;

import com.mdc.bot.MDCBot;

public class VersionCommand implements Command {

	@Override
	public boolean called(CommandSet s, MDCBot b) {
		return true;
	}

	@Override
	public void action(CommandSet s, MDCBot b) {
		//Try to load from build.gradle?
		//Doesn't work, just include internal version and REMEMBER to change it
		String version = "Version: "+b.getVersion();
		b.sendMessage(s.getTextChannel(), version);
	}

	@Override
	public String getHelpMessage() {
		return "Usage: `--version`";
	}

	
	@Override
	public Command[] getChildCommands() {
		return new Command[0];
	}
}
