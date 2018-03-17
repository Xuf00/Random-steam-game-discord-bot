/*
    This class is used to crawl the Steam page of a user
    to retrieve their games and Steam display name, provides
    methods to return a random game
*/

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

/**
 *
 * @author Jack
 */
public class SteamCrawler {
    
    private WebDriver driver = new ChromeDriver();
    private IChannel channel;
    private String profileURL;
    private ArrayList<Game> allGames = new ArrayList<>();
    private int totalGamesVal;
    private String steamName;
    
    public SteamCrawler(IChannel channel, String profileID) {
        this.channel = channel;
        createURL(profileID);
        selectPageElements();
    }

    /**
     * Select the game items from the users steam page and store them
     */
    private void selectPageElements() {
        driver.get(profileURL);
        Document doc = Jsoup.parse(driver.getPageSource());

        Elements games = doc.select(".gameListRow");
        allGames = getAllGames(games);
        if (noGamesOwned(allGames)) {
            sendMessage("Profile is either private, you own 0 games " +
                    "or you have provided an incorrect Steam name. Try again.");
            return ;
        }
        totalGamesVal = allGames.size();
        steamName = doc.select("span.profile_small_header_name a").text();
    }
    
    // Choose a random game for the user to play
    public void randGame() {
        Game randGame = chooseRandGame(allGames);
        String installLink = "steam://run/" + randGame.getGameID();

        sendMessage(steamName + " owns " + allGames.size() + " games.\n"
                    + "I'd recommend " + steamName + " plays **" + randGame.getGameName() + "**.\n" +
                    "Install or play the game: " + installLink);
        driver.quit();
    }

    /**
     * Choose and return a random game for the user that they have already played before,
     * will also display the amount of games that they have played already along with
     * the percentage of games they have played
     */
    public void randPlayedGame() {

        ArrayList<Game> playedGames = filterGames(allGames, true);

        if (noGamesOwned(playedGames)) {
            sendMessage("You haven't played any games yet.");
        }

        int playedGameVal = playedGames.size();
        int gamePlayedPercent = (playedGameVal * 100 / totalGamesVal);
        
        Game randPlayedGame = chooseRandGame(playedGames);
        String installLink = "steam://run/" + randPlayedGame.getGameID();

        sendMessage(steamName + " has played " + playedGameVal + " of their games out of "
                    + totalGamesVal + " (" + gamePlayedPercent + "%)" + ".\n"
                    + "I recommend that " + steamName + " plays **" + randPlayedGame.getGameName()
                    + "**.\nThere is currently " + randPlayedGame.getHoursPlayed() + " on this game.\n"
                    + "Install or play the game: " + installLink);
        driver.quit();
    }

    /**
     * Choose and return a random game for the user that they have not yet played,
     * will also display the amount of games that they haven't played along with
     * the percentage of games they have not yet played
     */
    public void randUnplayedGame() {

        ArrayList<Game> unplayedGames = filterGames(allGames, false);

        if (noGamesOwned(unplayedGames)) {
            sendMessage("You've played all of your games already.");
            return ;
        }

        int unplayedGameVal = unplayedGames.size();
        int gamePlayedPercent = (unplayedGameVal * 100 / totalGamesVal);
        
        Game randUnplayedGame = chooseRandGame(unplayedGames);
        String installLink = "steam://run/" + randUnplayedGame.getGameID();

        sendMessage(steamName + " hasn't played " + unplayedGameVal + " of their games out of "
                + totalGamesVal + " (" + gamePlayedPercent + "%)" + ".\n"
                + "I recommend that " + steamName + " plays **" + randUnplayedGame.getGameName() + "**.\n"
                + "Install or play the game: " + installLink);
        driver.quit();
    }

    /**
     * Return the users most played games on Steam,
     * nicely formatted and then output to them
     */
    public void mostPlayedGames() {

        ArrayList<Game> playedGames = filterGames(allGames, true);

        if(noGamesOwned(playedGames)) {
            sendMessage("You haven't played any games yet.");
            return;
        }
        if (playedGames.size() < 5) {
            sendMessage("You need to have played five games.");
            return;
        }

        EmbedBuilder builder = new EmbedBuilder();
        builder.withColor(41, 128, 185);
        builder.withDesc("The top five most played games on Steam for this user.");
        builder.withTitle(steamName + " most played games\n");

        builder.appendField("Game Name",
                playedGames.get(0).getGameName() + "\n" +
                playedGames.get(1).getGameName() + "\n" +
                playedGames.get(2).getGameName() + "\n" +
                playedGames.get(3).getGameName() + "\n" +
                playedGames.get(4).getGameName(), true);

        builder.appendField( "Hours Played",
                playedGames.get(0).getHoursPlayed() + "\n" +
                        playedGames.get(1).getHoursPlayed() + "\n" +
                        playedGames.get(2).getHoursPlayed() + "\n" +
                        playedGames.get(3).getHoursPlayed() + "\n" +
                        playedGames.get(4).getHoursPlayed(), true);

        RequestBuffer.request(() ->
                channel.sendMessage(builder.build()));
        driver.quit();
    }

    /**
     * Helpful method for choosing a random game
     * @param games The games in which to choose a random game from
     * @return A random game
     */
    private Game chooseRandGame(ArrayList<Game> games) {
        Random r = new Random();
        int rand = r.nextInt(games.size());
        return games.get(rand);
    }

    /**
     * Retrieve all of the users games
     * @param games The HTML elements from Steam which contain the games information
     * @return All of the users Steam games
     */
    private ArrayList<Game> getAllGames(Elements games) {
        ArrayList<Game> allGames = new ArrayList<>();
        for (Element game : games) {
            String currentGame = game.select(".gameListRowItem .gameListRowItemName.ellipsis").text();
            String gameID = game.id().substring(5);
            if (!BotMain.dlcList.contains(currentGame)) {
                // Check if the game has play time, if not it hasn't been played yet
                if (game.select(".gameListRowItem h5").text().length() == 0) {
                    allGames.add(new Game(gameID, currentGame));
                }
                else {
                    String getHoursPlayed = game.select(".gameListRowItem h5").text();
                    allGames.add(new Game(gameID, currentGame, getHoursPlayed));
                }
            }
        }
        return allGames;
    }

    /**
     * Filter games by whether they've been played or not
     * @param games The games to filter
     * @param played Whether or not the game has been played
     * @return
     */
    private ArrayList<Game> filterGames(ArrayList<Game> games, boolean played) {
        ArrayList<Game> temp = new ArrayList<>();
        for (Game game : games) {
            if (game.getPlayStatus() == played) {
                temp.add(game);
            }
        }
        return temp;
    }

    /**
     * Send a message on the discord channel
     * @param message Message to send
     */
    private void sendMessage(String message) {
        RequestBuffer.request(() ->
            channel.sendMessage(message));
    }

    /**
     * Create the URL required based on the users ID
     * @param profileID The users steam profile ID/name
     */
    private void createURL(String profileID) {
        if (profileID.matches("\\d+")) {
            profileURL = "https://steamcommunity.com/profiles/"
                    + profileID + "/games/?tab=all";
        } else {
            profileURL = "https://steamcommunity.com/id/" 
                    + profileID + "/games/?tab=all";
        }
    }

    /**
     * Check if the user actually owns any games
     * @param games The games to pass in
     * @return Whether or not at least one game exists
     */
    private boolean noGamesOwned(ArrayList<Game> games) {
        if (games.isEmpty()) {
            driver.quit();
            return true;
        }
        return false;
    }
}
