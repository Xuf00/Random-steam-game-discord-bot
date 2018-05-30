package com.discord.randsteamgamebot;

import com.discord.randsteamgamebot.crawler.SteamCrawler;
import com.discord.randsteamgamebot.listeners.CommandHandler;
import com.discord.randsteamgamebot.listeners.GuildListener;
import com.discord.randsteamgamebot.utils.BotUtils;
import org.apache.log4j.BasicConfigurator;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;

/**
 *
 * @author Jack
 */
public class BotMain {
    
    public static void main(String[] args) {
        BasicConfigurator.configure();
        if (args.length <= 0 && args.length > 2) {
            throw new IllegalStateException("Need to pass in the bot token as an argument.");
        }
        IDiscordClient discordBot = BotUtils.createClient(args[0]);
        try {
            EventDispatcher dispatcher = discordBot.getDispatcher();
            dispatcher.registerListener(new CommandHandler());
            dispatcher.registerListener(new GuildListener());
            discordBot.login();
            CommandHandler.appOwner = discordBot.getApplicationOwner();
            SteamCrawler.steamApiToken = args[1];
        } catch (NullPointerException ex) {
            System.out.println("Error. Ensure the bot token and steam api key is set in the programs arguments.");
            System.out.println("Go to https://discordapp.com/developers/applications/me to create a bot and get a token.");
        }
    }
}
