package com.discord.randsteamgamebot.crawler;

/*
    This class is used to crawl the Steam page of a user
    to retrieve their games and Steam display name, provides
    methods to return a random game
*/

import com.discord.randsteamgamebot.domain.Game;
import com.discord.randsteamgamebot.domain.SteamUser;
import com.discord.randsteamgamebot.utils.BotUtils;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Jack
 */
public class SteamCrawler {

    private Logger logger = LoggerFactory.getLogger(SteamCrawler.class);

    public static String steamApiToken;
    private IChannel channel;
    private SteamUser steamUser;

    public SteamCrawler(IChannel channel, SteamUser steamUser) {
        this.channel = channel;
        this.steamUser = steamUser;
    }

    /**
     * Choose a random game for the user to play.
     */
    public void randGame() {
        IMessage message = sendMessage("Retrieving information for " + steamUser.getDisplayName() + "...");;

        ArrayList<Game> allGames = Game.getAllGames(steamUser.getSteam64Id());

        if (Game.noGamesOwned(allGames)) {
            message.edit("You either don't own any games or your privacy settings are affecting the result.");
            return ;
        }

        steamUser.setTotalGames(allGames.size());
        Game randGame = Game.chooseRandGame(allGames);
        String storePage = "http://store.steampowered.com/app/" + randGame.getGameID();

        message.edit(steamUser.getDisplayName() + " owns " + allGames.size() + " games.\n"
                    + "I'd recommend " + steamUser.getDisplayName() + " plays **" + randGame.getGameName() + "**.\n" +
                    "Install or play the game: " + randGame.getInstallLink() + " or go to the store page: " + storePage);

        logger.info("Successfully returned played game " + randGame.getGameName() + " for profile: " + steamUser.getDisplayName());
    }

    /**
     * Choose and return a random game for the user that they have already played before,
     * will also display the amount of games that they have played already along with
     * the percentage of games they have played
     */
    public void randPlayedGame() {
        IMessage message = sendMessage("Retrieving information for " + steamUser.getDisplayName() + "...");;

        ArrayList<Game> allGames = Game.getAllGames(steamUser.getSteam64Id());

        if (Game.noGamesOwned(allGames)) {
            message.edit("You either don't own any games or your privacy settings are affecting the result.");
            return ;
        }

        steamUser.setTotalGames(allGames.size());
        ArrayList<Game> playedGames = Game.filterGames(allGames, true);

        if (Game.noGamesOwned(playedGames)) {
            message.edit("You haven't played any games yet or your privacy setting is hiding your game play time.");
        }

        int playedGameVal = playedGames.size();
        float gamePlayedPercent = (playedGameVal * 100.0f) / steamUser.getTotalGames();
        
        Game randPlayedGame = Game.chooseRandGame(playedGames);
        String storePage = "http://store.steampowered.com/app/" + randPlayedGame.getGameID();

        message.edit(steamUser.getDisplayName() + " has played " + playedGameVal + " of their games out of "
                    + steamUser.getTotalGames() + " (" + gamePlayedPercent + "%)" + ".\n"
                    + "I recommend that " + steamUser.getDisplayName() + " plays **" + randPlayedGame.getGameName()
                    + "**.\nThere is currently " + randPlayedGame.getGamePlayedTime() + " played on this game.\n"
                    + "Install or play the game: " + randPlayedGame.getInstallLink() + " or go to the store page: " + storePage);

        logger.info("Successfully returned played game " + randPlayedGame.getGameName() + " for profile: " + steamUser.getDisplayName());

    }

    /**
     * Choose and return a random game for the user that they have not yet played,
     * will also display the amount of games that they haven't played along with
     * the percentage of games they have not yet played
     */
    public void randUnplayedGame() {
        IMessage message = sendMessage("Retrieving information for " + steamUser.getDisplayName() + "...");;

        ArrayList<Game> allGames = Game.getAllGames(steamUser.getSteam64Id());

        if (Game.noGamesOwned(allGames)) {
            message.edit("You either don't own any games or your privacy settings are affecting the result.");
            return ;
        }

        steamUser.setTotalGames(allGames.size());
        ArrayList<Game> unplayedGames = Game.filterGames(allGames, false);

        if (Game.noGamesOwned(unplayedGames)) {
            message.edit("You've played all of your games already.");
            return ;
        }

        int unplayedGameVal = unplayedGames.size();
        float gamePlayedPercent = (unplayedGameVal * 100.0f) / steamUser.getTotalGames();

        Game randUnplayedGame = Game.chooseRandGame(unplayedGames);
        String storePage = "http://store.steampowered.com/app/" + randUnplayedGame.getGameID();

        message.edit(steamUser.getDisplayName() + " hasn't played " + unplayedGameVal + " of their games out of "
                + steamUser.getTotalGames() + " (" + gamePlayedPercent + "%)" + ".\n"
                + "I recommend that " + steamUser.getDisplayName() + " plays **" + randUnplayedGame.getGameName() + "**.\n"
                + "Install or play the game: " + randUnplayedGame.getInstallLink() + " or go to the store page: " + storePage);

        logger.info("Successfully returned played game " + randUnplayedGame.getGameName() + " for profile: " + steamUser.getDisplayName());
    }

