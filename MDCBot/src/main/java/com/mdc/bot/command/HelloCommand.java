package com.mdc.bot.command;

import com.mdc.bot.MDCBot;

import net.dv8tion.jda.core.MessageBuilder;

/**
 * First command in the bot. Says hello with a mention.
 * @author xDestx
 *
 */
public class HelloCommand implements Command {

	private String help;
	/**
	 * Create help text
	 */
	public HelloCommand()
	{
		help = "Usage: `--hello`";
	}
	
	@Override
	public boolean called(CommandSet s, MDCBot b) {
		//There is no way to call this wrong...
		return true;
	}

	@Override
	public void action(CommandSet s, MDCBot b) {
		//getTextChannel because getChannel could return ... a voice channel?
		if(s.getMessageReceivedEvent().getTextChannel().canTalk()) {
			MessageBuilder mb = new MessageBuilder();
			mb.append("Hello, ");
			mb.append(s.getSender());
			mb.append("!");
			b.sendMessage(s.getMessageReceivedEvent().getTextChannel(), mb);
		} else {
			System.out.println("Can't send messages in channel " + s.getMessageReceivedEvent().getChannel().getName());
		}
	}

	@Override
	public String getHelpMessage() {
		return help;
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
