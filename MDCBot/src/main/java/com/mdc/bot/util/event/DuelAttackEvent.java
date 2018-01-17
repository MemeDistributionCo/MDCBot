package com.mdc.bot.util.event;

import com.mdc.bot.command.game.Duel;
import com.mdc.bot.command.game.FightPlayer;


/**
 * Custom event for duel attacks
 * @author xDestx
 *
 */
public class DuelAttackEvent extends CEvent {

	private final Duel d;
	private final FightPlayer attacker,defender,currentTurn;
	private final int damage;
	
	/**
	 * Create a new duel attack event
	 * @param d The duel for this event
	 * @param attacker The FightPlayer attacking
	 * @param defender The FightPlayer defending
	 * @param currentTurn The current fightplayer turn
	 * @param damage the damage
	 */
	public DuelAttackEvent(Duel d, FightPlayer attacker, FightPlayer defender, FightPlayer currentTurn, int damage) {
		super(d.getBot());
		this.attacker = attacker;
		this.defender = defender;
		this.currentTurn = currentTurn;
		this.damage = damage;
		this.d = d;
	}
	
	/**
	 * Get the duel for this event
	 * @return The duel
	 */
	public final Duel getDuel() {
		return this.d;
	}
	
	/**
	 * Get the attacker for this event
	 * @return FightPlayer attacker
	 */
	public final FightPlayer getAttacker() {
		return this.attacker;
	}
	
	/**
	 * Get the defender for this event
	 * @return FightPlayer defender
	 */
	public final FightPlayer getDefender() {
		return this.defender;
	}
	
	/**
	 * Get the current player turn
	 * @return FightPlayer current turn
	 */
	public final FightPlayer getCurrentTurn() {
		return this.currentTurn;
	}
	
	/**
	 * Get the damage in this event
	 * @return The damage
	 */
	public final int getDamage() {
		return this.damage;
	}
	
}
