package com.discord.randsteamgamebot.listeners;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

import java.util.List;

public interface Command {

    void runCommand(MessageReceivedEvent event, IMessage origMess, List<String> args);

}
