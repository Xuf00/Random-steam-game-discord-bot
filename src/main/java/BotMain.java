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
    
    public static void main(String[] args) {
        if (args.length < 0 && args.length > 1) {
            throw new IllegalStateException("Need to pass in the bot token as an argument.");
        }
        IDiscordClient discordBot = BotUtils.createClient(args[0], true);
        try {
            EventDispatcher dispatcher = discordBot.getDispatcher();
            dispatcher.registerListener(new DiscordListener());
        } catch (NullPointerException ex) {
            System.out.println("Error. Ensure the bot token is set in the programs arguments.");
            System.out.println("Go to https://discordapp.com/developers/applications/me to create a bot and get a token.");
        }
    }
}
