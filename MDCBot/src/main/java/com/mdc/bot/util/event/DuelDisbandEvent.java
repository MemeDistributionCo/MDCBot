package com.mdc.bot.util.event;

import com.mdc.bot.command.game.Duel;

public class DuelDisbandEvent extends CEvent {

	private final Duel duel;
	
	public DuelDisbandEvent(Duel d) {
		super(d.getBot());
		this.duel = d;
	}
	
	public final Duel getDuel() {
		return this.duel;
	}
	

}
