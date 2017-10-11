package com.mdc.bot.command;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class HelloCommand implements Command {

	private String help;
	
	public HelloCommand()
	{
		help = "Suffer";
	}
	
	@Override
	public boolean called(String[] args, MessageReceivedEvent e) {
		//There is no way to call this wrong...
		return true;
	}

	@Override
	public void action(String[] args, MessageReceivedEvent e) {
		//getTextChannel because getChannel could return ... a voice channel?
		if(e.getTextChannel().canTalk()) {
			e.getTextChannel().sendMessage("Hello!").complete();
		} else {
			System.out.println("Can't send messages in channel " + e.getChannel().getName());
		}
	}

	@Override
	public String getHelpMessage() {
		return help;
	}

}
