package com.discord.randsteamgamebot.command.game;

import com.discord.randsteamgamebot.command.BotCommand;
import com.discord.randsteamgamebot.domain.BotCommandArgs;

import java.util.List;

import static java.util.stream.Collectors.joining;

public class RandGameTagCommand implements BotCommand {

    @Override
    public boolean matches(List<String> arguments) {
        return arguments.size() >= 4 && arguments.get(0).equals("rgame") && arguments.get(2).equals("tag");
    }

    @Override
    public void execute(BotCommandArgs botCommandArgs) {
        String tag = botCommandArgs.getCommandNames().stream().skip(3).collect(joining(" ")).toLowerCase();

        botCommandArgs.getGameRandomizer().randGameByTag(botCommandArgs.getBotUser(), tag);
    }
}
