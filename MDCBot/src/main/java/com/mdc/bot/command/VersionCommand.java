package com.mdc.bot.command;

import com.mdc.bot.MDCBot;

/**
 * Command to get the bot version
 * @author xDestx
 *
 */
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
	
	@Override
	public boolean isRootCommand() {
		return getParentCommand() == this;
	}

	@Override
	public Command getParentCommand() {
		return this;
	}
	
}
