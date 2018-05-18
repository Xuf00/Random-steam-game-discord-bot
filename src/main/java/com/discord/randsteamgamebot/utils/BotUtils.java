package com.discord.randsteamgamebot.utils;

import com.discord.randsteamgamebot.domain.Game;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.GuildLeaveEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.ArrayList;
import java.util.Comparator;

public class BotUtils {

    private static Logger logger = LoggerFactory.getLogger(BotUtils.class);

    public final static String BOT_PREFIX = "!";

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

    // Build and display the list of commands to the user
    public static void commandList(IChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withColor(41, 128, 185);
        builder.appendDescription("Grabs all of a users games on Steam and selects a random game. "
                + "User can filter whether or not they want a random game they haven't played before. Can also see top played and least played games.");
        builder.appendField("Commands   ", "!rgame [name/17 digit ID]"
                + "           " + "\n!rgame [name/17 digit ID] [played/unplayed]"
                + "           " + "\n!mostplayed [name/17 digit ID]"
                + "           " + "\n!leastplayed [name/17 digit ID]"
                + "           " + "\n\nGithub link: https://git.io/vxBc6", true);
        builder.appendField("Example", "!rgame Xufoo\n!rgame 76561198054740594 played\n!mostplayed Xufoo\n!leastplayed Xufoo", true);
        RequestBuffer.request(() -> channel.sendMessage(builder.build()));
    }

    public static void messageOwner(GuildLeaveEvent event) {

    }

}
