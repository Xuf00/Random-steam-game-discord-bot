package com.discord.randsteamgamebot;

import com.discord.randsteamgamebot.listeners.CommandHandler;
import com.discord.randsteamgamebot.utils.BotUtils;
import discord4j.core.DiscordClient;
import discord4j.core.DiscordClientBuilder;
import discord4j.core.object.presence.Activity;
import discord4j.core.object.presence.Presence;

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

        DiscordClient client = new DiscordClientBuilder(botToken)
                .setInitialPresence(Presence.idle(Activity.playing("Starting up...")))
                .build();

        CommandHandler commandHandler = new CommandHandler();
        commandHandler.onMessageReceived(client);

        client.login().block();
    }
}
