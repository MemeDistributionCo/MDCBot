package com.mdc.bot.util.event;

import com.mdc.bot.command.game.Duel;
import com.mdc.bot.command.game.FightPlayer;


public class DuelAttackEvent extends CEvent {

	private final Duel d;
	private final FightPlayer attacker,defender,currentTurn;
	private final int damage;
	
	public DuelAttackEvent(Duel d, FightPlayer attacker, FightPlayer defender, FightPlayer currentTurn, int damage) {
		super(d.getBot());
		this.attacker = attacker;
		this.defender = defender;
		this.currentTurn = currentTurn;
		this.damage = damage;
		this.d = d;
	}
	
	public final Duel getDuel() {
		return this.d;
	}
	
	public final FightPlayer getAttacker() {
		return this.attacker;
	}
	
	public final FightPlayer getDefender() {
		return this.defender;
	}
	
	public final FightPlayer getCurrentTurn() {
		return this.currentTurn;
	}
	
	public final int getDamage() {
		return this.damage;
	}
	
}
