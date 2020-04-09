package com.discord.randsteamgamebot.command;

import com.discord.randsteamgamebot.domain.BotCommandArgs;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public interface BotCommand {
    boolean matches(List<String> arguments);

    void execute(BotCommandArgs commandArgs);
}
