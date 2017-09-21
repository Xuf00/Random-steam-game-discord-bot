import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.util.DiscordException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Jack
 */
public class BotMain {
    
    public static final IDiscordClient discordBot = createClient("MzQ4MTA5NDUyMDQzNDg1MTg1.DKK6vA.9jpSZmjq7oX5wPsc6_EUNluX1-E", true);
    public static final ArrayList<String> dlcList = getAllDLC();
    
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "D:\\Misc\\chromedriver.exe");
        EventDispatcher dispatcher = discordBot.getDispatcher();
        dispatcher.registerListener(new EventListener());
    }
    
    private static IDiscordClient createClient(String token, boolean login) {
        ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken(token);
        try {
            if (login) {
                return clientBuilder.login();
            } else {
                return clientBuilder.build();
            }
        } catch (DiscordException e) {
            return null;
        }
    }

    private static ArrayList<String> getAllDLC() {
        ArrayList<String> temp = new ArrayList<>();
        try {
            Scanner s = new Scanner(new File("src\\main\\java\\ListOfDLC.txt"));
            while (s.hasNextLine()){
                temp.add(s.nextLine());
            }
            s.close();
        } catch (FileNotFoundException f) {
            System.out.println("File could not be found.");
        }
        return temp;
    }

}
