package com.mdc.bot.util.event;

import com.mdc.bot.MDCBot;

import net.dv8tion.jda.core.events.Event;

/**
 * Base class for creating custom events.
 * @author xDest
 *
 */
public abstract class CEvent extends Event {

	protected final MDCBot bot;
	
	/**
	 * Create an event with the bot instance
	 * @param b The bot
	 */
	public CEvent(MDCBot b) {
		super(b.getJDAInstance());
		this.bot = b;
	}
	
	/**
	 * Get the bot instance for this event
	 * @return The Bot
	 */
	public final MDCBot getBot() {
		return this.bot;
	}

}
