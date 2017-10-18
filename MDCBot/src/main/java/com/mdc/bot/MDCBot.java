package com.mdc.bot;

import com.mdc.bot.reaction.CoolReaction;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class MDCBot {
	
	
		//Please read the readme on the github repo (https://github.com/MemeDistributionCo/MDCBot)
		//Please do not make any changes on master or testing-merge-here
		//Can be found at https://github.com/DV8FromTheWorld/JDA
		private static JDA jda;
		private static final String LIVE_TOKEN = "MzY3NDk2MzI5OTQ3MTE5NjE4.DL8SHQ.nW_rtXFgD7ytS3j7_lzZqxb4D5c", TEST_TOKEN = "MzY4MjE2MjU0Njg0NTk0MTc2.DMGvnw.gm57DR4Ado7zYE9M75zBI9x-38c";
		
		//Version 1.0.0
		
		public static void main(String[] args) {
			try {
				//Creates a JDA (Java Discord API) instance of "Bot" type with the bot token below. Builds account with blocking (freezes until finished, versus aSync)
				/*
				 * Switch TEST_TOKEN to LIVE_TOKEN when merging to master
				 */
				String tokenToUse;
				if(args.length == 0) {
					tokenToUse = TEST_TOKEN;
				} else {
					try {
						boolean useLive = Boolean.parseBoolean(args[0]);
						if(useLive) {
							tokenToUse = LIVE_TOKEN;
						} else {
							tokenToUse = TEST_TOKEN;
						}
					} catch (Exception e) {
						System.out.println("Error--Not a boolean value");
						tokenToUse = TEST_TOKEN;
					}
				}
				jda = new JDABuilder(AccountType.BOT).setToken(tokenToUse).buildBlocking();
				//Command listener
				jda.addEventListener(new MessageListener());
				
				/*
				 * Reactions and other listeners
				 */
				jda.addEventListener(new CoolReaction());
				
				jda.setAutoReconnect(true);
			} catch (Exception e) {
				System.out.println("There was an error enabling the bot");
				e.printStackTrace();
			}
		}
	
}
