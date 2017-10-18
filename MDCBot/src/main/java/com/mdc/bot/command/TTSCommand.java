package com.mdc.bot.command;

import com.mdc.bot.MDCBot;
import com.mdc.bot.util.PermUtil;

import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;

public class TTSCommand implements Command {

	@Override
	public boolean called(CommandSet s) {
		Guild g = s.getServer();
		User u = s.getSender();
		return PermUtil.isUserSD(PermUtil.userToMember(u, g));
	}

	@Override
	public void action(CommandSet s) {
		if(s.getArgs().length == 0) {
			//Toggle
			toggleTTS();
		} else if (s.getArgs().length >= 1) {
			try {
				Boolean b = Boolean.parseBoolean(s.getArgs()[0]);
				setTTS(b.booleanValue());
			} catch (Exception e) {
				//Just toggle
				toggleTTS();
			}
		}
		MDCBot.sendMessage(s.getMessageReceivedEvent().getTextChannel(), (new MessageBuilder()).append("TTS set to " + MDCBot.ttsMode));
	}

	@Override
	public String getHelpMessage() {
		return "--tts to toggle, or --tts <true/false> to set";
	}
	
	private void setTTS(boolean b) {
		MDCBot.ttsMode = b;
	}
	
	private void toggleTTS() {
		MDCBot.ttsMode = !MDCBot.ttsMode;
	}

}
