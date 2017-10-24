package com.mdc.bot.reaction;

import com.mdc.bot.MDCBot;

import net.dv8tion.jda.core.entities.Emote;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class CoolReaction extends ListenerAdapter {

	private MDCBot bot;
	
	public CoolReaction(MDCBot b) {
		this.bot = b;
	}
	
	public MDCBot getBot() {
		return this.bot;
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
