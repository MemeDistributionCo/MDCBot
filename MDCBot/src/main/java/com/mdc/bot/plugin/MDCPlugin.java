package com.mdc.bot.plugin;

import com.mdc.bot.MDCBot;

/**
 * Interface used to create plugins
 * @author xDest
 *
 */
public interface MDCPlugin {
	
	/**
	 * Enable the plugin
	 * @param b The MDCBot
	 */
	void enable(MDCBot b);
	/**
	 * Disable the plugin
	 * @param b The MDCBot
	 */
	void disable(MDCBot b);

}
