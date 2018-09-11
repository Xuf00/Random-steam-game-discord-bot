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
        if (args.length <= 0 && args.length > 2) {
            throw new IllegalStateException("Need to pass in the bot token as an argument.");
        }
        GameRandomizer.STEAM_API_KEY = args[1];

        IDiscordClient discordBot = BotUtils.createClient(args[0]);
        discordBot.getDispatcher().registerListener(new ReadyListener());
        discordBot.login();
    }
}
