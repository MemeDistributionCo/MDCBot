package com.mdc.bot.command;

import java.util.LinkedList;
import java.util.Queue;

import com.mdc.bot.MDCBot;
import com.mdc.bot.util.Util;

import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

/**
 * A command in which those with the God role can perform divine punishment upon another user. Read the wiki for more information.
 * @author xDestx
 *
 */
public class DivinePunishmentCommand implements Command {

	private final Command[] children;
	private static Queue<Trial> trialedUsers = new LinkedList<Trial>();
	private static Thread trialWatcher;
	private static Runnable trialHandler;
	private static boolean trialActive = false, objectionOccured = false;
	
	/**
	 * Creates a trial handler if it doesn't exist.
	 */
	public DivinePunishmentCommand() {
		if(trialHandler == null) {
			trialHandler = new Runnable() {
				@Override
				public void run() {
					while(trialedUsers.size() > 0) {
							Trial x = trialedUsers.poll();
							if(x == null) continue;
							startTrial(x);
							awaitObjections(System.nanoTime());
							endTrial(x);
					}
				}
			};
		}
		children = new Command[] {new DivinePunishmentPunish(this), new DivineObjectionCommand(this), new DivineRevivalCommand(this)};
	}
	
	@Override
	public boolean called(CommandSet s, MDCBot b) {
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
		String[] newArgs = new String[s.getArgs().length-1];
		for(int i = 0; i < newArgs.length; i++) {
			newArgs[i] = s.getArgs()[i+1];
		}
		for(Command c : children) {
			CommandSet cs = new CommandSet(s.getArgs()[0], newArgs, c, s.getMessageReceivedEvent());
			if(c.called(cs, b)) {
				c.action(cs, b);
				break;
			}
		}
	}

	@Override
	public String getHelpMessage() {
		return "Administer Divine Punishment or Objection\nUsage: --divine punishment @user\n--divine objection";
	}

	@Override
	public Command[] getChildCommands() {
		return children;
	}

	@Override
	public boolean isRootCommand() {
		return true;
	}

	@Override
	public Command getParentCommand() {
		return this;
	}
	
	/**
	 * Starts a {@link Trial}
	 * @param t The Trial
	 */
	protected void startTrial(Trial t) {
		trialActive = true;
		String msg = "";
		msg+="######################################################################\n";
		msg+="\t\t\t\t\t\t\t\t\t\t\t\t\t\t🔥 D I V I N E  P U N I S H M E N T 🔥\n";
		msg+="\t\t\t\t\t\t\t\t\t\t*The God " + t.god.getAsMention() + " has decided to administer **divine punishment** upon " +t.target.getAsMention() + "*\n";
		msg+="\t\t\t\t\t\t\t\t\t\t*Unless there is a divine objection, " + t.target.getAsMention() + " **will be punished by the gods" + (t.reason.equals("") ? "***\n":" for" + t.reason+"***\n");
		msg+="######################################################################\n";
		t.bot.sendMessage(t.channel, msg);
	}
	
	/**
	 * Wait for objections (Trap, blocking)
	 * @param startTime Time to wait
	 */
	protected void awaitObjections(long startTime) {
		long timeWaited = 0;
		while(timeWaited < 1e9*10) {
			long currentTime = System.nanoTime();
			timeWaited += currentTime - startTime;
			startTime = currentTime;
		}
	}
	
