package com.discord.randsteamgamebot.command;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class RandGameTagCommand implements BotCommand {

    @Override
    public boolean matches(List<String> arguments) {
        return arguments.size() >= 4 && arguments.get(0).equals("rgame") && arguments.get(2).equals("tag");
    }

    @Override
    public void execute(MessageReceivedEvent event, List<String> args) {
        System.out.println("I should get a game for a user based on tag!");
    }
}
