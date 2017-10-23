package com.mdc.bot.command.game;

import java.util.HashSet;
import java.util.Set;

import com.mdc.bot.MDCBot;
import com.mdc.bot.util.Util;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class Duel {
	
	private static Set<Duel> activeDuels = new HashSet<Duel>();
	private static Set<Duel> pendingDuels = new HashSet<Duel>();
	
	
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
		pendingDuels.add(this);
	}
	
	public void sendStartMessage() {
		bot.sendMessage(channel, new MessageBuilder().append(getCurrentAttacker().getUser()).append(", it is your turn to attack."));
	}
	
	public void incrementTurn() {
		turn++;
		turn = turn %2;
		bot.sendMessage(channel, new MessageBuilder().append(getCurrentAttacker().getUser()).append(", it is your turn to attack."));
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
		int attackRoll = Util.randVal(1, 20);
		int attackDmg = Util.randVal(1, 6);
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
			attackRoll=Util.randVal(2, 19);
		}
		
		bot.sendMessage(channel, new MessageBuilder().append(attacker.getUser()).append(" rolled a " + attackRoll + " for their attack role!"));
		
		boolean death = false;
		
		if(attackRoll > defender.getAC()) {
			//SHineeee
			bot.sendMessage(channel, new MessageBuilder().append("The attack hits for " + attackDmg + " damage."));
			if(defender.getHP() - attackDmg > 0) {
				//Still alive
				bot.sendMessage(channel, new MessageBuilder().append(defender.getUser()).append(" has " + (defender.getHP() - attackDmg) + " HP left."));
			} else {
				death = true;
			}
			defender.decrementHP(attackDmg, this);
			
		} else {
			bot.sendMessage(channel, new MessageBuilder().append("The attack missed!"));
		}
		if(!death) this.incrementTurn();
	}
	
	public void playerHasDied(FightPlayer p) {
		if(p == p1 || p == p2) {
			FightPlayer winner = p == p1 ? p2:p1;
			bot.sendMessage(channel, new MessageBuilder().append(p.getUser()).append(" has died! The winner is ").append(winner.getUser()));
			Duel.disbandDuel(this);
		} 
	}
	
	public static Duel getDuelWithPlayer(FightPlayer p) {
		if(isPlayerInDuel(p.getUser())) {
			for(Duel d : activeDuels) {
				if(d.getPlayer1() == p || d.getPlayer2() == p) {
					return d;
				}
			}
		}
		return null;
	}
	
	public static boolean isPlayerInDuel(User p) {
		for(Duel d : activeDuels) {
			if(d.getPlayer1().getUser() == p || d.getPlayer2().getUser() == p) return true;
		}
		return false;
	}
	
	public static boolean disbandDuel(Duel d) {
		boolean removed = activeDuels.contains(d) || pendingDuels.contains(d);
		activeDuels.remove(d);
		pendingDuels.remove(d);
		for(FightPlayer p : d.getPlayers()) {
			FightPlayer.removeFightPlayer(p);
		}
		return removed;
		//Done
	}
	
	public static Duel getPendingDuelWithUsers(User u1, User u2) {
		for(Duel d : Duel.pendingDuels) {
			if(d.getPlayer1().getUser() == u1 || d.getPlayer1().getUser() == u2) {
				if(d.getPlayer2().getUser() == u1 || d.getPlayer2().getUser() == u2) {
					return d;
				}
			}
		}
		return null;
	}
	
	public static boolean playerAcceptedDuel(User targetDuelPartner, User accepter) {
		Duel d = getPendingDuelWithUsers(targetDuelPartner,accepter);
		if(d == null) {
			//Nada
			return false;
		} else {
			Duel.pendingDuels.remove(d);
			Duel.activeDuels.add(d);
			d.sendStartMessage();
			return true;
		}
	}
	
	public static void playerRejectedDuel(User u1, User u2) {
		Duel d = getPendingDuelWithUsers(u1,u2);
		if(d == null) {
			//Nothing
		} else {
			Duel.disbandDuel(d);
		}
	}

}
