package com.mdc.bot.command.fight;

import com.mdc.bot.MDCBot;

import com.mdc.bot.command.Command;
import com.mdc.bot.command.CommandSet;
import com.mdc.bot.util.Util;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.User;

public class DuelCommand implements Command {

	@Override
	public boolean called(CommandSet s, MDCBot b) {
		//This is a challenge or an attack
		if(s.getArgs().length == 0) return false;
		if(s.getArgs()[0].equalsIgnoreCase("attack")) {
			FightPlayer dueler = FightPlayer.getFightPlayer(s.getSender());
			Duel duel = Duel.getDuelWithPlayer(dueler);
			if (duel == null) {
				FightPlayer.removeFightPlayer(dueler);
			}
			return duel != null;
		} else if (s.getArgs()[0].equalsIgnoreCase("help")) { 
			return true;
		} else if(s.getArgs()[0].equalsIgnoreCase("accept")) {
			try {
				s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0);
				return true;
			} catch (Exception e) {
				b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("Couldn't find duel request."));
				return false;
			}
		} else if (s.getArgs()[0].equalsIgnoreCase("reject")) {
			try {
				s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0);
				return true;
			} catch (Exception e) {
				b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("Couldn't find duel request."));
				return false;
			}
		} else if(s.getArgs().length >= 1 && s.getMessageReceivedEvent().getMessage().getMentionedUsers().size() == 1) {
			try {
				User target = s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0);
				User initiator = s.getSender();
				if(Util.sameUser(target,initiator)) {
					b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("You can't duel yourself, fool."));
					return false;
				}
				if(Util.getMemberById(target.getIdLong(), s.getServer()).getUser().isBot()) {
					b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("You can't duel bots (Yet)"));
					return false;
				}
				if(Duel.isPlayerInDuel(target) || Duel.isPlayerInDuel(initiator)) {
					//Can't already be in a duel
					b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("Someone is already in a duel"));
					return false;
				}
				return true;
			} catch (IndexOutOfBoundsException ex) {
				b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("Couldn't find player to duel"));
				return false;
			}
		}
		
		
	
		return false;
	}

	@Override
	public void action(CommandSet s, MDCBot b) {
		if(s.getArgs()[0].equalsIgnoreCase("attack")) {
			FightPlayer dueler = FightPlayer.getFightPlayer(s.getSender());
			Duel duel = Duel.getDuelWithPlayer(dueler);
			duel.userTriedAttack(dueler);
		} else if(s.getArgs()[0].equalsIgnoreCase("help")) { 
			//Do Later
			b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("Help!"));
		} else if(s.getArgs()[0].equalsIgnoreCase("accept")) {
			User u = s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0);
			Duel.playerAcceptedDuel(u, s.getSender());
			b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("Duel accepted"));
		} else if (s.getArgs()[0].equalsIgnoreCase("reject")) {
			User u = s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0);
			Duel.playerRejectedDuel(u, s.getSender());
			b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("Duel rejected"));
		} else {
			User target = s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0);
			User initiator = s.getSender();
			FightPlayer p1 = FightPlayer.getFightPlayer(target);
			FightPlayer p2 = FightPlayer.getFightPlayer(initiator);
			new Duel(p1,p2, s.getMessageReceivedEvent().getTextChannel(), b);
		}
		
	
	}

	@Override
	public String getHelpMessage() {
		return "Help at: `--duel help`";
	}

	
	
}