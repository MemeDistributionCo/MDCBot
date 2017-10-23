package com.mdc.bot.util;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;

public class Util {

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
	
	/**
	 * Compare two members to see if they are the same.
	 * @param m1
	 * @param m2
	 * @return
	 */
	public static boolean sameMember(Member m1, Member m2) {
		return Util.sameUser(m1.getUser(), m2.getUser());
	}
	
	/**
	 * Join together strings in an array while adding a space in between each element
	 * @param s
	 * @param startingIndex
	 * @return
	 */
	public static String joinStrings(String[] s, int startingIndex) {
		if(startingIndex > s.length-1)
			return "";
		String fin ="";
		for (int i = startingIndex; i < s.length; i++) {
			fin+=s[i] + (i == s.length-1 ? "":" ");
		}
		return fin;
	}
	
	/**
	 * Retrieve a member from a guild by their id.
	 * @param id
	 * @param g
	 * @return
	 */
	public static Member getMemberById(long id, Guild g) {
		return g.getMemberById(id);
	}
	
	/**
	 * Check whether two users are the same
	 * @param u1
	 * @param u2
	 * @return Whether they are the same
	 */
	public static boolean sameUser(User u1, User u2) {
		long id1 = u1.getIdLong();
		long id2 = u2.getIdLong();
		return id1 == id2;
	}
	
	/**
	 * Random value, inclusive on both ends
	 * @param begin
	 * @param end
	 * @return
	 */
	public static int randVal(int begin, int end) {
		return (int)(Math.random() * (end-begin+1)) + begin;
	}
}
