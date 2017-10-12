package com.mdc.bot;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class MDCBot {

	//Will work on trying to understand this later
		//Can be found at https://github.com/DV8FromTheWorld/JDA
		private static JDA jda;
		
		//Version 1.0.0 b0
		
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
	
}
