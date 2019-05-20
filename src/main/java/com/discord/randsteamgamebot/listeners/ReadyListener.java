package com.discord.randsteamgamebot.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadyListener {

    private final Logger logger = LoggerFactory.getLogger(ReadyListener.class);

    /*@EventSubscriber
    public void onReadyEvent(ReadyEvent event) { // This method is called when the ReadyEvent is dispatched
        IDiscordClient client = event.getClient();
        client.changeStreamingPresence(StatusType.ONLINE, "!sbhelp | Newest command is for tags", null);
        EventDispatcher dispatcher = client.getDispatcher();

        dispatcher.registerListener(new CommandHandler());
        dispatcher.registerListener(new GuildListener());

        logger.info("Bot is ready to execute commands.");
    }*/
}
