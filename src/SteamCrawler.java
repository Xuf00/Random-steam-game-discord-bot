/*
    This class is used to crawl the Steam page of a user
    to retrieve their games and Steam display name, provides
    methods to return a random game
*/

import java.io.File;
import java.io.FileNotFoundException;
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
    private Elements games;
    private String steamName;
    
    public SteamCrawler(IChannel channel, String profileURL) {
        this.channel = channel;
        createURL(profileURL);
        selectPageElements();
    }
    
    /*
        Navigate to the users game page and select the HTML elements that contain
        the users games and Steam display name
    */
    private void selectPageElements() {
        driver.get(profileURL);
        Document doc = Jsoup.parse(driver.getPageSource());
        games = doc.select(".gameListRow");
        steamName = doc.select("span.profile_small_header_name a").text();
    }
    
    // Choose a random game for the user to play
    public void randGame() {
        if (noGamesOwned()) { return ; }
        
        ArrayList<Game> allGames = getAllGames();
        
        Game randGame = chooseRandGame(allGames);
        String installGame = "steam://run/" + randGame.getGameID();

        RequestBuffer.request(() -> 
                channel.sendMessage(steamName + " owns " + allGames.size() + " games.\n"
                + "I'd recommend " + steamName + " plays **" + randGame.getGameName() + "**.\n" +
                        "Install or play the game: " + installGame));
        driver.quit();
    }
    
    // Choose a random game that the user has already played before
    public void randPlayedGame() {
        if (noGamesOwned()) { return ; }
        
        ArrayList<Game> allGames = getAllGames();
        ArrayList<Game> playedGames = filterGames(allGames, true);
        
        int totalGamesVal = allGames.size();
        int playedGameVal = playedGames.size();
        int gamePlayedPercent = (playedGameVal * 100 / totalGamesVal);
        
        Game randPlayedGame = chooseRandGame(playedGames);
        String installGame = "steam://run/" + randPlayedGame.getGameID();
        
        RequestBuffer.request(() ->
                channel.sendMessage(steamName + " has played " + playedGameVal + " of their games out of "
                    + totalGamesVal + " (" + gamePlayedPercent + "%)" + ".\n"
                    + "I recommend that " + steamName + " plays **" + randPlayedGame.getGameName()
                    + "**.\nThere is currently " + randPlayedGame.getHoursPlayed() + " on this game.\n"
                    + "Install or play the game: " + installGame));
        driver.quit();
    }
    
    // Choose a random game that the user has never played
    public void randUnplayedGame() {
        if (noGamesOwned()) { return ; }
        
        ArrayList<Game> allGames = getAllGames();
        ArrayList<Game> unplayedGames = filterGames(allGames, false);
        
        int totalGamesVal = allGames.size();
        int unplayedGameVal = unplayedGames.size();
        int gamePlayedPercent = (unplayedGameVal * 100 / totalGamesVal);
        
        Game randUnplayedGame = chooseRandGame(unplayedGames);
        String installGame = "steam://run/" + randUnplayedGame.getGameID();
        
        RequestBuffer.request(() ->
                channel.sendMessage(steamName + " hasn't played " + unplayedGameVal + " of their games out of "
                    + totalGamesVal + " (" + gamePlayedPercent + "%)" + ".\n"
                    + "I recommend that " + steamName + " plays **" + randUnplayedGame.getGameName() + "**.\n"
                    + "Install or play the game: " + installGame));
        driver.quit();
    }

    public void mostPlayedGames() {
        if (noGamesOwned()) { return ; }

        ArrayList<Game> allGames = getAllGames();
        ArrayList<Game> playedGames = filterGames(allGames, true);

        if (playedGames.size() < 5) {
            channel.sendMessage("You need to have played five games.");
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

        builder.appendField( "\t\t\t Hours Played",
                playedGames.get(0).getHoursPlayed() + "\n" +
                        playedGames.get(1).getHoursPlayed() + "\n" +
                        playedGames.get(2).getHoursPlayed() + "\n" +
                        playedGames.get(3).getHoursPlayed() + "\n" +
                        playedGames.get(4).getHoursPlayed(), true);

        RequestBuffer.request(() ->
                channel.sendMessage(builder.build()));
        driver.quit();
    }
    
    private Game chooseRandGame(ArrayList<Game> games) {
        Random r = new Random();
        int rand = r.nextInt(games.size());
        return games.get(rand);
    }
    
    // Get all of the games from the users game page
    private ArrayList<Game> getAllGames() {
        ArrayList<Game> allGames = new ArrayList<>();
        for (Element game : games) {
            String nextGame = game.select(".gameListRowItem .gameListRowItemName.ellipsis").text();
            String gameID = game.id().substring(5);
            if (!BotMain.dlcList.contains(nextGame)) {
                // Check if the game has play time, if not it hasn't been played yet
                if (game.select(".gameListRowItem h5").text().length() == 0) {
                    allGames.add(new Game(gameID, nextGame));
                }
                else {
                    String getHoursPlayed = game.select(".gameListRowItem h5").text();
                    allGames.add(new Game(gameID, nextGame, getHoursPlayed));
                }
            }
        }
        return allGames;
    }

    // Filter games by whether or not they have been played
    private ArrayList<Game> filterGames(ArrayList<Game> games, boolean played) {
        ArrayList<Game> temp = new ArrayList<>();
        for (Game game : games) {
            if (game.getPlayStatus() == played) {
                temp.add(game);
            }
        }
        return temp;
    }
    
    // Check if the user has a custom URL or Steam 64 URL and return their profile URL
    private void createURL(String profileID) {
        if (profileID.matches("\\d+")) {
            profileURL = "https://steamcommunity.com/profiles/"
                    + profileID + "/games/?tab=all";
        } else {
            profileURL = "https://steamcommunity.com/id/" 
                    + profileID + "/games/?tab=all";
        }
    }

    // Check to see if the user doesn't own any games
    private boolean noGamesOwned() {
        if (games.isEmpty()) {
            channel.sendMessage("Either your profile is private, "
                    + "you have provided an incorrect profile name or you own 0 games. "
                    + "Try again.");
            driver.quit();
            return true;
        }
        return false;
    }
    
    
}
