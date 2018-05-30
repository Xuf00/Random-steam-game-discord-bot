package com.discord.randsteamgamebot.listeners;

import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.GuildLeaveEvent;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.StatusType;
import sx.blah.discord.util.RequestBuffer;

import java.util.stream.Collectors;

public class GuildListener {

    @EventSubscriber
    public void onGuildJoined(GuildCreateEvent event) {
        IDiscordClient client = event.getClient();
        client.changeStreamingPresence(StatusType.ONLINE, "On " + String.valueOf(client.getGuilds().size()) + " servers.", null);
        IPrivateChannel privateChannel = client.getOrCreatePMChannel(client.getApplicationOwner());
        RequestBuffer.request(() -> {
            privateChannel.sendMessage("Bot is currently active in " + String.valueOf(client.getGuilds().size()) + " servers.\n" +
                    "Joined guild with name: "  + "\"" + event.getGuild().getName() + "\"\n" +
                    "Servers user count (incl bots): " + event.getGuild().getTotalMemberCount() + "\n" +
                    "Servers user count (excl. bots): " +
                    event.getGuild().getUsers().stream().filter(user -> !user.isBot() )
                            .collect(Collectors.toList()).size());
        });
    }

    @EventSubscriber
    public void onGuildLeft(GuildLeaveEvent event) {
        IDiscordClient client = event.getClient();
        client.changeStreamingPresence(StatusType.ONLINE, "On " + String.valueOf(client.getGuilds().size()) + " servers.", null);
        IPrivateChannel privateChannel = client.getOrCreatePMChannel(client.getApplicationOwner());
        RequestBuffer.request(() -> {
            privateChannel.sendMessage("Bot is currently active in " + String.valueOf(client.getGuilds().size()) + " servers.\n" +
                    "Left guild with name: " + "\"" + event.getGuild().getName() + "\"\n" +
                    "Servers user count (incl bots): " + event.getGuild().getTotalMemberCount() + "\n" +
                    "Servers user count (excl. bots): " +
                    event.getGuild().getUsers().stream().filter(user -> !user.isBot() )
                            .collect(Collectors.toList()).size());
        });
    }

}
