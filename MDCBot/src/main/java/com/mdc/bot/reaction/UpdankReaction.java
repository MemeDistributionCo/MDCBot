package com.mdc.bot.reaction;

import com.mdc.bot.MDCBot;
import com.mdc.bot.util.MDCUser;
import com.mdc.bot.util.StatCollection;

import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.core.events.message.react.MessageReactionRemoveEvent;

/**
 * Updank reaction system which functions similiarly to upvotes on reddit except
 * without the downvote
 * 
 * @author xDestx
 *
 */
public class UpdankReaction extends Reaction {

	/**
	 * Super constructor
	 * @param b The bot
	 */
	public UpdankReaction(MDCBot b) {
		super(b);
	}

	@Override
	public void onMessageReactionAdd(MessageReactionAddEvent e) {
		User sender = e.getTextChannel().getMessageById(e.getMessageIdLong()).complete().getAuthor();
		User reactor = e.getUser();
		if (e.getReactionEmote().isEmote()) {
			return;
		} else if (!e.getReactionEmote().getName().equals("👍")) {
			return;
		}

		if (sender.getIdLong() != reactor.getIdLong()) {
			MDCUser updankReceiver = MDCUser.getMDCUser(sender.getIdLong());
			if (updankReceiver.getStats("updanks") == null) {
				StatCollection updankStats = new StatCollection("updanks");
				updankStats.setStat("updanks", 0);
				updankReceiver.addStatCollection(updankStats);
			}
			StatCollection updanks = updankReceiver.getStats("updanks");
			updanks.setStat("updanks", updanks.getStatMap().get("updanks").intValue() + 1);
			updankReceiver.saveUser();
		}
	}

	@Override
	public void onMessageReactionRemove(MessageReactionRemoveEvent e) {
		User sender = e.getTextChannel().getMessageById(e.getMessageIdLong()).complete().getAuthor();
		User reactor = e.getUser();
		if (e.getReactionEmote().isEmote()) {
			return;
		} else if (!e.getReactionEmote().getName().equals("👍")) {
			return;
		}

		if (sender.getIdLong() != reactor.getIdLong()) {
			MDCUser updankReceiver = MDCUser.getMDCUser(sender.getIdLong());
			if (updankReceiver.getStats("updanks") == null) {
				StatCollection updankStats = new StatCollection("updanks");
				updankStats.setStat("updanks", 0);
				updankReceiver.addStatCollection(updankStats);
				return;
			}
			StatCollection updanks = updankReceiver.getStats("updanks");
			updanks.setStat("updanks", updanks.getStatMap().get("updanks").intValue() - 1);
			updankReceiver.saveUser();
		}
	}

}
