package com.discord.randsteamgamebot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BotUtils {

    private static Logger logger = LoggerFactory.getLogger(BotUtils.class);

    public static String STEAM_API_KEY;
    public final static String BOT_PREFIX = "!";
    /*public static final ReactionEmoji DELETE_EMOJI = ReactionEmoji.of("❌");*/
    private final static int BOT_COL_RED = 41;
    private final static int BOT_COL_GREEN = 128;
    private final static int BOT_COL_BLUE = 185;

    /*private static EmbedBuilder createEmbedBuilder(String title) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.withTitle(title);
        embedBuilder.withColor(BOT_COL_RED, BOT_COL_GREEN, BOT_COL_BLUE);

        return embedBuilder;
    }*/

    /*public static EmbedObject embedBuilderForGenre(String title) {
        EmbedBuilder embedBuilder = createEmbedBuilder(title);

        embedBuilder.appendField("Possible genres: \n",
                "1.     " + GameGenres.gameGenreMap.get("early access") + "     " + "\n" +
                        "2.    " + GameGenres.gameGenreMap.get("action") + "     " + "\n" +
                        "3.    " + GameGenres.gameGenreMap.get("strategy") + "     " + "\n" +
                        "4.    " + GameGenres.gameGenreMap.get("indie") + "     " + "\n" +
                        "5.    " + GameGenres.gameGenreMap.get("rpg") + "     " + "\n" +
                        "6.    " + GameGenres.gameGenreMap.get("racing") + "     " + "\n" +
                        "7.    " + GameGenres.gameGenreMap.get("sports") + "     " + "\n" +
                        "8.    " + GameGenres.gameGenreMap.get("free") + "     " + "\n" +
                        "9.  " + GameGenres.gameGenreMap.get("adventure") + "     " + "\n" +
                        "10.   " + GameGenres.gameGenreMap.get("simulation") + "     " + "\n" +
                        "11.  " + GameGenres.gameGenreMap.get("casual") + "     " + "\n" +
                        "12.  " + GameGenres.gameGenreMap.get("multiplayer") + "     ", true);

        return embedBuilder.build();
    }*/

    /*public static EmbedObject embedBuilderForTags(String title) {
        EmbedBuilder embedBuilder = createEmbedBuilder(title);

        embedBuilder.appendField("Available tags: \n",
                "1. Indie\n" +
                        "2. Turn-Based\n" +
                        "3. VR\n" +
                        "4. Replay Value\n" +
                        "5. Visual Novel\n" +
                        "6. Steampunk\n" +
                        "7. Crime\n" +
                        "Visit [Steam Database](https://steamdb.info/tags/) for more tags."
                ,true);

        return embedBuilder.build();
    }*/

    /*public static EmbedObject createEmbedBuilder(List<Game> games, String title, String desc, boolean mostPlayed) {
        if (mostPlayed) {
            games.sort(Comparator.comparingInt(Game::getMinutesPlayed).reversed());
        } else {
            games.sort(Comparator.comparingInt(Game::getMinutesPlayed));
        }

        EmbedBuilder embedBuilder = createEmbedBuilder(title);

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

        return embedBuilder.build();
    }*/

    // Build and display the list of commands to the user
    /*public static void commandList(IChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withColor(41, 128, 185);
        builder.appendDescription("**Important: Your Steam Privacy settings will affect the outcome, so try to make everything public.**\n\n"
                                    + "Grabs all of a users games on Steam and selects a random game."
                                    + " User can filter whether or not they want a random game they haven't played before. Can also see top played and least played games. **New feature to filter by tags.**");
        builder.appendField("Commands   ", "!rgame <name/17 digit ID>"
                                    + "           " + "\n!rgame <name/17 digit ID> <played/unplayed>"
                                    + "           " + "\n!rgame <name/17 digit ID> <genre>"
                                    + "           " + "\n!rgame <name/17 digit ID> tag <tagname>"
                                    + "           " + "\n!mostplayed <name/17 digit ID>"
                                    + "           " + "\n!leastplayed <name/17 digit ID>"
                                    + "           " + "\n\n[Github](https://git.io/vxnPL)"
                                    + "           " + "\n[Support Server](https://discord.gg/StGwXfy)"
                                    + "           " + "\n[Donate](https://donatebot.io/checkout/447433414665306122)",true);
        builder.appendField("Example", "!rgame Xufoo\n" +
                                                     "!rgame 76561198054740594 played\n" +
                                                     "!rgame Xufoo action\n" +
                                                     "!rgame Xufoo tag great soundtrack\n" +
                                                     "!mostplayed Xufoo\n" +
                                                     "!leastplayed Xufoo", true);
        builder.appendField("Special Mention", "[Steamspy](http://steamspy.com/) for the genre and tags information.", true);


        RequestBuffer.request(() -> channel.sendMessage(builder.build()).addReaction(DELETE_EMOJI));
    }*/

    /**
     * Send a message on the discord channel
     */
    /*public static Future<IMessage> sendInitialMessage(IChannel channel, IUser user, String profile) {
        return RequestBuffer.request(() ->
                channel.sendMessage(user + " - Retrieving information for the profile " + profile + "..."));
    }*/

    /*public static void editMessage(IMessage message, SteamUser steamUser, String content) {
        RequestBuffer.request(() -> {
            message.edit(steamUser.getDiscordRequester() + " - " + content).addReaction(DELETE_EMOJI);
        });
    }*/

   /* public static void editMessage(IMessage message, String content) {
        RequestBuffer.request(() -> {
            message.edit(content).addReaction(DELETE_EMOJI);
        });
    }

    public static void editMessage(IMessage message, SteamUser steamUser, EmbedObject content) {
        RequestBuffer.request(() -> {
            message.edit(steamUser.getDiscordRequester() + "", content).addReaction(DELETE_EMOJI);
        });
    }

    public static boolean botLoggedInAndReady(IDiscordClient client) {
        return client.isReady() && client.isLoggedIn();
    }

    public static void deleteMessage(IMessage message) {
        RequestBuffer.request(message::delete);
    }*/
}
