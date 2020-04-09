package com.discord.randsteamgamebot.domain;

import com.discord.randsteamgamebot.randomizer.GameRandomizer;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class BotCommandArgs {
    private MessageReceivedEvent event;
    private List<String> commandNames;
    private BotUser botUser;
    private GameRandomizer gameRandomizer;

    public MessageReceivedEvent getEvent() {
        return event;
    }

    public void setEvent(MessageReceivedEvent event) {
        this.event = event;
    }

    public List<String> getCommandNames() {
        return commandNames;
    }

    public void setCommandNames(List<String> commandName) {
        this.commandNames = commandName;
    }

    public BotUser getBotUser() {
        return botUser;
    }

    public void setBotUser(BotUser botUser) {
        this.botUser = botUser;
    }

    public GameRandomizer getGameRandomizer() {
        return gameRandomizer;
    }

    public void setGameRandomizer(GameRandomizer gameRandomizer) {
        this.gameRandomizer = gameRandomizer;
    }
}
