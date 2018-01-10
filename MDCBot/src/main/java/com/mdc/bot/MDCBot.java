package com.mdc.bot;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import javax.security.auth.login.LoginException;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;

import com.mdc.bot.reaction.CoolReaction;
import com.mdc.bot.reaction.DuelReaction;
import com.mdc.bot.reaction.UpdankReaction;
import com.mdc.bot.util.Util;
import com.mdc.bot.util.event.CEvent;
import com.mdc.bot.util.event.CEventListener;
import com.mdc.bot.util.event.RListener;
import com.mdc.bot.util.exception.TokenNotFoundException;

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
		
		//Bot settings
		
		
		private JDA jdaInstance;
		private String token;
		private boolean ttsEnabled;
		private boolean loggedIn;
		private CEventListener customListener;
		private final String version = "2.3.0";
		private final ScheduledExecutorService scheduler;
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
			customListener = new CEventListener(this);
			/*
			 * Register custom event listeners
			 */
			customListener.registerListener(new DuelReaction(this));
			scheduler = Executors.newScheduledThreadPool(10);
		}
		
		/**
		 * Register a {@link RListener} to listen for custom {@link CEvent}s.
		 * @param r The listener
		 */
		public void registerRListener(RListener r) {
			this.customListener.registerListener(r);
		}
		
		/**
		 * Unregister a {@link RListener} to listen for custom {@link CEvent}s.
		 * @param r The listener
		 * @return The listener if it was removed, null otherwise.
		 */
		public RListener unregisterRListener(RListener r) {
			return this.customListener.unregisterListener(r);
		}
		
		/**
		 * Get the custom event listener
		 * @return
		 */
		public CEventListener getCEventListener() {
			return this.customListener;
		}
		
		public void login() throws LoginException,IllegalArgumentException,InterruptedException, RateLimitedException {
			jdaInstance = new JDABuilder(AccountType.BOT).setToken(token).buildBlocking();
			
			//Command listener
			jdaInstance.addEventListener(new MessageListener(this));
			
			/*
			 * Reactions and other listeners
			 */
			jdaInstance.addEventListener(new CoolReaction(this));
			jdaInstance.addEventListener(new UpdankReaction(this));
			
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
			message.setTTS(this.isTTS());
			tc.sendMessage(message.build()).complete();
		}
		
		
		public void sendMessage(TextChannel tc, String message) {
			MessageBuilder mb = new MessageBuilder();
			mb.setTTS(this.isTTS());
			mb.append(message);
			tc.sendMessage(mb.build()).complete();
		}
		
		/**
		 * Invoke a custom event
		 * @param e
		 */
		public void invokeEvent(CEvent e) {
			this.customListener.invokeEvent(e);
		}
		
		/**
		 * Get MDCBot version
		 * @return Version as String
		 */
		public String getVersion() {
			return this.version;
		}

		
		public ScheduledExecutorService getScheduler() {
			return scheduler;
		}
		
		public void shutdown() {
			shutdown(null);
		}
		
		public void shutdown(TextChannel c) {
			if(c!=null) sendMessage(c, "Au Revoir");
			this.getJDAInstance().shutdown();
			getScheduler().shutdownNow();
		}

		//Version 2.0.0
		
		private static MDCGui frame;
		
		public static void main(String[] args) {
			frame = new MDCGui();
			String token;
			try {
				token = Util.readToken();
			} catch (IOException e) {
				e.printStackTrace();
				//System.out.println("Could not read or write token file...");
				frame.println("Could not read or write token file...");
				System.exit(1);
				return;
			} catch (TokenNotFoundException e) {
				//System.out.println(e.getMessage());
				frame.println(e.getMessage());
				//System.out.println("Token file path: " + e.getTokenPath());
				frame.println("Token file path: " + e.getTokenPath());
				//System.exit(1);
				//return;
				token = "";
			}
			
			MDCBot newBot = new MDCBot(token);
			
			try {
				newBot.login();
			} catch (LoginException | IllegalArgumentException | InterruptedException | RateLimitedException e) {
				e.printStackTrace();
				frame.println("Unable to log in! Yikes! (Invalid token?)");
				//System.out.println("Unable to log in! Yikes! (Invalid token?)");
			}
			
		}
		
		public static void print(String s) {
			frame.print(s);
		}
		
		public static void println(String s) {
			frame.println(s);
		}
		
		
		static class MDCGui extends JFrame {

			/**
			 * 
			 */
			private static final long serialVersionUID = 4037835942100275817L;
			private JList<String> messageList;
			private DefaultListModel<String> listModel;
			private MDCBot bot;
			
			public MDCGui() {
				setSize(1600/4*3,900/4*3); 
				setResizable(false);
				
				listModel = new DefaultListModel<String>();
				messageList = new JList<String>(listModel);
				messageList.setSize(1600/4*3, 900/4*3);
				messageList.setFixedCellHeight(30);
				JScrollPane jsp = new JScrollPane(messageList);
				jsp.setSize(1600/4*3,900/4*3);
				add(jsp);
				setVisible(true);
				setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
				addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						if((e.getKeyCode() == KeyEvent.VK_W || e.getKeyCode() == KeyEvent.VK_C) && e.isControlDown()) {
							closeWindow();
						}
					}
				});
				bot = null;
				jsp.addKeyListener(this.getKeyListeners()[0]);
				messageList.addKeyListener(this.getKeyListeners()[0]);
			}
			
			public MDCBot getBot() {
				return bot;
			}
			
			public void setBot(MDCBot b) {
				this.bot = b;
			}
			
			
			public void closeWindow() {
				//And bot
				if(bot != null) {
					bot.shutdown();
				}
				setVisible(false);
				dispose();
				System.exit(0);
			}
			
			public void print(String s) {
				List<String> toAppend = new ArrayList<String>();
				String lastStr;
				if(listModel.getSize() == 0) {
					lastStr = getDate();
				} else {
					lastStr = listModel.getElementAt(listModel.getSize()-1);
				}
				if(s.split("\n").length > 0) {
					lastStr+=s.split("\n")[0];
					for(int i = 1; i < s.split("\n").length; i++) {
						toAppend.add(getDate() + s.split("\n")[i]);
					}
				} else {
					lastStr+=s;
				}

				if(listModel.getSize() != 0) {
					listModel.set(listModel.getSize()-1, lastStr);
				} else {
					listModel.addElement(lastStr);
				}
				for(String str : toAppend) {
					listModel.addElement(str);
				}
				System.out.print(s);
			}
			
			public void println(String s) {
				listModel.addElement(getDate() + s);
				System.out.println(getDate() + s);
			}
			
			public String getDate() {
				return "[" + Calendar.getInstance().getTime().toString() + "]: ";
			}
			
			
		}
	
}
