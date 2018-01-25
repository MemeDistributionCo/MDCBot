package com.mdc.bot.command;

import java.util.Set;

import com.mdc.bot.MDCBot;
import com.mdc.bot.util.MDCUser;
import com.mdc.bot.util.StatCollection;
import com.mdc.bot.util.Util;

import net.dv8tion.jda.core.entities.User;

/**
 * Updank command to check a users updanks
 * @author xDestx
 *
 */
public class UpdankCommand implements Command {

	@Override
	public boolean called(CommandSet s, MDCBot b) {
		//updank @user, but anything works really
		if(s.getMessageReceivedEvent().getMessage().getMentionedUsers().size() == 1) {
			return true;
		} else if (s.getArgs().length < 3 && s.getArgs().length > 0 && s.getMessageReceivedEvent().getMessage().getMentionedUsers().size() == 0 && s.getArgs()[0].equalsIgnoreCase("top")) {
			return true;
		}
		return false;
	}

	@Override
	public void action(CommandSet s, MDCBot b) {
		if(s.getMessageReceivedEvent().getMessage().getMentionedUsers().size() == 1) {
			MDCUser mdcUser = MDCUser.getMDCUser(s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0).getIdLong());
			if(mdcUser.getStats("updanks") == null) {
				StatCollection updankStats = new StatCollection("updanks");
				updankStats.setStat("updanks", 0);
				mdcUser.addStatCollection(updankStats);
			}
			b.sendMessage(s.getTextChannel(), s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0).getName() + " has " + mdcUser.getStats("updanks").getStatMap().get("updanks").intValue() + " updanks");
		} else {
			int topCount = 5;
			if(s.getArgs().length == 2) {
				try {
					topCount = Integer.parseInt(s.getArgs()[1]);
				} catch (Exception e) {
					//No reason to deal with this
				}
			}
			Set<MDCUser> users = MDCUser.getAllMDCUsers();
			MDCUser[] orderedUsers = new MDCUser[users.size()];
			for(int i = 0; i < orderedUsers.length; i++) {
				MDCUser top = null;
				for(MDCUser u : users) {
					if(top == null) {
						top = u;
					}
					else {
						if(!(top.getStats("updanks") != null && top.getStats("updanks").getStatMap().get("updanks") != null)){
							top.addStatCollection(new StatCollection("updanks"));
							top.getStats("updanks").setStat("updanks", 0);
							
						}
						
						if(!(u.getStats("updanks") != null && u.getStats("updanks").getStatMap().get("updanks") != null)) {
							u.addStatCollection(new StatCollection("updanks"));
							u.getStats("updanks").setStat("updanks", 0);
							
						}
						if(top.getStats("updanks").getStatMap().get("updanks").intValue() < u.getStats("updanks").getStatMap().get("updanks").intValue()) {
							//if top has less than this
							top = u;
							//set top to this
						}
					}
				}
				orderedUsers[i] = top;
				users.remove(top);
			}
			String msg = "";
			if(topCount <= 0 || topCount > orderedUsers.length) {
				topCount = orderedUsers.length;
			}
			msg+="Top " + topCount + " updank counts:\n";
			
			for(int i = 0; i < topCount; i++) {
				User u = Util.getUserByID(orderedUsers[i].getUserId(), s.getServer());
				msg+=(i+1)+". " + Util.getUserDisplayName(u, s.getServer()) + " - " + orderedUsers[i].getStats("updanks").getStatMap().get("updanks").intValue() + " (" + u.getName() +"#" + u.getDiscriminator() +")\n";
			}
			b.sendMessage(s.getTextChannel(), msg);
			
		}
	}

	@Override
	public String getHelpMessage() {
		return "Shows the amount of updanks a user has.\nUsage: `--updank @user`\nAward updanks by reacting to a message with :+1:";
	}

	@Override
	public Command[] getChildCommands() {
		return new Command[0];
	}

	@Override
	public boolean isRootCommand() {
		return true;
	}

	@Override
	public Command getParentCommand() {
		return this;
	}
	

}
