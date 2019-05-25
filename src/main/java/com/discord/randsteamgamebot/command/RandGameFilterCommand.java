package com.discord.randsteamgamebot.command;

import com.discord.randsteamgamebot.domain.SteamUser;
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
    public void execute(MessageReceivedEvent event, List<String> args) {
        String steamName = args.get(1);

        SteamUser steamUser = SteamUser.createSteamUser(steamName);

        if (steamUser == null) {
            event.getChannel().sendMessage("This profile is either private or does not exist, set your privacy to public and try again.");
            return ;
        }
        steamUser.setDiscordRequester(event.getAuthor());
        steamUser.setUserChannel(event.getChannel());

        GameRandomizer crawler = new GameRandomizer(steamUser);

        if (args.get(2).equals("played")) {
            crawler.mostPlayedGames();
        } else {
            crawler.leastPlayedGames();
        }
    }
}
