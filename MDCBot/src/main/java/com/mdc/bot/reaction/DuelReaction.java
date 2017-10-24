package com.mdc.bot.reaction;

import com.mdc.bot.MDCBot;
import com.mdc.bot.command.game.Duel;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


/**
 * Duel reaction to allow the bot to engage in duels with other users
 * @author xDest
 *
 */
public class DuelReaction extends Reaction {

	private Duel currentDuel;
	
	public DuelReaction(MDCBot b) {
		super(b);
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		//Listen for duels
	}
	
}
