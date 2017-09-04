/*
    This class is used to crawl the Steam page of a user
    to retrieve their games and Steam display name, provides
    methods to return a random game
*/

package com.mycompany.firstdiscordbot;

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
import sx.blah.discord.util.RequestBuffer;

/**
 *
 * @author Jack
 */
public class SteamCrawler {
    
    private WebDriver driver;
    private IChannel channel;
    private String profileURL;
    private Elements games;
    private Elements steamName;
    private ArrayList<String> DLCList = new ArrayList<>();
    
    public SteamCrawler(IChannel channel, String profileURL) {
        try {
            Scanner s = new Scanner(new File("D:\\Jack\\Documents\\NetBeansProjects\\SeleniumandJSoup\\ListOfDLC.txt"));
            while (s.hasNextLine()){
                DLCList.add(s.nextLine());
            }
            s.close();
        } catch (FileNotFoundException f) {
            System.out.println("File could not be found.");
        }
        this.channel = channel;
        createURL(profileURL);
        driver = new ChromeDriver();
        selectPageElements();
    }
    
    /*
        Navigate to the users game page and select the HTML elements that contain
        the users games and Steam display name
    */
    private void selectPageElements() {
        driver.get(profileURL);
        Document doc = Jsoup.parse(driver.getPageSource());
        games = doc.select("div#mainContents "
                + "div.gameListRowItem");
        steamName = doc.select("span.profile_small_header_name a");
    }
    
    // Choose a random game for the user to play
    public void randGame() {
        if (checkIfNoGames()) { driver.quit(); return ; }
        
        ArrayList<Game> allGames = getAllGames();
        
        Game randGame = chooseRandGame(allGames);
        RequestBuffer.request(() -> 
                channel.sendMessage(steamName.text() + " owns " + allGames.size() + " games.\n"
                + "I'd recommend " + steamName.text() + " plays **" + randGame.getGameName() + "**."));
        driver.quit();
    }
    
    // Choose a random game that the user has already played before
    public void randPlayedGame() {
        if (checkIfNoGames()) { driver.quit(); return ; }
        
        ArrayList<Game> allGames = getAllGames();
        ArrayList<Game> playedGames = filterByPlayed(allGames);
        
        int totalGamesVal = allGames.size();
        int playedGameVal = playedGames.size();
        int gamePlayedPercent = (int) (playedGameVal * 100 / totalGamesVal);
        
        Game randPlayedGame = chooseRandGame(playedGames);
        
        RequestBuffer.request(() ->
                channel.sendMessage(steamName.text() + " has played " + playedGameVal + " of their games out of "
                    + totalGamesVal + " (" + gamePlayedPercent + "%)" + ".\n"
                    + "I recommend that " + steamName.text() + " plays **" + randPlayedGame.getGameName()
                    + "**.\nThere is currently " + randPlayedGame.getHoursPlayed() + " on this game."));
        driver.quit();
    }
    
    // Choose a random game that the user has never played
    public void randUnplayedGame() {
        if (checkIfNoGames()) { return ; }
        
        ArrayList<Game> allGames = getAllGames();
        ArrayList<Game> unplayedGames = filterByUnplayed(allGames);
        
        int totalGamesVal = allGames.size();
        int unplayedGameVal = unplayedGames.size();
        int gamePlayedPercent = (int) (unplayedGameVal * 100 / totalGamesVal);
        
        Game randUnplayedGame = chooseRandGame(unplayedGames);
        
        RequestBuffer.request(() ->
                channel.sendMessage(steamName.text() + " hasn't played " + unplayedGameVal + " of their games out of "
                    + totalGamesVal + " (" + gamePlayedPercent + "%)" + ".\n"
                    + "I recommend that " + steamName.text() + " plays **" + randUnplayedGame.getGameName() + "**."));
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
            String nextGame = game.select(".gameListRowItemName.ellipsis").text();
            if (!DLCList.contains(nextGame)) {
                // Check if the game has play time, if not it hasn't been played yet
                if (game.select("h5").text().length() == 0) {
                    allGames.add(new Game(nextGame));
                }
                else {
                    String getHoursPlayed = game.select("h5").text();
                    allGames.add(new Game(nextGame, getHoursPlayed));
                }
            }
        }
        return allGames;
    }
    
    private ArrayList<Game> filterByPlayed(ArrayList<Game> games) {
        ArrayList<Game> temp = new ArrayList<>();
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).getPlayStatus()) {
                temp.add(games.get(i));
            }
        }
        return temp;
    }
    
    private ArrayList<Game> filterByUnplayed(ArrayList<Game> games) {
        ArrayList<Game> temp = new ArrayList<>();
        for (int i = 0; i < games.size(); i++) {
            if (!games.get(i).getPlayStatus()) {
                temp.add(games.get(i));
            }
        }
        return temp;
    }
    
    // Check if the user has a custom URL or Steam 64 URL and return their profile URL
    private void createURL(String s) {
        if (s.matches("\\d+")) {
            profileURL = "https://steamcommunity.com/profiles/" 
                    + s + "/games/?tab=all";
        } else {
            profileURL = "https://steamcommunity.com/id/" 
                    + s + "/games/?tab=all";
        }
    }
    
    private boolean checkIfNoGames() {
        if (games.isEmpty()) {
            channel.sendMessage("Either your profile is either private, "
                    + "you have provided an incorrect profile name or you own 0 games. "
                    + "Try again.");
            return true;
        }
        return false;
    }
    
    
}
