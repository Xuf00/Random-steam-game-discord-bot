package com.discord.randsteamgamebot.command.game;

import com.discord.randsteamgamebot.command.BotCommand;
import com.discord.randsteamgamebot.domain.BotCommandArgs;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class RandGameCommand implements BotCommand {

    @Override
    public boolean matches(List<String> arguments) {
        return arguments.size() == 2 && arguments.get(0).equals("rgame");
    }

    @Override
    public void execute(BotCommandArgs botCommandArgs) {
        botCommandArgs.getGameRandomizer().randGame(botCommandArgs.getBotUser());
    }
}
