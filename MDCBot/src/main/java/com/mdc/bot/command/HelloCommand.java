package com.mdc.bot.command;

import net.dv8tion.jda.core.MessageBuilder;

public class HelloCommand implements Command {

	private String help;
	
	public HelloCommand()
	{
		help = "Suffer";
	}
	
	@Override
	public boolean called(CommandSet s) {
		//There is no way to call this wrong...
		return true;
	}

	@Override
	public void action(CommandSet s) {
		//getTextChannel because getChannel could return ... a voice channel?
		if(s.getMessageReceivedEvent().getTextChannel().canTalk()) {
			MessageBuilder mb = new MessageBuilder();
			mb.append("Hello, ");
			mb.append(s.getSender());
			mb.append("!");
			s.getMessageReceivedEvent().getTextChannel().sendMessage(mb.build()).complete();
		} else {
			System.out.println("Can't send messages in channel " + s.getMessageReceivedEvent().getChannel().getName());
		}
	}

	@Override
	public String getHelpMessage() {
		return help;
	}

}
