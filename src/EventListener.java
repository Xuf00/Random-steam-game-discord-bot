/* This class is used to listen to message events on discord,
    and respond accordingly to them based upon the contents
    of the message.
*/

package com.mycompany.firstdiscordbot;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
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
    private ArrayList<String> DLCList = new ArrayList<String>();

    // Executes when the bot is ready
    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) throws Exception {
        //driver = new ChromeDriver();
        Scanner s = new Scanner(new File("D:\\Jack\\Documents\\NetBeansProjects\\SeleniumandJSoup\\ListOfDLC.txt"));
        
        while (s.hasNextLine()){
            DLCList.add(s.nextLine());
        }
        s.close();
    }
    
    // Executes when a message is received.
    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) {
        channel = event.getChannel();
        IUser user = event.getAuthor();
        IMessage message = event.getMessage();
        
        String messageUser = user.getName();
        
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
            else if (splitStr[0].equals("clear") && messageUser.equals("Xufoo")) {
                channel.bulkDelete();
            }
            else if (splitStr[0].equals("rgame")) {
                if (argLength < 2) {
                    commandList();
                    return ;
                }
                
                driver = new ChromeDriver();
                profileURL = createURL(splitStr[1]);
                
                if (argLength == 3) {
                    if (splitStr[2].equals("played") || splitStr[2].equals("unplayed")) {
                        getAllPlayedAndUnplayedGames(splitStr[2]);
                        driver.quit();
                        return ;
                    } else {
                        commandList();
                        driver.quit();
                        return ;
                    }
                }
                getAllUsersGames();
            }
            driver.quit();
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
        driver.get(profileURL); 
        Elements games = selectElement(driver, "div#mainContents "
                + "div.gameListRowItem "
                + "div.gameListRowItemName.ellipsis");
        Elements name = selectElement(driver, "span.profile_small_header_name a");

        if (checkIfNoGames(games)) { return ; }
        
        ArrayList<Game> allGames = new ArrayList<>();
        int i = 0;
        for (Element game : games) {
            if (!DLCList.contains(game.text())) {
                allGames.add(new Game(game.text()));
            }
            i++;
        }
        Random r = new Random();
        int rand = r.nextInt(allGames.size());
        RequestBuffer.request(() -> 
                channel.sendMessage(name.text() + " owns " + allGames.size() + " games.\n"
                + "I'd recommend " + name.text() + " plays **" + allGames.get(rand).getGameName() + "**."));
    }
    
    // Retrieve all games and seperate them by whether they've been played or not
    private void getAllPlayedAndUnplayedGames(String playedStatus) {
        driver.get(profileURL);
        
        Elements games = selectElement(driver, "div#mainContents div.gameListRowItem");
        
        //Game[] allGames = new Game[games.size()];
        ArrayList<Game> allGames = new ArrayList<>();
        int i = 0;
        for (Element game : games) {
            if (game.select("h5").text().length() == 0) {
                String getUnplayedGame = game.select(".gameListRowItemName.ellipsis").text();
                allGames.add(new Game(getUnplayedGame));
            } else {
                String getPlayedGame = game.select(".gameListRowItemName.ellipsis").text();
                String getHoursPlayed = game.select("h5").text();
                allGames.add(new Game(getPlayedGame, getHoursPlayed));
            }
            i++;
        }
        
        if (playedStatus.equals("played")) {
            sendPlayedOrUnplayedGame(allGames, true);
        } else {
            sendPlayedOrUnplayedGame(allGames, false);
        }
    }
    
    // Send a random unplayed game back to the user
    private void sendPlayedOrUnplayedGame(ArrayList<Game> allGames, boolean gamePlayed) {
        Elements name = selectElement(driver, "span.profile_small_header_name a");
        int gamePlayedPercent;
        int totalGames = allGames.size();
        String gamePlayedOrNot = "";
        
        if (gamePlayed) {
            gamePlayedPercent = (int) (noOfPlayedOrUnplayedGames(allGames, true) * 100 / totalGames);
            Game randPlayedGame = randPlayedOrUnplayedGame(allGames, true);
            
            gamePlayedOrNot = name.text() + " has played " + noOfPlayedOrUnplayedGames(allGames, true) + " of their games out of "
                    + totalGames + " (" + gamePlayedPercent + "%)" + ". (Includes DLC)\n"
                    + "I recommend that " + name.text() + " plays **" + randPlayedGame.getGameName()
                    + "**.\nThere is currently " + randPlayedGame.getHoursPlayed() + " on this game.";
        } 
        if (!gamePlayed) {
            gamePlayedPercent = (int) (noOfPlayedOrUnplayedGames(allGames, false) * 100 / totalGames);
            Game randUnplayedGame = randPlayedOrUnplayedGame(allGames, false);
        
            gamePlayedOrNot = name.text() + " hasn't played " + noOfPlayedOrUnplayedGames(allGames, false) + " of their games out of "
                    + totalGames + " (" + gamePlayedPercent + "%)" + ". (Includes DLC)\n"
                    + "I recommend that " + name.text() + " plays **" + randUnplayedGame.getGameName() + "**.";
        }
        
        channel.sendMessage(gamePlayedOrNot);
    }
    
    private int noOfPlayedOrUnplayedGames(ArrayList<Game> allGames, boolean played) {
        int temp = 0;
        for (int i = 0; i < allGames.size(); i++) {
            boolean checkStatus = allGames.get(i).getPlayStatus();
            if (!played) {
                if (!checkStatus) {
                    temp++;
                }
            } else if (played) {
                if (checkStatus) {
                    temp++;
                }
            }
        }
        return temp;
    }
    
    private Game randPlayedOrUnplayedGame(ArrayList<Game> games, boolean played) {
        ArrayList<Game> temp = new ArrayList<>();
        Random r = new Random();
        for (int i = 0; i < games.size(); i++) {
            if (played) {
                if (games.get(i).getPlayStatus()) {
                    temp.add(games.get(i));
                }
            }
            else if (!played) {
                if (!games.get(i).getPlayStatus()) {
                    temp.add(games.get(i));
                }
            }
        }
        int rand = r.nextInt(temp.size());
        return temp.get(rand);
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
        System.out.println(temp);
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
