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
	 */
	void enable(MDCBot b);
	/**
	 * Disable the plugin
	 */
	void disable(MDCBot b);

}
