package com.mdc.bot.command.game;

/**
 * Extending off of {@linkplain Player} for games which use a health system.
 * @author xDest
 *
 */
public interface LivingPlayer extends Player {

	/**
	 * Get the health of the Player
	 * @return The current amount of health the player has
	 */
	public int getHP();
	/**
	 * Used to tell whether the player is still alive
	 * @return Whether the player has died
	 */
	public boolean isDead();
	/**
	 * Increase the health of the player by 1.
	 */
	public void incrementHP();
	/**
	 * Decrease the health of the player by 1.
	 */
	public void decrementHP();
	/**
	 * Set the health of the current player
	 * @param hp The desired health
	 */
	public void setHP(int hp);
	/**
	 * Increase the health of the player by a specified amount.
	 * @param h The desired increase of health
	 */
	public void incrementHP(int h);
	/**
	 * Decrease the health of the player by a specified amount.
	 * @param h The amount of health to remove.
	 */
	public void decrementHP(int h);
	
}
