package com.mdc.bot.command.game;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.core.entities.User;

public class FightPlayer implements LivingPlayer {
	
	private static Map<User,FightPlayer> players = new HashMap<User,FightPlayer>();
	
	private User u;
	
	private int ac,hp;
	
	private FightPlayer() {}
	
	private FightPlayer(User u) {
		this.u = u;
		this.ac = 12;
		this.hp = 12;
		players.put(u, this);
	}
	
	public int getAC() {
		return this.ac;
	}
	
	public int getHP() {
		return this.hp;
	}
	
	public void decrementHP(Duel d) {
		hp--;
		checkDeath(d);
	}
	
	public void decrementHP(int c, Duel d) {
		hp-=c;
		checkDeath(d);
	}
	
	private void checkDeath(Duel d) {
		if(hp <= 0) {
			d.playerHasDied(this);
		}
	}
	
	public User getUser() {
		return this.u;
	}


	@Override
	public long getUserId() {
		return this.u.getIdLong();
	}

	@Override
	public boolean isDead() {
		return this.hp <= 0;
	}

	@Override
	public void incrementHP() {
		this.hp++;
	}

	@Override @Deprecated
	public void decrementHP() {
	//	this.hp--;
	}

	@Override
	public void setHP(int hp) {
		this.hp = hp;
	}

	@Override
	public void incrementHP(int h) {
		this.hp+=h;
	}

	
	@Override @Deprecated
	public void decrementHP(int h) {
		//nothing
	}
	
	
	public static FightPlayer getFightPlayer(User u) {
		if(players.containsKey(u)) {
			return players.get(u);
		}
		return new FightPlayer(u);
	}
	
	public static boolean doesPlayerExist(User u) {
		return players.containsKey(u);
	}
	
	public static void removeFightPlayer(FightPlayer p) {
		removeFightPlayer(p.getUser());
	}
	
	public static void removeFightPlayer(User u) {
		if(players.containsKey(u)) {
			players.remove(u);
		}
	}
}
