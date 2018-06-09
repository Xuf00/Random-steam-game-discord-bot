package com.discord.randsteamgamebot;

import com.discord.randsteamgamebot.crawler.SteamCrawler;
import com.discord.randsteamgamebot.listeners.CommandHandler;
import com.discord.randsteamgamebot.listeners.GuildListener;
import com.discord.randsteamgamebot.listeners.ReadyListener;
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
        if (args.length <= 0 && args.length > 2) {
            throw new IllegalStateException("Need to pass in the bot token as an argument.");
        }
        SteamCrawler.steamApiToken = args[1];

        IDiscordClient discordBot = BotUtils.createClient(args[0]);
        discordBot.getDispatcher().registerListener(new ReadyListener());
        discordBot.login();
        CommandHandler.appOwner = discordBot.getApplicationOwner();
    }
}
