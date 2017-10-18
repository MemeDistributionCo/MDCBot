package com.mdc.bot.util;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

public class PermUtil {

	/**
	 * Return whether the user has the "sd" role (Server Developer)
	 * @param a The member
	 * @return true or false
	 */
	public static boolean isUserSD(Member a) {
		for(Role r : a.getRoles()) {
			if (r.getName().equalsIgnoreCase("sd")) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Returns the Member from the Guild provided, assuming the user exists in it.
	 * @param u The user
	 * @param g The guild
	 * @return The member, or null
	 */
	public static Member userToMember(User u, Guild g) {
		return g.getMember(u);
	}
}
