package com.discord.randsteamgamebot.listeners;

import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;

import java.util.List;
import java.util.concurrent.Future;

public interface Command {

    void runCommand(MessageReceivedEvent event, Future<IMessage> origMess, List<String> args);

}
