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
	
	public CEvent(MDCBot b) {
		super(b.getJDAInstance());
		this.bot = b;
	}
	
	public final MDCBot getBot() {
		return this.bot;
	}

}
