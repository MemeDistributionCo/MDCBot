package com.mdc.bot.command;

import java.util.List;

import com.mdc.bot.MDCBot;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

public class ShutdownCommand implements Command {

	@Override
	public boolean called(CommandSet s) {
		Role adminRole;
		try {
			adminRole = s.getMessageReceivedEvent().getGuild().getRolesByName("sd", true).get(0);
		} catch (IndexOutOfBoundsException e) {
			//Catching index out on this bc getRoles might rerturn null
			//s.getMessageReceivedEvent().getTextChannel().sendMessage("Role not found").complete();
			adminRole = null;
		}
		Guild server = s.getMessageReceivedEvent().getGuild();
		if(adminRole != null && !containsMember(server.getMembersWithRoles(adminRole), server.getMember(s.getSender()))) {
			MDCBot.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("Sorry, you don't have PERMISSION TO END ME"));
			return false;
		}
		return true;
	}

	@Override
	public void action(CommandSet s) {
		MessageBuilder mb = new MessageBuilder();
		mb.append("Au revoir ").append(s.getMessageReceivedEvent().getGuild().getEmotesByName("thecool", true).get(0));
		MDCBot.sendMessage(s.getMessageReceivedEvent().getTextChannel(), mb);
		s.getMessageReceivedEvent().getJDA().shutdown();
	}
	
	//I don't think the objects are equal so im making this to compare by id
	private boolean containsMember(List<Member> memberList, Member m) {
		if(memberList == null || m == null) {
			return false;
		}
		for(Member member : memberList) {
			if(member.getUser().getIdLong() == m.getUser().getIdLong()) {
				//Match
				return true;
			}
		}
		return false;
	}

	@Override
	public String getHelpMessage() {
		return "Affff";
	}

}