	/**
	 * End a trial
	 * @param t The trial to end
	 */
	protected void endTrial(Trial t) {
		if(objectionOccured) {
			//Saved
			String msg = "";
			msg+="######################################################################\n";
			msg+="\t\t\t\t\t\t\t\t\t\t\t\t\t\t🔥 D I V I N E  P U N I S H M E N T 🔥\n";
			msg+="\t\t\t\t\t\t\t\t\t\t\t\t\t" + t.target.getAsMention() + ", the gods have chosen to spare you. Be grateful\n";
			msg+="######################################################################\n";
			t.bot.sendMessage(t.channel, msg);
		} else {
			//Not saved
			String msg = "";
			String godGlow = "";
			if(t.channel.getGuild().getEmotesByName("godglow", true).size() != 0) {
				godGlow = " " + t.channel.getGuild().getEmotesByName("godglow", true).get(0).getAsMention();
			}
			msg+="######################################################################\n";
			msg+="\t\t\t\t\t\t\t\t\t\t\t\t\t\t🔥 D I V I N E  P U N I S H M E N T 🔥\n";
			msg+="\t\t\t\t\t\t\t\t\t\t\t\t\t" + t.target.getAsMention() + ", **R E P E N T**\n";
			msg+="\t\t\t\t\t\t\t\t\t\t\t\t\t" + "*Omae wa, **mou shindeiru**" + godGlow + "*\n";
			msg+="######################################################################\n";
			t.bot.sendMessage(t.channel, msg);
			punish(t, System.nanoTime());
		}
		objectionOccured = false;
		trialActive = false;
	}
	
	/**
	 * Implement punishment onto a user
	 * @param t The trial associated
	 * @param startTime The start time (delay)
	 */
	protected void punish(Trial t, long startTime) {
		long timeWaited = 0;
		while(timeWaited < 1e9*2) {
			long currentTime = System.nanoTime();
			timeWaited+= currentTime - startTime;
			startTime = currentTime;
		}
		Util.addRolesToMember(t.channel.getGuild(), Util.getMemberById(t.target.getIdLong(), t.channel.getGuild()), "timeout").complete();
	}
	
	/**
	 * DivinePunishment Punish sub command. Used to p u n i s h.
	 * @author xDestx
	 *
	 */
	class DivinePunishmentPunish implements Command {
		
		private final DivinePunishmentCommand parent;
		/**
		 * Create a Punishment sub command with the parent command given.
		 * @param parent Divine punishment parent
		 */
		public DivinePunishmentPunish(DivinePunishmentCommand parent) {
			this.parent = parent;
		}

