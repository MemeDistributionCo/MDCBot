package com.mdc.bot.util.event;

import com.mdc.bot.command.game.Duel;

import net.dv8tion.jda.core.entities.User;

/**
 * Duel request event
 * @author xDestx
 *
 */
public class DuelRequestEvent extends CEvent {

	private final Duel pendingDuel;
	private final User target, requester;
	
	/**
	 * Create a Duel Request Event
	 * @param requester The duel requester
	 * @param target The duel partner target
	 * @param duelRequest The duel request created
	 */
	public DuelRequestEvent(User requester, User target, Duel duelRequest) {
		super(duelRequest.getBot());
		this.target = target;
		this.requester = requester;
		this.pendingDuel = duelRequest;
	}
	
	/**
	 * Get the duel created by this event
	 * @return The duel
	 */
	public final Duel getDuel() {
		return this.pendingDuel;
	}
	
	/**
	 * Get the duel target
	 * @return The target
	 */
	public final User getTarget() {
		return this.target;
	}
	
	/**
	 * Get the user who created this request
	 * @return The requester
	 */
	public final User getRequester() {
		return this.requester;
	}

}
