import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.util.DiscordException;

import java.util.ArrayList;

/**
 *
 * @author Jack
 */
public class BotMain {
    
    public static final IDiscordClient discordBot = createClient(BotUtils.loadBotToken("config/botconfig.txt"), true);

    // Store DLC list early on to speed bot up slightly
    public static final ArrayList<String> dlcList = BotUtils.loadAllDlc("src/main/java/ListOfDLC.txt");
    
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "chromedriver/chromedriver.exe");
        try {
            EventDispatcher dispatcher = discordBot.getDispatcher();
            dispatcher.registerListener(new DiscordListener());
        } catch (NullPointerException ex) {
            System.out.println("Error. Ensure the bot token is set in the botconfig file.");
            System.out.println("Go to https://discordapp.com/developers/applications/me to create a bot and get a token.");
        }
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

}
