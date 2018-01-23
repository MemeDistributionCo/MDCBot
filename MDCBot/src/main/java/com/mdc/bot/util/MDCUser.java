package com.mdc.bot.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
/**
 * The intention of this class is to store all custom information about the user. Duel stats, updanks, and whatever future stats we add.
 * @author xDest
 *
 */
public class MDCUser implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7878121990963606360L;

	private final long userId;
	
	private Map<String,StatCollection> stats;
	
	private MDCUser(long userId) {
		this.userId = userId;
		this.stats = new HashMap<String,StatCollection>();
	}
	
	/**
	 * Gets a StatCollection with the matching name. If it doesn't exist, returns null.
	 * @param name The stat collection with this name
	 * @return A StatCollection, or null
	 */
	public StatCollection getStats(String name) {
		for(String s : stats.keySet()) {
			if(s.equals(name)) {
				return stats.get(s);
			}
		}
		return null;
	}
	
	/**
	 * Get a map of all the stat collections which this user has.
	 * @return A Map&lt;String,StatCollection&gt; of stats 
	 */
	public Map<String,StatCollection> getAllStats() {
		return stats;
	}

	/**
	 * Ad a stat collection to the user stat map
	 * @param sc The stat collection to add
	 */
	public void addStatCollection(StatCollection sc) {
		this.stats.put(sc.getName(),sc);
	}
	
	/**
	 * Return the {@link net.dv8tion.jda.core.entities.User#getIdLong() longId} of the {@link net.dv8tion.jda.core.entities.User User}.
	 * @return the id
	 */
	public long getUserId() {
		return userId;
	}
	
	/**
	 * Save the user to a user file. Once this class changes, I'm not sure it will be compatible with previous versions.
	 * @return true, if save is successful.
	 */
	public boolean saveUser() {
		File userFolder = new File(Util.BOT_PATH + File.separatorChar + "MDCUsers");
		if(!userFolder.exists())
			userFolder.mkdirs();
		File userFile = new File(userFolder.getPath() + File.separatorChar + userId + ".mdu");
		if(!userFile.exists()) {
			try {
				userFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(userFile));
			oos.writeObject(this);
			oos.close();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * Gets the MDCUser with the specified id.
	 * @param userId The user id
	 * @return The MDCUser
	 */
	public static MDCUser getMDCUser(long userId) {
		File userFolder = new File(Util.BOT_PATH + File.separatorChar + "MDCUsers");
		if(!userFolder.exists())
			userFolder.mkdirs();
		File userFile = new File(userFolder.getPath() + File.separatorChar + userId + ".mdu");
		MDCUser searchedUser;
		if(!userFile.exists()) {
			MDCUser newUser = new MDCUser(userId);
			newUser.saveUser();
			searchedUser = newUser;
		} else {
			try {
				ObjectInputStream oos = new ObjectInputStream(new FileInputStream(userFile));
				searchedUser = (MDCUser)oos.readObject();
				oos.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				searchedUser = null;
			} catch (IOException e) {
				e.printStackTrace();
				searchedUser = null;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				searchedUser = null;
			}
		}
		return searchedUser;
	}
	
	public static Set<MDCUser> getAllMDCUsers() {
		File userFolder = new File(Util.BOT_PATH + File.separatorChar + "MDCUsers");
		FilenameFilter filter = new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".mdu");
			}
			
		};
		
		Set<MDCUser> userSet = new HashSet<MDCUser>();
		for(String f : userFolder.list(filter)) {
			MDCUser user = MDCUser.getMDCUser(Long.parseLong(f.replace(".mdu", "")));
			if(user == null) {
				continue;
			}
			userSet.add(user);
		}
		return userSet;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof MDCUser)) return false;
		MDCUser u = (MDCUser)o;
		return u.getUserId() == this.getUserId();
	}
	
	
}
