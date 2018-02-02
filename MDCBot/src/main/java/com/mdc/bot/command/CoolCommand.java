package com.mdc.bot.command;

import com.mdc.bot.MDCBot;

import net.dv8tion.jda.core.MessageBuilder;

/**
 * A command to send a :thecool: emoji
 * @author xDestx
 *
 */
public class CoolCommand implements Command {

	@Override
	public boolean called(CommandSet s, MDCBot b) {
		return true;
	}

	@Override
	public void action(CommandSet s, MDCBot b) {
		MessageBuilder mb = new MessageBuilder();
		mb.append(s.getMessageReceivedEvent().getGuild().getEmotesByName("thecool", true).get(0));
		b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), mb);
	}

	@Override
	public String getHelpMessage() {
		return "Usage: `--cool`";
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
