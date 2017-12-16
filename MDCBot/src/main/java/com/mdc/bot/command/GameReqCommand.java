package com.mdc.bot.command;

import java.util.ArrayList;
import java.util.List;

import com.mdc.bot.MDCBot;
import com.mdc.bot.util.Util;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.requests.restaction.AuditableRestAction;

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
			if (s.getArgs()[0].equals("join") || s.getArgs()[0].equals("leave")) {
				 if	(getGames(s.getServer()).contains(s.getArgs()[1])) {
					 return true;
				 } else {
					 b.sendMessage(s.getTextChannel(),"Game " + s.getArgs()[1] + " does not exist");
					 return false;
				 }
			}
		}
		b.sendMessage(s.getTextChannel(),getHelpMessage());
		return false;
	}

	@Override
	public void action(CommandSet s, MDCBot b) {
		// List games
		if (s.getArgs().length == 1) {
			MessageBuilder m = new MessageBuilder();
			m.append("List of games:\n");
			for (String game : getGames(s.getServer())) {
				//List games
				m.append("\t" + game + "\n");
			}
			b.sendMessage(s.getTextChannel(),m);
		// Add/Remove roles
		} else if (s.getArgs().length == 2) {
			// game exists
			if (getGames(s.getServer()).contains(s.getArgs()[1])) {
				String roleName = GAMEROLEPREFIX + s.getArgs()[1];
				//user has role
				boolean hasRole = s.getServer().getMember(s.getSender())
						.getRoles().contains(
								s.getServer().getRolesByName(roleName, true).get(0));
				
				MessageBuilder confirm = new MessageBuilder();
				confirm.append(s.getSender().getAsMention());
				
				Member mem = s.getServer().getMember(s.getSender());
				if (s.getArgs()[0].equals("join")) { //join
					if (hasRole) { //is already in
						confirm.append(" already has the role " + roleName);
						b.sendMessage(s.getTextChannel(),confirm);
						return;
					}
					AuditableRestAction<Void> a =
							Util.addRolesToMember(s.getServer(),mem, roleName);
					a.submit();
					confirm.append(" added to ");
				} else if (s.getArgs()[0].equals("leave")) { //leave
					if (!hasRole) { //is already out
						confirm.append(" does not have the role " + roleName);
						b.sendMessage(s.getTextChannel(),confirm);
						return;
					}
					AuditableRestAction<Void> a =
							Util.removeRolesFromMember(s.getServer(),mem, roleName);
					a.submit();
					confirm.append(" removed from ");
				}
				confirm.append(roleName);

				b.sendMessage(s.getTextChannel(),confirm);
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
	
	@Override
	public Command[] getChildCommands() {
		return new Command[0];
	}

}
