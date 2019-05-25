package com.discord.randsteamgamebot.command;

import com.discord.randsteamgamebot.utils.BotUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class SbHelpCommand implements BotCommand {

    @Override
    public boolean matches(List<String> arguments) {
        return arguments.size() == 1 && arguments.get(0).equals("sbhelp");
    }

    @Override
    public void execute(MessageReceivedEvent event, List<String> args) {
        BotUtils.commandList(event.getChannel());
    }
}
