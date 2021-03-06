package com.mdc.bot.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.mdc.bot.util.exception.TokenNotFoundException;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.managers.GuildController;
import net.dv8tion.jda.core.requests.restaction.AuditableRestAction;

/**
 * Utility class for bot related things
 * @author xDestx
 *
 */
public class Util {
	/**
	 * The Bot data folder path
	 */
	public final static String BOT_PATH = System.getProperty("user.home") + File.separatorChar + "MDCBot";
	/**
	 * The bot settings folder path
	 */
	public final static String BOT_SETTINGS_PATH = BOT_PATH + File.separatorChar + "settings";
	/**
	 * The bot token path
	 */
	public final static String TOKEN_FILE_PATH = BOT_SETTINGS_PATH + File.separatorChar + "token.txt";
	
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
	 * Get the User with the id
	 * @param id The id of the user
	 * @param g The guild
	 * @return The user
	 */
	public static User getUserByID(long id, Guild g) {
		return g.getMemberById(id).getUser();
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
	 * @param m1 Member 1
	 * @param m2 Member 2
	 * @return true if they are the same member
	 */
	public static boolean sameMember(Member m1, Member m2) {
		return Util.sameUser(m1.getUser(), m2.getUser());
	}
	
	/**
	 * Join together strings in an array while adding a space in between each element
	 * @param s String array
	 * @param startingIndex start at specified position in array (0 for all strings in array)
	 * @return A full string
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
	 * @param id User id
	 * @param g Server instance
	 * @return The Member if it exists
	 */
	public static Member getMemberById(long id, Guild g) {
		return g.getMemberById(id);
	}
	
	/**
	 * Check whether two users are the same
	 * @param u1 User 1
	 * @param u2 User 2
	 * @return Whether they are the same
	 */
	public static boolean sameUser(User u1, User u2) {
		long id1 = u1.getIdLong();
		long id2 = u2.getIdLong();
		return id1 == id2;
	}
	
	/**
	 * Random value, inclusive on both ends
	 * @param begin beginning value
	 * @param end ending value
	 * @return the random value
	 */
	public static int randVal(int begin, int end) {
		return (int)(Math.random() * (end-begin+1)) + begin;
	}
	
	/**
	 * Attempts to read the token from the {@link com.mdc.bot.util.Util#TOKEN_FILE_PATH Token File}. If the file didn't exist previously, it is created and a {@link TokenNotFoundException} is thrown.
	 * @return A String of the token used for the bot
	 * @throws IOException Failed IO action
	 * @throws TokenNotFoundException Token file does not exist
	 */
	public static String readToken() throws IOException, TokenNotFoundException {
		File tokenFolder = new File(Util.BOT_SETTINGS_PATH);
		if(!tokenFolder.exists()) {
			tokenFolder.mkdirs();
		}
		File tokenFile = new File(Util.TOKEN_FILE_PATH);
		if(!tokenFile.exists()) {
			tokenFile.createNewFile();
			FileWriter fw = new FileWriter(tokenFile);
			fw.write("token: ");
			fw.close();
			//Token never existed
			throw new TokenNotFoundException("We had to generate a new token file for you. You cannot run a bot without a token.", Util.TOKEN_FILE_PATH);
		}
		
		BufferedReader fr = new BufferedReader(new FileReader(tokenFile));
		String token = fr.readLine();
		fr.close();
		token = token.replace("token:", "");
		//token.replace(" ", "");
		token = token.trim();
	
		return token;
	}
	
	/**
	 * Adds all roles with the name "rolename" to the Member mem.
	 * Submit action after method completed
	 * @param g The guild to check for role, member, etc.
	 * @param mem The member to add role to
	 * @param roleName The name of the role(s) to add
	 * @return A rest action which needs to be <em>completed</em>
	 */
	public static AuditableRestAction<Void> addRolesToMember(Guild g, Member mem, String roleName) {
		GuildController gControl = new GuildController(g);
		List<Role> r = g.getRolesByName(roleName, true);
		return gControl.addRolesToMember(mem, r);
	}
	
	/**
	 * Removes all roles with the name "rolename" to the Member mem
	 * @param g The guild to check for role, member, etc.
	 * @param mem The member to add role to
	 * @param roleName The name of the role(s) to add
	 * @return A rest action which needs to me <em>completed</em>
	 */
	public static AuditableRestAction<Void> removeRolesFromMember(Guild g, Member mem, String roleName) {
		GuildController gControl = new GuildController(g);
		List<Role> r = g.getRolesByName(roleName, true);
		return gControl.removeRolesFromMember(mem, r);
	}

	
	/**
	 * Get the provided Users currently displayed name (Nickname).
	 * @param g The guild for the user
	 * @param u The user
	 * @return The User's display name
	 */
	public static String getUserDisplayName(User u, Guild g) {
		Member m = Util.userToMember(u, g);
		return getUserDisplayName(m);
	}
	
	/**
	 * Retrieve the member's nickname ({@link Member#getNickname()}
	 * @param m The member
	 * @return Their nick name as a String
	 */
	public static String getUserDisplayName(Member m) {
		return m.getNickname();
	}

	/**
	 * Check whether a user has a role
	 * @param u The user
	 * @param role The role name
	 * @param g The server instance
	 * @return true, if they have it
	 */

	public static boolean userHasRole(User u, String role, Guild g) {
		Member m = Util.userToMember(u, g);
		for(Role r : m.getRoles()) {
			if(r.getName().equalsIgnoreCase(role)) {
				return true;
			}
		}
		return false;
	}
}
