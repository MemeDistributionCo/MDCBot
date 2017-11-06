package com.mdc.bot.command;

import java.util.ArrayList;
import java.util.List;

import com.mdc.bot.MDCBot;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.managers.GuildController;

public class GameReqCommand implements Command {

	@Override
	public boolean called(CommandSet s, MDCBot b) {
		if (s.getArgs().length == 1) {
			if (s.getArgs()[0].equals("list"))
				return true;
		} else if (s.getArgs().length == 2) {
			// check if bot is allowed to manange roles
			if (!s.getServer().getSelfMember().hasPermission(Permission.MANAGE_ROLES)) {
				b.sendMessage(s.getTextChannel(),"Unable to manage roles.");
				return false;
			}
				
			// join/leave
			if (s.getArgs()[0].equals("join") || s.getArgs()[1].equals("leave")) {
				 if	(getGames(s.getServer()).contains(s.getArgs()[1])) {
					 return true;
				 } else {
					 b.sendMessage(s.getTextChannel(),"Game " + s.getArgs()[1] + " does not exist");
				 }
			}
		}
		b.sendMessage(s.getTextChannel(),getHelpMessage());
		return false;
	}

	@Override
	public void action(CommandSet s, MDCBot b) {
		if (s.getArgs().length == 1) {
			MessageBuilder m = new MessageBuilder();
			m.append("List of games:\n");
			for (String game : getGames(s.getServer())) {
				//List games
				m.append("\t" + game + "\n");
			}
			b.sendMessage(s.getTextChannel(),m.build().toString());
		} else if (s.getArgs().length == 2) {
			// join/leave
			if (getGames(s.getServer()).contains(s.getArgs()[1])) {
				//Get GuildController, Member, and Role
				GuildController gControl = new GuildController(s.getServer());
				Member mem = s.getServer().getMember(s.getSender());
				Role r = s.getServer().getRoleById(GAMEROLEPREFIX + s.getArgs()[1]);
				
				if (s.getArgs()[0].equals("join")) {
					gControl.addSingleRoleToMember(mem, r);
				} else if (s.getArgs()[1].equals("leave")) {
					gControl.removeSingleRoleFromMember(mem, r);
				}
			}
		}
	}

	private static final String GAMEROLEPREFIX = "g_";
	/**
	 * Returns a list of all games
	 * @param g The Guild to search
	 * @return a list of all game names without the prefix
	 */
	private static List<String> getGames(Guild g) {
		List<Role> roles = g.getRoles();
		List<String> roleNames = new ArrayList<String>();
		for (Role role : roles) {
			if (role.getName().startsWith(GAMEROLEPREFIX))
				roleNames.add(role.getName().substring(GAMEROLEPREFIX.length()));
			//Takes each role matching g_* and places the name (sans g_) in roleNames
		}
		return roleNames;
	}

	@Override
	public String getHelpMessage() {
		return "--game: \n"
				+ "\tjoin <game name> - Join the game group\n"
				+ "\tleave <game name> - Leave the game group\n"
				+ "\tlist - List game groups\n";
	}

}
