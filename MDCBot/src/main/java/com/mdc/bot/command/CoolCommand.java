package com.mdc.bot.command;

import com.mdc.bot.MDCBot;

import net.dv8tion.jda.core.MessageBuilder;


public class CoolCommand implements Command {

	@Override
	public boolean called(CommandSet s) {
		return true;
	}

	@Override
	public void action(CommandSet s) {
		MessageBuilder mb = new MessageBuilder();
		mb.append(s.getMessageReceivedEvent().getGuild().getEmotesByName("thecool", true).get(0));
		MDCBot.sendMessage(s.getMessageReceivedEvent().getTextChannel(), mb);
	}

	@Override
	public String getHelpMessage() {
		return "AAA";
	}

}
