package com.discord.randsteamgamebot.command.game;

import com.discord.randsteamgamebot.command.BotCommand;
import com.discord.randsteamgamebot.domain.BotCommandArgs;
import com.discord.randsteamgamebot.domain.BotUser;
import com.discord.randsteamgamebot.randomizer.GameRandomizer;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.List;

public class RandGameFilterCommand implements BotCommand {

    @Override
    public boolean matches(List<String> arguments) {
        if (arguments.size() != 3) {
            return false ;
        }
        return arguments.get(0).equals("rgame") && arguments.get(2).equals("played")
                || arguments.get(2).equals("unplayed");
    }

    @Override
    public void execute(BotCommandArgs botCommandArgs) {
        GameRandomizer gameRandomizer = botCommandArgs.getGameRandomizer();
        BotUser botUser = botCommandArgs.getBotUser();

        if (botCommandArgs.getCommandNames().get(2).equals("played"))
            gameRandomizer.randPlayedGame(botUser);
        else gameRandomizer.randUnplayedGame(botUser);
    }
}