		@Override
		public boolean called(CommandSet s, MDCBot b) {
			if(s.getArgs().length > 0 && s.getArgs()[0].equals("?") && s.getLabel().equalsIgnoreCase("punishment")) {
				return true;
			}
			if (!(Util.userHasRole(s.getSender(), "God", s.getServer()) || Util.userHasRole(s.getSender(), "sd", s.getServer()))) {
				return false;
			}
			return s.getLabel().equals("punishment") && s.getMessageReceivedEvent().getMessage().getMentionedUsers().size() == 1 && !(Util.userHasRole(s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0),"God", s.getServer()) || Util.userHasRole(s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0),"sd", s.getServer()));
		}

		@Override
		public void action(CommandSet s, MDCBot b) {
			if(s.getArgs().length > 0 && s.getArgs()[0].equals("?")) {
				b.sendMessage(s.getTextChannel(), "`Divine Punishment`\nUsage: `--divine punishment <@user>`\nGod role required.");
				return;
			}
			User target = s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0);
			String reason = s.getMessageReceivedEvent().getMessage().getContent().trim().replace("--divine punishment @" + Util.getUserDisplayName(s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0), s.getServer()) + "", "");
			for(Trial t : trialedUsers) {
				if(t.target.getIdLong() == target.getIdLong()) {
					b.sendMessage(s.getTextChannel(), "Target is already up for trial");
					return;
				}
			}
			trialedUsers.add(new Trial(target, s.getSender(), reason, b, s.getTextChannel()));
			if(trialWatcher == null || !trialWatcher.isAlive()) {
				trialWatcher = new Thread(trialHandler);
				trialWatcher.start();
			}
		}

		@Override
		public String getHelpMessage() {
			return "Usage: `--divine punishment @target`\nRequirements: God role\nSentences a user to timeout punishment with a 10 second grace period in which yourself or other Gods can use `--divine objection` in order to spare the target.";
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

	/**
	 * Divine Object sub command to save those in need.
	 * @author xDestx
	 *
	 */
	class DivineObjectionCommand implements Command {
		
		private final DivinePunishmentCommand parent;
		/**
		 * Create a divine objection sub command with the provided parent
		 * @param parent DivinePunishmentCommand 
		 */
		public DivineObjectionCommand(DivinePunishmentCommand parent) {
			this.parent = parent;
		}
		
		@Override
		public boolean called(CommandSet s, MDCBot b) {
			if(s.getArgs().length > 0 && s.getArgs()[0].equals("?") && s.getLabel().equalsIgnoreCase("objection")) {
				return true;
			}
			if (!(Util.userHasRole(s.getSender(), "God", s.getServer()) || Util.userHasRole(s.getSender(), "sd", s.getServer()))) {
				return false;
			}
			if(s.getLabel().equals("objection") && DivinePunishmentCommand.trialActive == true) {
				return true;
			}
			return false;
		}

		@Override
		public void action(CommandSet s, MDCBot b) {
			if(s.getArgs().length > 0 && s.getArgs()[0].equals("?")) {
				b.sendMessage(s.getTextChannel(), "`Divine Objection`\nUsage: `--divine objection`\nGod role required.");
				return;
			}
			DivinePunishmentCommand.objectionOccured = true;
			b.sendMessage(s.getTextChannel(), "**O B J E C T I O N**");
		}

		@Override
		public String getHelpMessage() {
			return "Usage: `--divine objection`\nCan only be used during a trial, guaranteed to save target from divine punishment. God role required.";
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
	
	/**
	 * Divine Revival sub command to revive those in need.
	 * @author xDestx
	 *
	 */
	class DivineRevivalCommand implements Command {
		
		private final DivinePunishmentCommand parent;
		/**
		 * Create a divine objection sub command with the provided parent
		 * @param parent DivinePunishmentCommand 
		 */
		public DivineRevivalCommand(DivinePunishmentCommand parent) {
			this.parent = parent;
		}
		
		@Override
		public boolean called(CommandSet s, MDCBot b) {
			if(s.getArgs().length > 0 && s.getArgs()[0].equals("?") && s.getLabel().equalsIgnoreCase("revival")) {
				return true;
			}
			if (!(Util.userHasRole(s.getSender(), "God", s.getServer()) || Util.userHasRole(s.getSender(), "sd", s.getServer()))) {
				return false;
			}
			if(s.getLabel().equals("revival") && s.getArgs().length > 0 && s.getMessageReceivedEvent().getMessage().getMentionedUsers().size() > 0 && Util.userHasRole(s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0), "timeout", s.getServer())) {
				return true;
			}
			return false;
		}

		@Override
		public void action(CommandSet s, MDCBot b) {
			if(s.getArgs().length > 0 && s.getArgs()[0].equals("?")) {
				b.sendMessage(s.getTextChannel(), "`Divine Revival`\nUsage: `--divine revival <@user>`\nGod role required.");
				return;
			}
			User u = s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0);
			Util.removeRolesFromMember(s.getServer(), Util.getMemberById(u.getIdLong(), s.getServer()), "timeout").complete();
			b.sendMessage(s.getTextChannel(), "The great " + s.getSender().getAsMention() + " has freed " + u.getAsMention() + "!");
		}

		@Override
		public String getHelpMessage() {
			return "Usage: `--divine revival`\nCan only be used on user with timeout, brings them back. God role required.";
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
	
	/**
	 * Trial class to hold all pieces of a trial.
	 * @author xDestx
	 *
	 */
	class Trial {
		/**
		 * The target to punish
		 */
		public final User target, 
		/**
		 * The god issuing punishment
		 */
		god;
		/**
		 * The punishment reason
		 */
		public final String reason;
		/**
		 * The bot instance
		 */
		public final MDCBot bot;
		/**
		 * The trial channel
		 */
		public final TextChannel channel;
		/**
		 * Trial constructor
		 * @param target The trial target
		 * @param god The punishing god
		 * @param reason The punishment reason
		 * @param bot The bot instance
		 * @param channel The trial channel
		 */
		public Trial(User target, User god, String reason, MDCBot bot, TextChannel channel) {
			this.god = god;
			this.target = target;
			this.reason = reason;
			this.bot = bot;
			this.channel = channel;
		}
		/**
		 * To String implementation
		 */
		public String toString() {
			return this.god.getName() + "  " + this.target.getName() + "  " + this.reason + "  BOT xD" + " CHANNEL";
		}
	}
}
