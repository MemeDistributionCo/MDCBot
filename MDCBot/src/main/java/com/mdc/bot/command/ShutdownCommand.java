package com.mdc.bot.command;

import java.util.List;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;

public class ShutdownCommand implements Command {

	@Override
	public boolean called(CommandSet s) {
		return true;
	}

	@Override
	public void action(CommandSet s) {
		MessageBuilder mb = new MessageBuilder();
		Role adminRole;
		try {
			adminRole = s.getMessageReceivedEvent().getGuild().getRolesByName("sd", true).get(0);
		} catch (IndexOutOfBoundsException e) {
			//Catching index out on this bc getRoles might rerturn null
			s.getMessageReceivedEvent().getTextChannel().sendMessage("Role not found");
			adminRole = null;
		}
		Guild server = s.getMessageReceivedEvent().getGuild();
		//Do null check in if statement bc
		if(adminRole != null && !containsMember(server.getMembersWithRoles(adminRole), server.getMember(s.getSender()))) {
			s.getMessageReceivedEvent().getTextChannel().sendMessage("TTTTTTEST");
		}
		mb.append("Au revoir ").append(s.getMessageReceivedEvent().getGuild().getEmotesByName("thecool", true).get(0));
		s.getMessageReceivedEvent().getTextChannel().sendMessage(mb.build()).complete();
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
