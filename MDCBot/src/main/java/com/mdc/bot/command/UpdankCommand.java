package com.mdc.bot.command;

import com.mdc.bot.MDCBot;
import com.mdc.bot.util.MDCUser;
import com.mdc.bot.util.StatCollection;

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
		}
		return false;
	}

	@Override
	public void action(CommandSet s, MDCBot b) {
		MDCUser mdcUser = MDCUser.getMDCUser(s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0).getIdLong());
		if(mdcUser.getStats("updanks") == null) {
			StatCollection updankStats = new StatCollection("updanks");
			updankStats.setStat("updanks", 0);
			mdcUser.addStatCollection(updankStats);
		}
		b.sendMessage(s.getTextChannel(), s.getMessageReceivedEvent().getMessage().getMentionedUsers().get(0).getName() + " has " + mdcUser.getStats("updanks").getStatMap().get("updanks").intValue() + " updanks");
	}

	@Override
	public String getHelpMessage() {
		return "Shows the amount of updanks a user has.\nUsage: `--updank @user`";
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
