package com.mdc.bot;

import javax.security.auth.login.LoginException;

import com.mdc.bot.reaction.CoolReaction;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.exceptions.RateLimitedException;

public class MDCBot {
	
	
		//Please read the readme on the github repo (https://github.com/MemeDistributionCo/MDCBot)
		//Please do not make any changes on master or testing-merge-here
		//Can be found at https://github.com/DV8FromTheWorld/JDA
		
		//private static JDA jda;
		//private static final String LIVE_TOKEN = "MzY3NDk2MzI5OTQ3MTE5NjE4.DL8SHQ.nW_rtXFgD7ytS3j7_lzZqxb4D5c", TEST_TOKEN = "MzY4MjE2MjU0Njg0NTk0MTc2.DMGvnw.gm57DR4Ado7zYE9M75zBI9x-38c";
		
		//Bot settings
		public static boolean ttsMode;
		
		
		private JDA jdaInstance;
		private String token;
		private boolean ttsEnabled;
		private boolean loggedIn;
		
		
		/**
		 * Attempts to construct a Bot with the provided token.
		 * @param token Token
		 * @throws LoginException
		 * @throws IllegalArgumentException
		 * @throws InterruptedException
		 * @throws RateLimitedException
		 */
		public MDCBot(String token)  {
			this.token = token;
			ttsEnabled = false;
			jdaInstance = null;
			this.loggedIn = false;
		}
		
		public void login() throws LoginException,IllegalArgumentException,InterruptedException, RateLimitedException {
			jdaInstance = new JDABuilder(AccountType.BOT).setToken(token).buildBlocking();
			
			//Command listener
			jdaInstance.addEventListener(new MessageListener(this));
			
			/*
			 * Reactions and other listeners
			 */
			jdaInstance.addEventListener(new CoolReaction(this));
			
			jdaInstance.setAutoReconnect(true);
			
			//if it makes it here, the bot is logged in
			this.loggedIn = true;
		}
		
		public String getToken() {
			return token;
		}
		
		public JDA getJDAInstance() {
			return this.jdaInstance;
		}
		
		public void setTTS(boolean v) {
			this.ttsEnabled = v;
		}
		
		public boolean isTTS() {
			return this.ttsEnabled;
		}
		
		public boolean isLoggedIn() {
			return this.loggedIn;
		}

		/**
		 * Sends a completed message to the text channel provided.
		 * @param tc
		 * @param message
		 */
		public void sendMessage(TextChannel tc, MessageBuilder message) {
			message.setTTS(ttsMode);
			tc.sendMessage(message.build()).complete();
		}
		
		//Version 1.1.0
		
		public static void main(String[] args) {
			args = new String[] {"true"};
			String LIVE_TOKEN = "MzY3NDk2MzI5OTQ3MTE5NjE4.DL8SHQ.nW_rtXFgD7ytS3j7_lzZqxb4D5c", TEST_TOKEN = "MzY4MjE2MjU0Njg0NTk0MTc2.DMGvnw.gm57DR4Ado7zYE9M75zBI9x-38c";
			String correctToken;
			if(args.length == 0) {
				correctToken = TEST_TOKEN;
			} else {
				try {
					boolean val = Boolean.parseBoolean(args[0]);
					if(val) {
						correctToken = LIVE_TOKEN;
					} else {
						correctToken = TEST_TOKEN;
					}
				} catch (Exception e) {
					correctToken = TEST_TOKEN;
				}
			}
			
			MDCBot newBot = new MDCBot(correctToken);
			
			try {
				newBot.login();
			} catch (LoginException | IllegalArgumentException | InterruptedException | RateLimitedException e) {
				e.printStackTrace();
				System.out.println("Unable to log in! Yikes!");
			}
			
		}
		
	
}
