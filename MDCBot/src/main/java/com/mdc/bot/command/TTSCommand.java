package com.mdc.bot.command;

import com.mdc.bot.MDCBot;
import com.mdc.bot.util.Util;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

/**
 * TTS Command to toggle or check TTS status of the bot
 * @author xDestx
 *
 */
public class TTSCommand implements Command {

	@Override
	public boolean called(CommandSet s, MDCBot b) {
		Guild g = s.getServer();
		User u = s.getSender();
		return Util.isUserSD(Util.userToMember(u, g));
	}

	@Override
	public void action(CommandSet s, MDCBot bot) {
		if(s.getArgs().length == 0) {
			//Display TTS status
			bot.sendMessage(s.getMessageReceivedEvent().getTextChannel(), (new MessageBuilder()).append("TTS is " + (bot.isTTS()?"on":"off")));
		} else if (s.getArgs().length >= 1) {
			try {
				Boolean b = Boolean.parseBoolean(s.getArgs()[0]);
				setTTS(b.booleanValue(), bot);
			} catch (Exception e) {
				//Just toggle
				toggleTTS(bot);
			}
			bot.sendMessage(s.getMessageReceivedEvent().getTextChannel(), (new MessageBuilder()).append("TTS set to " + bot.isTTS()));
		}
	}

	@Override
	public String getHelpMessage() {
		return "--tts to show the current status, or --tts <true/false> to set";
	}
	
	private void setTTS(boolean val, MDCBot b) {
		b.setTTS(val);
	}
	
	private void toggleTTS(MDCBot b) {
		b.setTTS(!b.isTTS());
	}

	@Override
	public Command[] getChildCommands() {
		return new Command[0];
	}
	
	@Override
	public boolean isRootCommand() {
		return getParentCommand() == this;
	}

	@Override
	public Command getParentCommand() {
		return this;
	}
	
}
