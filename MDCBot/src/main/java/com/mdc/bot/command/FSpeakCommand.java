package com.mdc.bot.command;

import java.util.List;

import com.mdc.bot.MDCBot;
import com.mdc.bot.util.PermUtil;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

public class FSpeakCommand implements Command {

	@Override
	public boolean called(CommandSet s) {
		Guild g = s.getMessageReceivedEvent().getGuild();
		User u = s.getSender();
		if(s.getArgs().length < 2) {
			MDCBot.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("Need at least 2 arguments"));
			return false;
		}
		return PermUtil.isUserSD(PermUtil.userToMember(u, g));
	}

	@Override
	public void action(CommandSet s) {
		List<TextChannel> tc = s.getMessageReceivedEvent().getGuild().getTextChannelsByName(s.getArgs()[0], true);
		if(tc.size() < 1) {
			MDCBot.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("Could not locate channel `" + s.getArgs()[0] + "`"));
			return;
		}
		
		MDCBot.sendMessage(tc.get(0), new MessageBuilder().append(PermUtil.joinStrings(s.getArgs(), 1)));
	}

	@Override
	public String getHelpMessage() {
		return "--fspeak <target channel> <message...>";
	}
	
}
