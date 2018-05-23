package com.discord.randsteamgamebot.crawler;

/*
    This class is used to crawl the Steam page of a user
    to retrieve their games and Steam display name, provides
    methods to return a random game
*/

import com.discord.randsteamgamebot.domain.Game;
import com.discord.randsteamgamebot.domain.SteamUser;
import com.discord.randsteamgamebot.utils.BotUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

import java.util.ArrayList;
import java.util.Comparator;

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
        ArrayList<Game> allGames = Game.getAllGames(steamUser.getSteam64Id());

        if (Game.noGamesOwned(allGames)) {
            sendMessage("You either don't own any games or your privacy settings are affecting the result.");
            return ;
        }

        steamUser.setTotalGames(allGames.size());
        Game randGame = Game.chooseRandGame(allGames);
        String storePage = "http://store.steampowered.com/app/" + randGame.getGameID();

        sendMessage(steamUser.getDisplayName() + " owns " + allGames.size() + " games.\n"
                    + "I'd recommend " + steamUser.getDisplayName() + " plays **" + randGame.getGameName() + "**.\n" +
                    "Install or play the game: " + randGame.getInstallLink() + " or go to the store page: " + storePage);

        logger.debug("Successfully returned " + randGame.getGameName() + " for profile: " + steamUser.getDisplayName());
    }

    /**
     * Choose and return a random game for the user that they have already played before,
     * will also display the amount of games that they have played already along with
     * the percentage of games they have played
     */
    public void randPlayedGame() {

        ArrayList<Game> allGames = Game.getAllGames(steamUser.getSteam64Id());

        if (Game.noGamesOwned(allGames)) {
            sendMessage("You either don't own any games or your privacy settings are affecting the result.");
            return ;
        }

        steamUser.setTotalGames(allGames.size());
        ArrayList<Game> playedGames = Game.filterGames(allGames, true);

        if (Game.noGamesOwned(playedGames)) {
            sendMessage("You haven't played any games yet or your privacy setting is hiding your game play time.");
        }

        int playedGameVal = playedGames.size();
        float gamePlayedPercent = (playedGameVal * 100.0f) / steamUser.getTotalGames();
        
        Game randPlayedGame = Game.chooseRandGame(playedGames);
        String storePage = "http://store.steampowered.com/app/" + randPlayedGame.getGameID();

        sendMessage(steamUser.getDisplayName() + " has played " + playedGameVal + " of their games out of "
                    + steamUser.getTotalGames() + " (" + gamePlayedPercent + "%)" + ".\n"
                    + "I recommend that " + steamUser.getDisplayName() + " plays **" + randPlayedGame.getGameName()
                    + "**.\nThere is currently " + randPlayedGame.getGamePlayedTime() + " played on this game.\n"
                    + "Install or play the game: " + randPlayedGame.getInstallLink() + " or go to the store page: " + storePage);

        logger.debug("Successfully returned played game" + randPlayedGame.getGameName() + " for profile: " + steamUser.getDisplayName());

    }

    /**
     * Choose and return a random game for the user that they have not yet played,
     * will also display the amount of games that they haven't played along with
     * the percentage of games they have not yet played
     */
    public void randUnplayedGame() {

        ArrayList<Game> allGames = Game.getAllGames(steamUser.getSteam64Id());

        if (Game.noGamesOwned(allGames)) {
            sendMessage("You either don't own any games or your privacy settings are affecting the result.");
            return ;
        }

        steamUser.setTotalGames(allGames.size());
        ArrayList<Game> unplayedGames = Game.filterGames(allGames, false);

        if (Game.noGamesOwned(unplayedGames)) {
            sendMessage("You've played all of your games already.");
            return ;
        }

        int unplayedGameVal = unplayedGames.size();
        float gamePlayedPercent = (unplayedGameVal * 100.0f) / steamUser.getTotalGames();

        Game randUnplayedGame = Game.chooseRandGame(unplayedGames);
        String storePage = "http://store.steampowered.com/app/" + randUnplayedGame.getGameID();

        sendMessage(steamUser.getDisplayName() + " hasn't played " + unplayedGameVal + " of their games out of "
                + steamUser.getTotalGames() + " (" + gamePlayedPercent + "%)" + ".\n"
                + "I recommend that " + steamUser.getDisplayName() + " plays **" + randUnplayedGame.getGameName() + "**.\n"
                + "Install or play the game: " + randUnplayedGame.getInstallLink() + " or go to the store page: " + storePage);

        logger.debug("Successfully returned " + randUnplayedGame.getGameName() + " for profile: " + steamUser.getDisplayName());
    }

    /**
     * Return the users most played games on Steam,
     * nicely formatted and then output to them
     */
    public void mostPlayedGames() {

        ArrayList<Game> allGames = Game.getAllGames(steamUser.getSteam64Id());

        if (Game.noGamesOwned(allGames)) {
            sendMessage("You either don't own any games or your privacy settings are affecting the result.");
            return ;
        }

        steamUser.setTotalGames(allGames.size());
        ArrayList<Game> playedGames = Game.filterGames(allGames, true);

        if (playedGames.size() < 5) {
            sendMessage("You need to have played five games to use this command.");
            return;
        }

        EmbedBuilder embedBuilder = BotUtils.createEmbedBuilder(playedGames, "Most played games for " + steamUser.getDisplayName() + "\n",
                "The top five most played games on Steam for this user.", true);

        RequestBuffer.request(() ->
                channel.sendMessage(embedBuilder.build()));

        logger.debug("Successfully returned most played games for profile: " + steamUser.getDisplayName());
    }

    /**
     * Return the users least played games on Steam which at least have
     * some time logged against them
     */
    public void leastPlayedGames() {
        ArrayList<Game> allGames = Game.getAllGames(steamUser.getSteam64Id());

        if (Game.noGamesOwned(allGames)) {
            sendMessage("You either don't own any games or your privacy settings are affecting the result.");
            return ;
        }

        steamUser.setTotalGames(allGames.size());
        ArrayList<Game> playedGames = Game.filterGames(allGames, true);

        if (playedGames.size() < 5) {
            sendMessage("You need to have played five games to use this command.");
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
           channel.sendMessage(embedBuilder.build());
        });

        logger.debug("Succesfully returned least played games for profile: " + steamUser.getDisplayName());
    }

    /**
     * Send a message on the discord channel
     * @param message Message to send
     */
    private void sendMessage(String message) {
        RequestBuffer.request(() ->
            channel.sendMessage(message));
    }
}
