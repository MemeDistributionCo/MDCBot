package com.mdc.bot.command;

import com.mdc.bot.MDCBot;

import net.dv8tion.jda.core.MessageBuilder;

/**
 * List Command which displays all possible commands
 * @author xDestx
 *
 */
public class ListCommand implements Command {

	@Override
	public boolean called(CommandSet s, MDCBot b) {
		return true;
	}

	@Override
	public void action(CommandSet s, MDCBot bo) {
		MessageBuilder b = new MessageBuilder().append("Commands: \n");
		for(CommandLabel cl : CommandLabel.values()) {
			b.append("- " + cl.getLabel());
			b.append("\n");
		}
		bo.sendMessage(s.getMessageReceivedEvent().getTextChannel(), b);
	}

	@Override
	public String getHelpMessage() {
		return "Usage: `--commands`";
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
