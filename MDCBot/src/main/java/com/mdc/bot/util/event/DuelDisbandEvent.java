package com.mdc.bot.util.event;

import com.mdc.bot.command.game.Duel;

/**
 * Duel disband event
 * @author xDestx
 *
 */
public class DuelDisbandEvent extends CEvent {

	private final Duel duel;
	/**
	 * Create a duel disband event
	 * @param d The duel disbanded
	 */
	public DuelDisbandEvent(Duel d) {
		super(d.getBot());
		this.duel = d;
	}
	/**
	 * Get the disbanded duel
	 * @return The duel
	 */
	public final Duel getDuel() {
		return this.duel;
	}
	

}
