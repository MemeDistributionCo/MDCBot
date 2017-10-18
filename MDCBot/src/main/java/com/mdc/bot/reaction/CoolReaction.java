package com.mdc.bot.reaction;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CoolReaction extends ListenerAdapter {

	@Override
	public void onMessageReceived(MessageReceivedEvent e) {
		if(e.getAuthor().isBot())
			return;
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
