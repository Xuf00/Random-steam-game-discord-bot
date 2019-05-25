package com.discord.randsteamgamebot.command;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public interface BotCommand {
    boolean matches(List<String> arguments);

    void execute(MessageReceivedEvent event, List<String> args);
}
