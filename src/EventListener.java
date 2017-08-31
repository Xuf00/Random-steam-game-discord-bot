/* This class is used to listen to message events on discord,
    and respond accordingly to them based upon the contents
    of the message.
*/

package com.mycompany.firstdiscordbot;

import java.util.ArrayList;
import java.util.Random;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

/**
 *
 * @author Jack Johnson
 */
public class EventListener {
    
    private WebDriver driver;
    private IChannel channel;
    private String profileURL;
    static boolean pageStillLoading = false;

    // Executes when the bot is ready
    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) {
        driver = new ChromeDriver();
    }
    
    // Executes when a message is received.
    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        channel = event.getChannel();
        IUser user = event.getAuthor();
        IMessage message = event.getMessage();
        
        // Split the users message up based on whitespace
        String[] splitStr = message.getContent().substring(1).split(" ");
        int argLength = splitStr.length;
        
        if (argLength <= 0 || argLength > 3) {
            return ;
        }
        
        if (message.getContent().charAt(0) == '!') {
            if (splitStr[0].equals("commands")) {
                commandList();
            }
            else if (splitStr[0].equals("clear") && user.getName().equals("Xufoo")) {
                channel.bulkDelete();
            }
            else if (splitStr[0].equals("rgame")) {
                if (argLength < 2) {
                    commandList();
                    return ;
                }
                
                if (pageStillLoading) {
                    driver = new ChromeDriver();
                }
                profileURL = createURL(splitStr[1]);
                
                if (argLength == 3) {
                    if (splitStr[2].equals("played") || splitStr[2].equals("unplayed")) {
                        getAllPlayedAndUnplayedGames(splitStr[2]);
                        return ;
                    } else {
                        commandList();
                        return ;
                    }
                }
                
                getAllUsersGames();
                
            }
        }
    }
 
    // Build and display the list of commands to the user
    private void commandList() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withColor(41, 128, 185);
        builder.appendDescription("Grabs all of a users games on Steam and selects a random game. "
                + "User can filter whether or not they want a random game they haven't played before.");
        builder.appendField("Commands   ", "!rgame [name/17 digit ID]" 
                + "           " + "\n!rgame [name/17 digit ID] [played/unplayed]", true);
        builder.appendField("Example", "!rgame Xufoo\n!rgame 76561198054740594 played", true);
        RequestBuffer.request(() -> channel.sendMessage(builder.build()));
    }
    
    // Navigate to the users games page and retrieve all of their games, return a random one
    private void getAllUsersGames() {
        pageStillLoading = true;
        driver.get(profileURL);
        pageStillLoading = false;
                
        Elements games = selectElement(driver, "div#mainContents "
                + "div.gameListRowItem "
                + "div.gameListRowItemName.ellipsis");
        Elements name = selectElement(driver, "span.profile_small_header_name a");

        if (checkIfNoGames(games)) { return ; }

        ArrayList<String> gamesOwned = new ArrayList<>();
        games.forEach((game) -> {
            gamesOwned.add(game.text());
        });

        Random r = new Random();
        int rand = r.nextInt(gamesOwned.size());
        channel.sendMessage(name.text() + " owns " + gamesOwned.size() + " games. (Including DLC)\n"
                + "I'd recommend " + name.text() + " plays **" + gamesOwned.get(rand) + "**.");
    }
    
    // Retrieve all games and seperate them by whether they've been played or not
    private void getAllPlayedAndUnplayedGames(String playedStatus) {
        Elements games = selectElement(driver, "div#mainContents div.gameListRowItem");
        
        if (checkIfNoGames(games)) { return ; }
        
        ArrayList<String> playedGames = new ArrayList<>();
        ArrayList<String> unplayedGames = new ArrayList<>();
        ArrayList<String> hoursPlayed = new ArrayList<>();
        
        games.forEach((game) -> {
            if (game.select("h5").text().length() == 0) {
                unplayedGames.add(game.select(".gameListRowItemName.ellipsis").text());
            } else {
                playedGames.add(game.select(".gameListRowItemName.ellipsis").text());
                hoursPlayed.add(game.select("h5").text());
            }
        });
        
        playedStatus(playedStatus, playedGames, unplayedGames, hoursPlayed);
    }
    
    // Check if the user wants a played game or unplayed game and calls a method to send them the data
    private void playedStatus(String playedStatus, ArrayList<String> playedGames, ArrayList<String> unplayedGames, ArrayList<String> hoursPlayed) {
        Elements name = selectElement(driver, "span.profile_small_header_name a");
        int totalGames = playedGames.size() + unplayedGames.size();
        int gamePlayedPercentage;
        
        if (playedStatus.equals("played")) {
            gamePlayedPercentage = (int) (playedGames.size() * 100 / totalGames);
            sendPlayedOrUnplayedGame(name, playedGames, gamePlayedPercentage, totalGames, hoursPlayed);
        } else {
            gamePlayedPercentage = (int) (unplayedGames.size() * 100 / totalGames);
            sendPlayedOrUnplayedGame(name, unplayedGames, gamePlayedPercentage, totalGames);
        }
    }
    
    // Send a random unplayed game back to the user
    private void sendPlayedOrUnplayedGame(Elements name, ArrayList<String> games, int gamePlayedPercentage, int totalGames) {
        Random r = new Random();
        int rand = r.nextInt(games.size());
        String gamePlayedOrNot = name.text() + " hasn't played " + games.size() + " of their games out of "
                    + totalGames + " (" + gamePlayedPercentage + "%)" + ". (Includes DLC)\n"
                    + "I recommend that " + name.text() + " plays **" + games.get(rand) + "**.";
        channel.sendMessage(gamePlayedOrNot);
    }
    
    // Overloaded method, send a random played game back to the user
    private void sendPlayedOrUnplayedGame(Elements name, ArrayList<String> games, int gamePlayedPercentage,int totalGames, ArrayList<String> hoursPlayed) {
        Random r = new Random();
        int rand = r.nextInt(games.size());
        String gamePlayedOrNot = name.text() + " has played " + games.size() + " of their games out of "
                    + totalGames + " (" + gamePlayedPercentage + "%)" + ". (Includes DLC)\n"
                    + "I recommend that " + name.text() + " plays **" + games.get(rand)
                    + "**.\nThere is currently " + hoursPlayed.get(rand) + " on this game.";
        channel.sendMessage(gamePlayedOrNot);
    }
    
    // Select the elements required on the webpage to get the games and other information
    private Elements selectElement(WebDriver wd, String cssQuery) {
        Document doc = Jsoup.parse(wd.getPageSource());
        return doc.select(cssQuery);
    }
    
    // Check if the user has a custom URL or Steam 64 URL and return their profile URL
    private String createURL(String s) {
        String temp;
        if (s.matches("\\d+")) {
            temp = "https://steamcommunity.com/profiles/" 
                    + s + "/games/?tab=all";
        } else {
            temp = "https://steamcommunity.com/id/" 
                    + s + "/games/?tab=all";
        }
        return temp;
    }
    
    private boolean checkIfNoGames(Elements games) {
        if (games.isEmpty()) {
            channel.sendMessage("Your profile is either private or "
                    + "you have provided an incorrect profile name. Try again.");
            return true;
        }
        return false;
    }
}
