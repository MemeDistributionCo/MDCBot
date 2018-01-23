package com.mdc.bot.command;

import com.mdc.bot.MDCBot;

import net.dv8tion.jda.core.MessageBuilder;
/**
 * Help command displays help text of given commands
 * @author xDestx
 *
 */
public class HelpCommand implements Command {

	@Override
	public boolean called(CommandSet s, MDCBot b) {
		return true;
	}

	@Override
	public void action(CommandSet s, MDCBot b) {
		Command needHelpCommand;
		if (s.getArgs().length == 0) {
			needHelpCommand = this;
		} else {
			String commandName = s.getArgs()[0];
			CommandLabel cl = null;
			for(CommandLabel commandLabel : b.getCommandLabels()) {
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
			b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append("Could not find a command with that label."));
		} else {
			b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), new MessageBuilder().append(needHelpCommand.getHelpMessage()));
		}
	}

	@Override
	public String getHelpMessage() {
		// TODO Auto-generated method stub
		return "`--help <command name>`";
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
