package com.mdc.bot.command;

import java.util.List;

import com.mdc.bot.MDCBot;
import com.mdc.bot.util.Util;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class FSpeakCommand implements Command {

	@Override
	public boolean called(CommandSet s, MDCBot b) {
		Guild g = s.getMessageReceivedEvent().getGuild();
		User u = s.getSender();
		if(s.getArgs().length < 2) {
			b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("Need at least 2 arguments"));
			return false;
		}
		return Util.isUserSD(Util.userToMember(u, g));
	}

	@Override
	public void action(CommandSet s, MDCBot b) {
		List<TextChannel> tc = s.getMessageReceivedEvent().getGuild().getTextChannelsByName(s.getArgs()[0], true);
		if(tc.size() < 1) {
			b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("Could not locate channel `" + s.getArgs()[0] + "`"));
			return;
		}
		
		b.sendMessage(tc.get(0), new MessageBuilder().append(Util.joinStrings(s.getArgs(), 1)));
	}

	@Override
	public String getHelpMessage() {
		return "--fspeak <target channel> <message...>";
	}
	
	@Override
	public Command[] getChildCommands() {
		return new Command[0];
	}
	
	@Override
	public boolean isRootCommand() {
		return getParentCommand() == this;
	}

	@Override
	public Command getParentCommand() {
		return this;
	}
	
}
