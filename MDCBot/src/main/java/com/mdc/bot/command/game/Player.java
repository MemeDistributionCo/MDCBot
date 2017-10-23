package com.mdc.bot.command.game;

import net.dv8tion.jda.core.entities.User;

/**
 * Interface for players in any game
 * @author xDest
 *
 */
public interface Player {

	/**
	 * Get the {@link net.dv8tion.jda.core.entities.User User} for this {@link Player}.
	 * @return
	 */
	public User getUser();
	/**
	 * Return the {@link net.dv8tion.jda.core.entities.User#getIdLong() longId} of the {@link net.dv8tion.jda.core.entities.User User}.
	 * @return the id
	 */
	public long getUserId();
	
}
