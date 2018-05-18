package com.discord.randsteamgamebot.listeners;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public interface Command {

    void runCommand(MessageReceivedEvent event, List<String> args);

}
