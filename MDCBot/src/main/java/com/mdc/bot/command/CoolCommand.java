package com.mdc.bot.command;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CoolCommand implements Command {

	@Override
	public boolean called(String[] args, MessageReceivedEvent e) {
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent e) {
		MessageBuilder mb = new MessageBuilder();
		mb.append(e.getGuild().getEmotesByName("thecool", true).get(0));
		e.getTextChannel().sendMessage(mb.build()).complete();
	}

	@Override
	public String getHelpMessage() {
		return "AAA";
	}

}
