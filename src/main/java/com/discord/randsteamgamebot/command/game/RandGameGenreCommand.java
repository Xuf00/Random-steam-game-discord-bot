package com.discord.randsteamgamebot.command.game;

import com.discord.randsteamgamebot.command.BotCommand;
import com.discord.randsteamgamebot.domain.BotCommandArgs;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class RandGameGenreCommand implements BotCommand {

    @Override
    public boolean matches(List<String> arguments) {
        return arguments.size() == 3 && arguments.get(0).equals("rgame");
    }

    @Override
    public void execute(BotCommandArgs botCommandArgs) {
        String genre = botCommandArgs.getCommandNames().stream().skip(1).collect(joining(" ")).toLowerCase();

        botCommandArgs.getGameRandomizer().randGameByGenre(botCommandArgs.getBotUser(), genre);
    }
}
