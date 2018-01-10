package com.mdc.bot.command.game;

import com.mdc.bot.MDCBot;
import com.mdc.bot.command.Command;
import com.mdc.bot.command.CommandSet;
import com.mdc.bot.command.game.Duel.Stats;
import com.mdc.bot.util.Util;
import com.mdc.bot.util.event.DuelRequestEvent;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.User;

/**
 * Duel command for handling duel actions
 * @author xDestx
 *
 */
public class DuelCommand implements Command {

	private final Command[] children;
	/**
	 * Create sub commands
	 */
	public DuelCommand() {
		children = new Command[] {new DuelStatusCommand(this)};
	}
	
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
		} else if (s.getArgs()[0].equalsIgnoreCase("quit")) { 
			//Check if user is in duel
			if(FightPlayer.doesPlayerExist(s.getSender())) {
				FightPlayer fp = FightPlayer.getFightPlayer(s.getSender());
				Duel d = Duel.getDuelWithPlayer(fp);
				return d != null;
			}
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
		} else if(!s.getArgs()[0].equalsIgnoreCase("stats") && !s.getArgs()[0].equalsIgnoreCase("status") && s.getArgs().length >= 1 && s.getMessageReceivedEvent().getMessage().getMentionedUsers().size() == 1) {
			try {
				User target = s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0);
				User initiator = s.getSender();
				if(Util.sameUser(target,initiator)) {
					b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("You can't duel yourself, fool."));
					return false;
				}
				//if(Util.getMemberById(target.getIdLong(), s.getServer()).getUser().isBot()) {
					//b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("You can't duel bots (Yet)"));
					//return false;
				//}
				if(Duel.isPlayerInActiveDuel(target) || Duel.isPlayerInActiveDuel(initiator)) {
					//Can't already be in a duel
					b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("Someone is already in a duel"));
					return false;
				}
				return true;
			} catch (IndexOutOfBoundsException ex) {
				b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("Couldn't find player to duel"));
				return false;
			}
		} else if (s.getArgs()[0].equalsIgnoreCase("stats")) {
			return true;
		}
		String[] newArgs = new String[s.getArgs().length-1];
		for(int i = 0; i < newArgs.length; i++) {
			newArgs[i] = s.getArgs()[i+1];
		}
		for(Command c : children) {
			CommandSet cs = new CommandSet(s.getArgs()[0], newArgs, c, s.getMessageReceivedEvent());
			if(c.called(cs, b)) {
				return true;
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
			b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), "Duel help:\n"
					+ "--duel @user  ||  Creates a duel request with a user. If the user or requester is in a duel, the request will fail.\n"
					+ "--duel accept @user  ||  Accepts an outgoing request from the specified @user. Will fail if there is no request.\n"
					+ "--duel reject @user  ||  Rejects an outgoing request from the specified @user. Will fail if there is no request.\n"
					+ "--duel attack  ||  Attacks, if you are in a duel.\n"
					+ "--duel stats <@user (optional)> || Show's the duel stats of a user. Don't @ anyone to see your own stats.\n"
					+ "--duel help  ||  Displays this message.");
		} else if (s.getArgs()[0].equalsIgnoreCase("quit")) { 
			//Quit duel
			FightPlayer fp = FightPlayer.getFightPlayer(s.getSender());
			Duel d = Duel.getDuelWithPlayer(fp);
			if(Duel.disbandDuel(d)) {
				b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), "Quit duel");
			} else {
				b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), "Failed to quit duel");
			}
		} else if(s.getArgs()[0].equalsIgnoreCase("accept")) {
			User u = s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0);
			if(Duel.playerAcceptedDuel(u, s.getSender())) {
				b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("Duel accepted"));
			} else {
				b.sendMessage(s.getMessageReceivedEvent().getTextChannel(),  "Couldn't accept duel");
			}
		} else if (s.getArgs()[0].equalsIgnoreCase("reject")) {
			User u = s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0);
			Duel.playerRejectedDuel(u, s.getSender(), b);
			b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("Duel rejected"));
		} else if (!s.getArgs()[0].equalsIgnoreCase("stats") && !s.getArgs()[0].equalsIgnoreCase("status")) {
			User target = s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0);
			User initiator = s.getSender();
			FightPlayer p1 = FightPlayer.getFightPlayer(target);
			FightPlayer p2 = FightPlayer.getFightPlayer(initiator);
			Duel d = new Duel(p1,p2, s.getMessageReceivedEvent().getTextChannel(), b);
			b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), "Duel request created with players " + target.getAsMention() + " and " + initiator.getAsMention());
			
			//Invoke event
			DuelRequestEvent dre = new DuelRequestEvent(initiator, target, d);
			b.invokeEvent(dre);
			
		} else {
			String[] newArgs = new String[s.getArgs().length-1];
			for(int i = 0; i < newArgs.length; i++) {
				newArgs[i] = s.getArgs()[i+1];
			}
			for(Command c : children) {
				CommandSet cs = new CommandSet(s.getArgs()[0], newArgs, c, s.getMessageReceivedEvent());
				if(c.called(cs, b)) {
					c.action(cs, b);
					return;
				}
			}
			if(s.getMessageReceivedEvent().getMessage().getMentionedUsers().size() == 0) {
				//Get self stats
				long userId = s.getSender().getIdLong();
				Stats stats = Duel.getStats(userId);
				b.sendMessage(s.getTextChannel(), "Stats for " + s.getSender().getAsMention() + ": \nWins: " + stats.wins + "\nLosses: " + stats.losses + "\nStreak: " + (stats.streak > 2 ? "**"+stats.streak+"**":stats.streak));
			} else {
				//Get other stats
				User target = s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0);
				long userId = target.getIdLong();
				Stats stats = Duel.getStats(userId);
				b.sendMessage(s.getTextChannel(), "Stats for " + target.getAsMention() + ": \nWins: " + stats.wins + "\nLosses: " + stats.losses + "\nStreak: " + (stats.streak > 2 ? "**"+stats.streak+"**":stats.streak));
			}
		}
		
		
	}

	@Override
	public String getHelpMessage() {
		return "Help at: `--duel help`";
	}

	@Override
	public Command[] getChildCommands() {
		return children;
	}


	@Override
	public boolean isRootCommand() {
		return getParentCommand() == this;
	}

	@Override
	public Command getParentCommand() {
		return this;
	}
	
	/**
	 * Duel status sub command used to check status of a current user
	 * @author xDestx
	 *
	 */
	class DuelStatusCommand implements Command {
		private Command parent;
		
		/**
		 * Parent constructor
		 * @param d The parent command
		 */
		public DuelStatusCommand(DuelCommand d) {
			parent = d;
		}
		
		@Override
		public boolean called(CommandSet s, MDCBot b) {
			if(s.getLabel().equalsIgnoreCase("status")) {
				return true;
			}
			return false;
		}

		@Override
		public void action(CommandSet s, MDCBot b) {
			String output = "";
			User target;
			if(s.getMessageReceivedEvent().getMessage().getMentionedUsers().size() != 0) {
				target = s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0);
			} else {
				target = s.getSender();
			}
			output+="Duel status for " + target.getAsMention() + "\n";
			if(Duel.isPlayerInActiveDuel(target)) {
				Duel d = Duel.getDuelWithPlayer(FightPlayer.getFightPlayer(target));
				output+="In a duel: " + d.getPlayer1().getUser().getAsMention() + " vs. " + d.getPlayer2().getUser().getAsMention() + "\n";
				output+=d.getPlayer1().getUser().getAsMention() + " health: " + d.getPlayer1().getHP() + "\n";
				output+=d.getPlayer2().getUser().getAsMention() + " health: " + d.getPlayer2().getHP() + "\n";
				output+="Current turn: " + d.getCurrentAttacker().getUser().getAsMention() + "\n";
			} else if (Duel.getPendingDuelsWithUser(target).length != 0) {
				Duel[] duels = Duel.getPendingDuelsWithUser(target);
				output+=target.getAsMention() + " is in pending duel(s)" + "\n";
				for(Duel d : duels) {
					output+="\nDuel with " + d.getPlayer1().getUser().getAsMention() + " and " + d.getPlayer2().getUser().getAsMention() + "\n";
				}
			} else {
				output+=target.getAsMention() + " is not currently in any active or pending duels";
			}
			b.sendMessage(s.getTextChannel(), output);
		}

		@Override
		public String getHelpMessage() {
			return "";
		}

		@Override
		public Command[] getChildCommands() {
			return new Command[0];
		}

		@Override
		public boolean isRootCommand() {
			return false;
		}

		@Override
		public Command getParentCommand() {
			return parent;
		}
		
	}
	
}
