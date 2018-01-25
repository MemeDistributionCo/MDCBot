package com.mdc.bot.reaction;

import com.mdc.bot.util.event.CommandSentEvent;
import com.mdc.bot.util.event.RListener;

/**
 * A reaction which deletes specified command messages upon being sent
 * @author xDest
 *
 */
public class CommandReaction implements RListener {

	/**
	 * Catch command sent events and delete them if they should be deleted
	 * @param e The command sent event
	 */
	public void onCommandSent(CommandSentEvent e) {
		if(e.getBot().shouldDeleteCommand(e.getCommandSet().getCommandInstance().getClass())) {
			e.getMessageReceivedEvent().getMessage().delete().complete();
		}
	}
	
}
