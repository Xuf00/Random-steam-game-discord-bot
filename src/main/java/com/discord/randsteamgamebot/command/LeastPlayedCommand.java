package com.discord.randsteamgamebot.command;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class LeastPlayedCommand implements BotCommand {

    @Override
    public boolean matches(List<String> arguments) {
        return arguments.size() == 2 && arguments.get(0).equals("leastplayed");
    }

    @Override
    public void execute(MessageReceivedEvent event, List<String> args) {

    }
}
