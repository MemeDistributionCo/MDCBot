package com.mdc.bot.command;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.mdc.bot.MDCBot;
import com.mdc.bot.util.Util;

import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class TimeoutCommand implements Command {

	private static Set<User> timeCool = new HashSet<User>();
	private static Map<User,TimeoutVote> currentVotes = new HashMap<User,TimeoutVote>();
	private static Map<User,Long> rollCool = new HashMap<User,Long>();
	
	@Override
	public boolean called(CommandSet s, MDCBot b) {
		if(s.getArgs().length == 0) return false;
		if(s.getMessageReceivedEvent().getMessage().getMentionedUsers().isEmpty()) {
			if(s.getArgs()[0].equalsIgnoreCase("roll")) {
				return Util.userHasRole(s.getSender(), "timeout", s.getServer());
			}
		} else if(!Util.userHasRole(s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0), "timeout", s.getServer()) && !timeCool.contains(s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0)) && !s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0).isBot()){
			return true;
		}
		b.sendMessage(s.getTextChannel(), "User is in a freedom period of 24 hours.");
		return false;
	}

	@Override
	public void action(CommandSet s, MDCBot b) {
		if(!s.getMessageReceivedEvent().getMessage().getMentionedUsers().isEmpty() && !s.getArgs()[0].equalsIgnoreCase("roll")) {
			//TIME OUT ATTACCC
			User u = s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0);
			if(currentVotes.containsKey(u)) {
				currentVotes.get(u).vote(s.getSender());
			} else {
				//Create a vote
				TimeoutVote tv = new TimeoutVote(Util.userToMember(u, s.getServer()),s.getTextChannel(),b,s.getServer(), s.getSender());
				currentVotes.put(u, tv);
				b.getScheduler().schedule(new Runnable() {
					@Override
					public void run() {
						tv.evaluate();
						currentVotes.remove(u);
					}
				}, 30, TimeUnit.SECONDS);
			}
		} else if (s.getArgs()[0].equalsIgnoreCase("roll")) {
			String out = "";
			if(TimeoutCommand.rollCool.containsKey(s.getSender())) {
				//Has to wait until the cooldown is over
				long allowedTime = TimeoutCommand.rollCool.get(s.getSender());
				if(System.nanoTime()>=allowedTime) {
					TimeoutCommand.rollCool.remove(s.getSender());
				} else {
					float remaining = (float)((allowedTime-System.nanoTime())/1e9);
					b.sendMessage(s.getTextChannel(), "You have to wait for " + remaining + " more seconds to roll.");
					return;
				}
			}
			int roll = (int)(Math.random()*21);
			out += "You rolled a " + roll + "!\n";
			if(roll >= 15) {
				//Free at last
				out+="Enjoy your freedom! You're immortal for approx 24h";
				TimeoutCommand.timeCool.add(s.getSender());
				b.getScheduler().schedule(new Runnable() {
					@Override
					public void run() {
						TimeoutCommand.timeCool.remove(s.getSender());
					}
				}, 24, TimeUnit.HOURS);
				Util.removeRolesFromMember(s.getServer(), Util.userToMember(s.getSender(), s.getServer()), "timeout").complete();
			} else {
				//Yikes
				out+="Sucks, doesn't it";
				TimeoutCommand.rollCool.put(s.getSender(), System.nanoTime()+(long)(15*1e9));
			}
			b.sendMessage(s.getTextChannel(), out);
		}
	}

	@Override
	public String getHelpMessage() {
		return "Usage: `--timeout @user` to participate or create a vote to send a user to timeout. The vote will last 30 seconds, 68% of the online members need to participate. Using this command means you are voting **to send the person to timeout**. Timeout lasts 15 minutes and the user is free from being voted on for 24 hours after.\n"
				+ "\n`--timeout roll` used to roll when a user is in timeout. Rolling a 15 or better will free you.";
	}
	
	static class TimeoutVote {
		private Set<User> voters;
		private TextChannel c;
		private Member target;
		private MDCBot b;
		private Guild s;
		private int onlineMembers;
		
		public TimeoutVote(Member target, TextChannel c, MDCBot b, Guild s, User voteCreator) {
			voters = new HashSet<User>();
			this.target = target;
			this.c = c;
			this.b = b;
			this.s = s;
			for (Member m : s.getMembers()) {
				if(m.getOnlineStatus() == OnlineStatus.ONLINE) {
					if(!m.getUser().isBot()) onlineMembers++;
				}
			}
			b.sendMessage(c, "Vote created to send " + target.getAsMention() + " to timeout for 15 mintues.");
			vote(voteCreator);
		}
		
		public void vote(User u) {
			voters.add(u);
			b.sendMessage(c, u.getAsMention() + " just voted! The total is " + getVoteCount());
		}
		
		public int getVoteCount() {
			return voters.size();
		}
		
		
		public void evaluate() {
			float percent = (float)voters.size()/onlineMembers;
			if(percent > 0.68f) {
				//rest in peace
				b.sendMessage(c, "===================================================\n\n**Rest in peace, " + target.getAsMention() + ". " + (percent*100) + "% of the online users voted to send you to timeout.**\nThe threshold is at 68%. Goodbye.\n\n===================================================");
				Util.addRolesToMember(s, target, "timeout").complete();
				b.getScheduler().schedule(new Runnable() {
					@Override
					public void run() {
						Util.removeRolesFromMember(s, target, "timeout").complete();
						TimeoutCommand.timeCool.add(target.getUser());
						TimeoutCommand.rollCool.remove(target.getUser());
						b.getScheduler().schedule(new Runnable() {
							@Override
							public void run() {
								TimeoutCommand.timeCool.remove(target.getUser());
							}
						}, 24, TimeUnit.HOURS);
					}
				}, 15, TimeUnit.MINUTES);
			} else {
				b.sendMessage(c, "===================================================\n\n**You have survived, " + target.getAsMention() + ". " + (percent*100) + "% of the online users voted to send you to timeout.\nThe threshold is at 68%.\n\n===================================================");
			}
		}
	}

	@Override
	public Command[] getChildCommands() {
		return new Command[0];
	}
}
