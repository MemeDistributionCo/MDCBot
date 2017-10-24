package com.mdc.bot.util.event;

import com.mdc.bot.MDCBot;
import com.mdc.bot.command.game.Duel;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.User;

public class DuelRequestEvent extends CEvent {

	private final Duel pendingDuel;
	private final User target, requester;
	
	public DuelRequestEvent(JDA api, MDCBot b, User requester, User target, Duel duelRequest) {
		super(api, b);
		this.target = target;
		this.requester = requester;
		this.pendingDuel = duelRequest;
	}
	
	public final Duel getDuel() {
		return this.pendingDuel;
	}
	
	public final User getTarget() {
		return this.target;
	}
	
	public final User getRequester() {
		return this.requester;
	}

}
