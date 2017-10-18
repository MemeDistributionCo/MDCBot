package com.mdc.bot.command;

import com.mdc.bot.MDCBot;

import net.dv8tion.jda.core.MessageBuilder;

public class HelpCommand implements Command {

	@Override
	public boolean called(CommandSet s) {
		return true;
	}

	@Override
	public void action(CommandSet s) {
		Command needHelpCommand;
		if (s.getArgs().length == 0) {
			needHelpCommand = this;
		} else {
			String commandName = s.getArgs()[0];
			CommandLabel cl = null;
			for(CommandLabel commandLabel : CommandLabel.values()) {
				if(commandLabel.getLabel().equalsIgnoreCase(commandName)) {
					cl = commandLabel;
					break;
				}
			}
			if(cl == null) {
				needHelpCommand = null;
			} else {
				try {
					needHelpCommand = (Command) cl.getCommandClass().newInstance();
				} catch (InstantiationException | IllegalAccessException e) {
					//e.printStackTrace();
					needHelpCommand = null;
				}
			}
		}
		if(needHelpCommand == null) {
			MDCBot.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("Could not find a command with that label."));
		} else {
			MDCBot.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append(needHelpCommand.getHelpMessage()));
		}
	}

	@Override
	public String getHelpMessage() {
		// TODO Auto-generated method stub
		return "`--help <command name>`";
	}

}
