package com.mdc.bot.reaction;

import com.mdc.bot.MDCBot;


import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class CoolReaction extends Reaction {
	
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
