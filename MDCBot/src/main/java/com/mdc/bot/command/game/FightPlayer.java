package com.mdc.bot.command.game;

import java.util.HashMap;
import java.util.Map;

import net.dv8tion.jda.core.entities.User;

/**
 * Player class used for duels. Implements LivingPlayer class (see DnD stats (at least some))
 * @author xDestx
 *
 */
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
	/**
	 * Get AC (Armor Class)
	 * @return The users AC
	 */
	public int getAC() {
		return this.ac;
	}
	
	/**
	 * Get the HP of the player
	 * @return HP
	 */
	public int getHP() {
		return this.hp;
	}
	
	/**
	 * Decrement HP of user by 1 and check death status
	 * @param d Duel
	 */
	public void decrementHP(Duel d) {
		hp--;
		checkDeath(d);
	}
	
	/**
	 * Decrement hp by a specified value then check death status
	 * @param c Removal count
	 * @param d Duel
	 */
	public void decrementHP(int c, Duel d) {
		hp-=c;
		checkDeath(d);
	}
	
	private void checkDeath(Duel d) {
		if(hp <= 0) {
			d.playerHasDied(this);
		}
	}
	
	/**
	 * Get the user which belongs to this fight player
	 * @return The user
	 */
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

	/**
	 * Deprecate in order to work with duels
	 */
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
	
	/**
	 * Get a fight player which belongs to the user, or create a new one.
	 * @param u The user
	 * @return The fight player instance (non null)
	 */
	public static FightPlayer getFightPlayer(User u) {
		if(players.containsKey(u)) {
			return players.get(u);
		}
		return new FightPlayer(u);
	}
	
	/**
	 * Check if a fight player exists for this player currently
	 * @param u The user
	 * @return true, if it exists
	 */
	public static boolean doesPlayerExist(User u) {
		return players.containsKey(u);
	}
	
	/**
	 * Remove the fight player by using a fight player reference
	 * @param p The fight player to remove
	 */
	public static void removeFightPlayer(FightPlayer p) {
		removeFightPlayer(p.getUser());
	}
	
	/**
	 * Remove the fight player by user reference.
	 * @param u The user
	 */
	public static void removeFightPlayer(User u) {
		if(players.containsKey(u)) {
			players.remove(u);
		}
	}
}
