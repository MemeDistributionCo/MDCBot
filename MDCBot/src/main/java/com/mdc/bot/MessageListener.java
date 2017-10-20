package com.mdc.bot;

import com.mdc.bot.command.Command;

import com.mdc.bot.command.CommandSet;

import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {
	
	private MDCBot bot;
	
	/**
	 * Requires Bot for commands
	 * @param botInstance
	 */
	public MessageListener(MDCBot botInstance) {
		this.bot = botInstance;
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		//Listen for commands
		if(e.getMessage().getContent().startsWith(Command.COMMAND_PREFIX)) {
			CommandSet c = Command.parseCommand(e.getMessage().getContent(), e);
			if(c != null) {
				if(c.getCommandInstance().called(c, bot)) {
					c.getCommandInstance().action(c, bot);
				} else {
					//Malformed command
				}
			} else {
				//Malformed command
			}
		}
	}
	
	@Override
	public void onReady(ReadyEvent e) {
		System.out.println("Login good! Logged in as " + e.getJDA().getSelfUser().getName());
	}
}