    /**
     * Return the users most played games on Steam,
     * nicely formatted and then output to them
     */
    public void mostPlayedGames() {
        IMessage message = sendMessage("Retrieving information for " + steamUser.getDisplayName() + "...");;

        ArrayList<Game> allGames = Game.getAllGames(steamUser.getSteam64Id());

        if (Game.noGamesOwned(allGames)) {
            message.edit("You either don't own any games or your privacy settings are affecting the result.");
            return ;
        }

        steamUser.setTotalGames(allGames.size());
        ArrayList<Game> playedGames = Game.filterGames(allGames, true);

        if (playedGames.size() < 5) {
            message.edit("You need to have played five games to use this command.");
            return ;
        }

        EmbedBuilder embedBuilder = BotUtils.createEmbedBuilder(playedGames, "Most played games for " + steamUser.getDisplayName() + "\n",
                "The top five most played games on Steam for this user.", true);

        RequestBuffer.request(() ->
                message.edit(embedBuilder.build()));

        logger.info("Successfully returned most played games for profile: " + steamUser.getDisplayName());
    }

    /**
     * Return the users 5 least played games on Steam which at least have
     * some time logged against them
     */
    public void leastPlayedGames() {
        IMessage message = sendMessage("Retrieving information for " + steamUser.getDisplayName() + "...");;

        ArrayList<Game> allGames = Game.getAllGames(steamUser.getSteam64Id());

        if (Game.noGamesOwned(allGames)) {
            message.edit("You either don't own any games or your privacy settings are affecting the result.");
            return ;
        }

        steamUser.setTotalGames(allGames.size());
        ArrayList<Game> playedGames = Game.filterGames(allGames, true);

        if (playedGames.size() < 5) {
            message.edit("You need to have played five games to use this command.");
            return ;
        }

        playedGames.sort(Comparator.comparingInt(Game::getMinutesPlayed));

        EmbedBuilder builder = new EmbedBuilder();
        builder.withColor(41, 128, 185);
        builder.withDesc("The top five least played games on Steam for this user.");
        builder.withTitle("Least played games for " + steamUser.getDisplayName() + "\n");

        EmbedBuilder embedBuilder = BotUtils.createEmbedBuilder(playedGames, "Least played games for " + steamUser.getDisplayName() + "\n",
                "The top five least played games on Steam for this user. (With playtime)", false);

        RequestBuffer.request(() -> {
           message.edit(embedBuilder.build());
        });

        logger.info("Successfully returned least played games for profile: " + steamUser.getDisplayName());
    }

    /**
     * Return a random game that the user owns based on genre
     * @param genre The genre to search
     */
    public void randGameByGenre(String genre) {
        IMessage message = sendMessage("Retrieving information for " + steamUser.getDisplayName() + "...");

        ArrayList<Game> allGames = Game.getAllGames(steamUser.getSteam64Id());

        if (Game.noGamesOwned(allGames)) {
            message.edit("You either don't own any games or your privacy settings are affecting the result.");
            return ;
        }

        steamUser.setTotalGames(allGames.size());

        try {
            HttpResponse<JsonNode> response = Unirest.get("https://steamspy.com/api.php?request=genre&genre=" + URLEncoder.encode(genre, "UTF-8")).asJson();
            JSONObject object = response.getBody().getObject();

            allGames.forEach(game -> {
                if (object.has(game.getGameID())) {
                    System.out.println(game.getGameName() + " has the genre " + genre);
                }
            });

            List<Game> gamesByGenre = allGames.stream()
                    .filter(game -> object.has(game.getGameID()))
                    .collect(Collectors.toList());

            if (Game.noGamesOwned(gamesByGenre)) {
                message.edit("You don't own any games in this genre or you entered it incorrectly.");
                return ;
            }

            Game randomGameFromGenre = Game.chooseRandGame(gamesByGenre);

            String storePage = "http://store.steampowered.com/app/" + randomGameFromGenre.getGameID();

            message.edit(steamUser.getDisplayName() + " owns " + gamesByGenre.size() + " games in the **" + genre + "** genre.\n"
                    + "I'd recommend " + steamUser.getDisplayName() + " plays **" + randomGameFromGenre.getGameName() + "**.\n" +
                    "Install or play the game: " + randomGameFromGenre.getInstallLink() + " or go to the store page: " + storePage);

            logger.info("Successfully returned game from genre " + genre + " " + randomGameFromGenre.getGameName() + " for profile: " + steamUser.getDisplayName());
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }



    /**
     * Send a message on the discord channel
     * @param message Message to send
     */
    private IMessage sendMessage(String message) {
        RequestBuffer.RequestFuture<IMessage> request = RequestBuffer.request(() ->
                channel.sendMessage(message));
        return request.get();
    }
}
