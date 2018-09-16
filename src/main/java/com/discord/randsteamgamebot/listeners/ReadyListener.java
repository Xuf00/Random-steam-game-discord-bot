package com.discord.randsteamgamebot.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventDispatcher;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.shard.ReconnectSuccessEvent;
import sx.blah.discord.handle.obj.StatusType;

public class ReadyListener {

    private final Logger logger = LoggerFactory.getLogger(ReadyListener.class);

    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) { // This method is called when the ReadyEvent is dispatched
        IDiscordClient client = event.getClient();
        client.changeStreamingPresence(StatusType.ONLINE, "New command for tags | !sbhelp", null);
        EventDispatcher dispatcher = client.getDispatcher();

        dispatcher.registerListener(new CommandHandler());
        dispatcher.registerListener(new GuildListener());

        logger.info("Bot is ready to execute commands.");
    }
}
