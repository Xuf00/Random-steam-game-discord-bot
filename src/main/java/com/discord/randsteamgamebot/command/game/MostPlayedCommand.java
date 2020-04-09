package com.discord.randsteamgamebot.command.game;

import com.discord.randsteamgamebot.command.BotCommand;
import com.discord.randsteamgamebot.domain.BotCommandArgs;

import java.util.List;

public class MostPlayedCommand implements BotCommand {

    @Override
    public boolean matches(List<String> arguments) {
        return arguments.size() == 2 && arguments.get(0).equals("mostplayed");
    }

    @Override
    public void execute(BotCommandArgs botCommandArgs) {
        botCommandArgs.getGameRandomizer().mostPlayedGames(botCommandArgs.getBotUser());
    }
}
