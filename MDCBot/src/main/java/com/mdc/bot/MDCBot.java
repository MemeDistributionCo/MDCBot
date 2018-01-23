package com.mdc.bot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

import javax.security.auth.login.LoginException;

import com.mdc.bot.command.CommandLabel;
import com.mdc.bot.command.CoolCommand;
import com.mdc.bot.command.DivinePunishmentCommand;
import com.mdc.bot.command.FSpeakCommand;
import com.mdc.bot.command.GameReqCommand;
import com.mdc.bot.command.HelloCommand;
import com.mdc.bot.command.HelpCommand;
import com.mdc.bot.command.ListCommand;
import com.mdc.bot.command.ShutdownCommand;
import com.mdc.bot.command.TTSCommand;
import com.mdc.bot.command.TimeoutCommand;
import com.mdc.bot.command.UpdankCommand;
import com.mdc.bot.command.VersionCommand;
import com.mdc.bot.command.game.DuelCommand;
import com.mdc.bot.plugin.MDCPlugin;
import com.mdc.bot.plugin.exception.PluginTXTNotFoundException;
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

/**
 * MDCBot Wrapper class. This class is the entire bot, and should be used for interaction.
 * @author xDestx
 *
 */
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
		private final String version = "2.3.2";
		private final ScheduledExecutorService scheduler;
		private final Set<MDCPlugin> plugins;
		private final Set<CommandLabel> commands;
		
		
		/**
		 * Attempts to construct a Bot with the provided token.
		 * @param token Bot Token
		 */
		public MDCBot(String token)  {
			this.token = token;
			ttsEnabled = false;
			jdaInstance = null;
			this.loggedIn = false;
			commands = new HashSet<CommandLabel>();
			loadDefaultCommands();
			customListener = new CEventListener(this);
			/*
			 * Register custom event listeners
			 */
			registerRListener(new DuelReaction(this));
			scheduler = Executors.newScheduledThreadPool(10);
			/*
			 * Load plugins
			 */
			plugins = new HashSet<MDCPlugin>();
			loadPlugins();
			enablePlugins();
		}
		
		public void registerCommand(CommandLabel label) {
			this.commands.add(label);
		}
		
		public void unregisterCommand(CommandLabel label) {
			this.commands.remove(label);
		}
		
		protected void loadDefaultCommands() {
			registerCommand(new CommandLabel("hello", HelloCommand.class));
			registerCommand(new CommandLabel("shutdown",ShutdownCommand.class));
			registerCommand(new CommandLabel("cool",CoolCommand.class));
			registerCommand(new CommandLabel("tts",TTSCommand.class));
			registerCommand(new CommandLabel("help",HelpCommand.class));
			registerCommand(new CommandLabel("fspeak",FSpeakCommand.class));
			registerCommand(new CommandLabel("commands",ListCommand.class));
			registerCommand(new CommandLabel("duel",DuelCommand.class));
			registerCommand(new CommandLabel("game",GameReqCommand.class));
			registerCommand(new CommandLabel("version",VersionCommand.class));
			registerCommand(new CommandLabel("timeout",TimeoutCommand.class));
			registerCommand(new CommandLabel("updank",UpdankCommand.class));
			registerCommand(new CommandLabel("divine",DivinePunishmentCommand.class));
		}
		
		public CommandLabel[] getCommandLabels() {
			CommandLabel[] lbls = new CommandLabel[commands.size()];
			lbls = commands.toArray(lbls);
			return lbls;
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
		 * @return The event listener
		 */
		public CEventListener getCEventListener() {
			return this.customListener;
		}
		
		/**
		 * Attempt to have the bot login.
		 * @throws LoginException On login failure
		 * @throws IllegalArgumentException On malformed token
		 * @throws InterruptedException On net failure
		 * @throws RateLimitedException On other failure (I apologize)
		 */
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
		
		/**
		 * Retrieve the bots token
		 * @return The token
		 */
		public String getToken() {
			return token;
		}
		
		/**
		 * Get the JDA Instance of the bot
		 * @return The JDA instance
		 */
		public JDA getJDAInstance() {
			return this.jdaInstance;
		}
		
		/**
		 * Set whether all bot messages should be TTS enabled
		 * @param v True or False
		 */
		public void setTTS(boolean v) {
			this.ttsEnabled = v;
		}
		
		/**
		 * Check whether the bot is currently TTS enabled
		 * @return True if enabled, otherwise false
		 */
		public boolean isTTS() {
			return this.ttsEnabled;
		}
		
		/**
		 * Check if the bot is currently logged in
		 * @return true, if logged in. 
		 */
		public boolean isLoggedIn() {
			return this.loggedIn;
		}

		/**
		 * Sends a completed message to the text channel provided.
		 * @param tc The text channel
		 * @param message The message builder message
		 */
		public void sendMessage(TextChannel tc, MessageBuilder message) {
			message.setTTS(this.isTTS());
			tc.sendMessage(message.build()).complete();
		}
		
		
		/**
		 * Sends a message to the specified text channel
		 * @param tc The text channel
		 * @param message The desired message
		 */
		public void sendMessage(TextChannel tc, String message) {
			MessageBuilder mb = new MessageBuilder();
			mb.setTTS(this.isTTS());
			mb.append(message);
			tc.sendMessage(mb.build()).complete();
		}
		
		/**
		 * Invoke a custom event
		 * @param e The event
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

		/**
		 * Get the Bot scheduler
		 * @return Scheduler instance
		 */
		public ScheduledExecutorService getScheduler() {
			return scheduler;
		}

		/**
		 * Load plugins from the MDCBot/Plugins directory
		 */
		protected void loadPlugins() {
			/*
			 * Find plugins folder
			 */
			File f = new File(Util.BOT_PATH + File.separatorChar + "Plugins");
			if(!f.exists()) {
				f.mkdirs();
			}
			for(String fileName : f.list(new FilenameFilter() {@Override public boolean accept(File dir, String name) { return name.endsWith(".jar");}})) {
				File possiblePlugin = new File(f.getPath() + File.separatorChar + fileName);
				try {
					JarFile jar = new JarFile(possiblePlugin);
					ZipEntry pluginTxt = jar.getEntry("plugin.txt");
					if(pluginTxt == null) {
						jar.close();
						throw new PluginTXTNotFoundException(fileName);
					}
					BufferedReader br = new BufferedReader(new InputStreamReader(jar.getInputStream(pluginTxt)));
					String line = br.readLine();
					if(line.contains("main:")) {
						line = line.replace("main:", "").trim();
						URL loc = possiblePlugin.toURI().toURL();
						URLClassLoader loader = new URLClassLoader(new URL[] {loc});
						Class<?> loadedClass = loader.loadClass(line);
						for(Class<?> i : loadedClass.getInterfaces()) {
							if(i.getName().equals("com.mdc.bot.plugin.MDCPlugin")) {
								Object instance = loadedClass.newInstance();
								if(instance instanceof MDCPlugin) {
									MDCPlugin pl = (MDCPlugin)instance;
									//Load the rest of the classes
									plugins.add(pl);
									
									Enumeration<JarEntry> entries = jar.entries();
									while(entries.hasMoreElements()) {
										JarEntry entry = entries.nextElement();
										if(entry.getName().endsWith(".class")) {
											loader.loadClass(entry.getName().replace(".class","").replace(""+File.separatorChar,"."));
										}
									}
								}
							}
						}
						loader.close();
					}
					br.close();
					jar.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (PluginTXTNotFoundException e) {
					e.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (InstantiationException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		
		/**
		 * Enable all loaded plugins in {@link #plugins}
		 */
		protected void enablePlugins() {
			for(MDCPlugin p : plugins) {
				p.enable(this);
			}
		}
		
		/**
		 * Disable all loaded plugins in {@link #plugins}
		 */
		protected void disablePlugins() {
			for(MDCPlugin p : plugins) {
				p.disable(this);
			}
		}
		
		
		public void shutdown() {
			disablePlugins();
			jdaInstance.shutdown();
			getScheduler().shutdownNow();
		}
		
		public void shutdown(TextChannel c) {
			sendMessage(c,"Au Revoir");
			shutdown();
		}
		
		/**
		 * Main method to run bot currently
		 * @param args Args for bot (Not used)
		 */
		public static void main(String[] args) {
			String token;
			try {
				token = Util.readToken();
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("Could not read or write token file...");
				System.exit(1);
				return;
			} catch (TokenNotFoundException e) {
				System.out.println(e.getMessage());
				System.out.println("Token file path: " + e.getTokenPath());
				System.exit(1);
				return;
			}
			
			MDCBot newBot = new MDCBot(token);
			
			try {
				newBot.login();
			} catch (LoginException | IllegalArgumentException | InterruptedException | RateLimitedException e) {
				e.printStackTrace();
				System.out.println("Unable to log in! Yikes! (Invalid token?)");
			}
			
		}
		
	
}
