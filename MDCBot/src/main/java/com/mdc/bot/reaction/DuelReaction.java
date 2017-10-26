package com.mdc.bot.reaction;

import com.mdc.bot.MDCBot;
import com.mdc.bot.command.game.Duel;
import com.mdc.bot.util.Util;
import com.mdc.bot.util.event.DuelAttackEvent;
import com.mdc.bot.util.event.DuelDisbandEvent;
import com.mdc.bot.util.event.DuelRequestEvent;
import com.mdc.bot.util.event.RListener;


/**
 * Duel reaction to allow the bot to engage in duels with other users
 * @author xDest
 *
 */
public class DuelReaction extends Reaction implements RListener {

	private Duel currentDuel;
	
	public DuelReaction(MDCBot b) {
		super(b);
		currentDuel = null;
	}
	
	/**
	 * Respond to duel request and attack.
	 * @param e
	 */
	public void onDuelRequest(DuelRequestEvent e) {
		//Accept duel
		//Check if the bot is in this duel??
		if(!Util.sameUser(e.getTarget(), this.getBot().getJDAInstance().getSelfUser())) return;
		e.getBot().sendMessage(e.getDuel().getChannel(), "--duel accept " + e.getRequester().getAsMention());
		currentDuel = e.getDuel();
		e.getBot().sendMessage(e.getDuel().getChannel(), "--duel attack");
		//Nice
	}
	
	/**
	 * Respond to duel attack
	 * @param e
	 */
	public void onDuelAttack(DuelAttackEvent e) {
		if(e.getDuel() != currentDuel) return;
		if(!Util.sameUser(e.getAttacker().getUser(), b.getJDAInstance().getSelfUser())) {
			//Opponent attacked
			if (e.getDamage() == 0) {
				b.sendMessage(e.getDuel().getChannel(), "Hah. You missed.");
			} else if (e.getDamage() >= 3) {
				b.sendMessage(e.getDuel().getChannel(), "'Tis but a scratch, fool.");
			} else {
				b.sendMessage(e.getDuel().getChannel(), e.getDamage() + " damage? Weak.");
			}
		}
		if(Util.sameUser(e.getCurrentTurn().getUser(), b.getJDAInstance().getSelfUser())) {
			//Bot's turn
			b.sendMessage(e.getDuel().getChannel(), "--duel attack");
		}
	}
	
	/**
	 * 
	 */
	public void onDuelDisband(DuelDisbandEvent e) {
		if(e.getDuel() == currentDuel) {
			e.getBot().sendMessage(e.getDuel().getChannel(), "**GG.**");
			currentDuel = null;
		}
	}
}
