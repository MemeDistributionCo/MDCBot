package com.mdc.bot.reaction;

import com.mdc.bot.MDCBot;

import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * Abstract class to keep reactions together
 * @author xDest
 *
 */
public abstract class Reaction extends ListenerAdapter {

	protected final MDCBot b;
	
	/**
	 * Create a reaction with the current bot instance
	 * @param b The MDCBot
	 */
	public Reaction(MDCBot b) {
		this.b = b;
	}
	
	/**
	 * Get the bot this reaction is listening to
	 * @return The Bot instance
	 */
	public final MDCBot getBot() {
		return this.b;
	}
	
}
