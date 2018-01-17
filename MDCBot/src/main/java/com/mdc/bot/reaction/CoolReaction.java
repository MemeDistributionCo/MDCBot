package com.mdc.bot.reaction;

import com.mdc.bot.MDCBot;


import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

/**
 * First reaction usage. Any message which sends ":thecool:" gets a ":thecool:" reaction
 * @author xDestx
 *
 */
public class CoolReaction extends Reaction {
	
	/**
	 * Constructor (super)
	 * @param b The MDCBot instance
	 */
	public CoolReaction(MDCBot b) {
		super(b);
	}
	
	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		if(e.getMessage().getRawContent().contains(":thecool:")) {
			Emote emote;
			try {
				emote = e.getGuild().getEmotesByName("thecool", true).get(0);
			} catch (IndexOutOfBoundsException ex) {
				//Oof
				emote = null;
			}
			if(emote != null)
				e.getMessage().addReaction(emote).complete();
		}
	}
	
}
