package com.mdc.bot.command.fight;

import java.util.HashSet;
import java.util.Set;

import com.mdc.bot.MDCBot;
import com.mdc.bot.util.PermUtil;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.TextChannel;

public class Duel {
	
	private static Set<Duel> activeDuels = new HashSet<Duel>();
	
	
	private FightPlayer p1,p2;
	private TextChannel channel;
	private MDCBot bot;
	
	private int turn;
	
	public Duel(FightPlayer p1, FightPlayer p2, TextChannel channel, MDCBot b) {
		this.p1 = p1;
		this.p2 = p2;
		turn = 0;
		this.channel = channel;
		this.bot = b;
		activeDuels.add(this);
	}
	
	public void incrementTurn() {
		turn++;
		turn = turn %2;
	}
	
	public FightPlayer getPlayer1() {
		return p1;
	}
	
	public FightPlayer getPlayer2() {
		return p2;
	}
	
	public FightPlayer[] getPlayers() {
		return new FightPlayer[] {p1,p2};
	}
	
	public FightPlayer getCurrentAttacker() {
		return turn == 0 ? p1:p2;
	}
	
	public FightPlayer getCurrentDefender() {
		return turn == 0 ? p2:p1;
	}
	
	public void userTriedAttack(FightPlayer p) {
		if(p != getCurrentAttacker()) {
			MessageBuilder mb = new MessageBuilder();
			mb.append("Agh! Stay back *p e a s a n t*. It's not your turn!");
			bot.sendMessage(channel, mb);
		} else {
			attack();
		}
	}
	
	private void attack() {
		int attackRoll = PermUtil.randVal(1, 20);
		int attackDmg = PermUtil.randVal(1, 6);
		int crit = 0;
		FightPlayer attacker = getCurrentAttacker();
		FightPlayer defender = getCurrentDefender();
		if(attackRoll == 1) {
			//Oof
			FightPlayer temp = attacker;
			attacker = defender;
			defender = temp;
			crit = 1;
		} else if (attackRoll == 20) {
			//Double oof
			attackDmg*=2;
			crit = 2;
		}
		
		switch (crit) {
		case 1:
			bot.sendMessage(channel, new MessageBuilder().append("Oof! **c r i t i c a l  m i s s**. Damage switched."));
			break;
		case 2:
			bot.sendMessage(channel, new MessageBuilder().append("How unlucky, ").append(defender.getUser()));
			break;
		}

		if(crit == 1) {
			attackRoll+=1;
		}
		
		bot.sendMessage(channel, new MessageBuilder().append(attacker.getUser()).append(" rolled a " + attackRoll + " for their attack role!"));
		
		if(attackRoll > defender.getAC()) {
			//SHineeee
			bot.sendMessage(channel, new MessageBuilder().append("The attack hits for " + attackDmg + " damage."));
			if(defender.getHP() - attackDmg > 0) {
				//Still alive
				bot.sendMessage(channel, new MessageBuilder().append(defender.getUser()).append(" has " + defender.getHP() + " HP left."));
			}
			defender.decrementHP(attackDmg, this);
			
		} else {
			bot.sendMessage(channel, new MessageBuilder().append("The attack missed!"));
		}
	}
	
	public void playerHasDied(FightPlayer p) {
		if(p == p1 || p == p2) {
			FightPlayer winner = p == p1 ? p1:p2;
			bot.sendMessage(channel, new MessageBuilder().append(p.getUser()).append(" has died! The winner is ").append(winner.getUser()));
			Duel.disbandDuel(this);
		} 
	}
	
	public static void disbandDuel(Duel d) {
		activeDuels.remove(d);
		for(FightPlayer p : d.getPlayers()) {
			FightPlayer.removeFightPlayer(p);
		}
		//Done
	}

}
