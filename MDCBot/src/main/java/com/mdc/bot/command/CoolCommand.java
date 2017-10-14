package com.mdc.bot.command;

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
		s.getMessageReceivedEvent().getTextChannel().sendMessage(mb.build()).complete();
	}

	@Override
	public String getHelpMessage() {
		return "AAA";
	}

}
