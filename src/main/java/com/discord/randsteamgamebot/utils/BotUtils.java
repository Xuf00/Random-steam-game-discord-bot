package com.discord.randsteamgamebot.utils;

import com.discord.randsteamgamebot.domain.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;

import java.util.ArrayList;
import java.util.Comparator;

public class BotUtils {

    private static Logger logger = LoggerFactory.getLogger(BotUtils.class);

    public static IDiscordClient createClient(String token, boolean login) {
        ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken(token);
        clientBuilder.withRecommendedShardCount();
        try {
            if (login) {
                return clientBuilder.login();
            } else {
                return clientBuilder.build();
            }
        } catch (DiscordException e) {
            logger.error("Couldn't create the discord client.");
            return null;
        }
    }

    public static EmbedBuilder createEmbedBuilder(ArrayList<Game> games, String title, String desc, boolean mostPlayed) {
        if (mostPlayed) {
            games.sort(Comparator.comparingInt(Game::getMinutesPlayed).reversed());
        } else {
            games.sort(Comparator.comparingInt(Game::getMinutesPlayed));
        }

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.withColor(41, 128, 185);
        embedBuilder.withTitle(title);
        embedBuilder.withDesc(desc);

        embedBuilder.appendField("Game Name",
                games.get(0).getEmbedLink() + "\n" +
                        games.get(1).getEmbedLink() + "   "   + "\n" +
                        games.get(2).getEmbedLink() + "   "   + "\n" +
                        games.get(3).getEmbedLink() + "   "   + "\n" +
                        games.get(4).getEmbedLink(), true);

        embedBuilder.appendField( "Time Played",
                games.get(0).getGamePlayedTime() + "\n" +
                        games.get(1).getGamePlayedTime() + "\n" +
                        games.get(2).getGamePlayedTime() + "\n" +
                        games.get(3).getGamePlayedTime() + "\n" +
                        games.get(4).getGamePlayedTime(), true);

        return embedBuilder;
    }

}
