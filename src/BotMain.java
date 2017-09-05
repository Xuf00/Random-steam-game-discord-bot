package com.mycompany.firstdiscordbot;

import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.util.DiscordException;

/**
 *
 * @author Jack
 */
public class BotMain {
    
    public static final IDiscordClient discordBot = createClient("Bot Key here", true);
    
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "D:\\Jack\\Documents"
                + "\\Programming\\Java\\ChromeDriver\\chromedriver.exe");
        EventDispatcher dispatcher = discordBot.getDispatcher();
        dispatcher.registerListener(new EventListener());
    }
    
    public static IDiscordClient createClient(String token, boolean login) {
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
