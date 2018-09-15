package com.discord.randsteamgamebot;

import com.discord.randsteamgamebot.randomizer.GameRandomizer;
import com.discord.randsteamgamebot.listeners.ReadyListener;
import com.discord.randsteamgamebot.utils.BotUtils;
import sx.blah.discord.api.IDiscordClient;

/**
 *
 * @author Jack
 */
public class BotMain {
    
    public static void main(String[] args) {
        BotUtils.STEAM_API_KEY = System.getProperty("steam.api.key");
        String botToken = System.getProperty("bot.token");

        if (BotUtils.STEAM_API_KEY == null || botToken == null) {
            throw new IllegalStateException("Requires the steam.api.key property and bot.token property.");
        }

        IDiscordClient discordBot = BotUtils.createClient(botToken);
        discordBot.getDispatcher().registerListener(new ReadyListener());
        discordBot.login();
    }
}
