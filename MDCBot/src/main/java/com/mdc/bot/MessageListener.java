package com.mdc.bot;

import com.mdc.bot.command.Command;
import com.mdc.bot.command.CommandSet;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

	//Will work on trying to understand this later
	//Can be found at https://github.com/DV8FromTheWorld/JDA
	private static JDA jda;
	
	public static void main(String[] args) {
		try {
			//Creates a JDA (Java Discord API) instance of "Bot" type with the bot token below. Builds account with blocking (freezes until finished, versus aSync)
			jda = new JDABuilder(AccountType.BOT).setToken("MzY3NDk2MzI5OTQ3MTE5NjE4.DL8SHQ.nW_rtXFgD7ytS3j7_lzZqxb4D5c").buildBlocking();
			jda.addEventListener(new MessageListener());
			jda.setAutoReconnect(true);
		} catch (Exception e) {
			System.out.println("There was an error enabling the bot");
			e.printStackTrace();
		}
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		//Listen for commands
		if(e.getMessage().getContent().startsWith(Command.COMMAND_PREFIX) && !e.getAuthor().isBot()) {
			CommandSet c = Command.parseCommand(e.getMessage().getContent(), e);
			if(c != null) {
				c.getCommandInstance().action(c.getArgs(), c.getMessageReceivedEvent());
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
