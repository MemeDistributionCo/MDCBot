package com.mdc.bot.command;

import com.mdc.bot.MDCBot;

import net.dv8tion.jda.core.MessageBuilder;

public class ListCommand implements Command {

	@Override
	public boolean called(CommandSet s) {
		return true;
	}

	@Override
	public void action(CommandSet s) {
		MessageBuilder b = new MessageBuilder().append("Commands: \n");
		for(CommandLabel cl : CommandLabel.values()) {
			b.append("- " + cl.getLabel());
			b.append("\n");
		}
		MDCBot.sendMessage(s.getMessageReceivedEvent().getTextChannel(), b);
	}

	@Override
	public String getHelpMessage() {
		return "Usage: `--commands`";
	}

	
	
}
