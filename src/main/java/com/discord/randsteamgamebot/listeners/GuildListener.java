package com.discord.randsteamgamebot.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.GuildLeaveEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionEvent;
import sx.blah.discord.handle.obj.IPrivateChannel;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.StatusType;
import sx.blah.discord.util.RequestBuffer;

import java.util.stream.Collectors;

import static com.discord.randsteamgamebot.utils.BotUtils.DELETE_EMOJI;

public class GuildListener {

    private final Logger logger = LoggerFactory.getLogger(GuildListener.class);

    @EventSubscriber
    public void onEmojiReact(ReactionEvent event) {
        try {
            IDiscordClient client = event.getClient();
            IUser self = client.getOurUser();
            IUser reactor = event.getUser();

            if (event.getUser().equals(self)) {
                return ;
            }

            boolean reactionIsDelete = event.getReaction().getEmoji().equals(DELETE_EMOJI);
            if (reactionIsDelete && event.getMessage().getContent().equals("") && event.getMessage().getAuthor().equals(self)) {
                event.getMessage().delete();
            }
            else if (reactionIsDelete && reactor.equals(event.getMessage().getMentions().get(0))) {
                event.getMessage().delete();
            }
        } catch (Exception ex) {
            logger.info("Failed on a user deleting a message.");
            throw new IllegalStateException(ex);
        }
    }

    @EventSubscriber
    public void onGuildJoined(GuildCreateEvent event) {
        IDiscordClient client = event.getClient();

        if (client.isReady()) {
            client.changeStreamingPresence(StatusType.ONLINE, "On " + String.valueOf(client.getGuilds().size()) + " servers | !sbhelp", null);
            IPrivateChannel privateChannel = client.getOrCreatePMChannel(client.getApplicationOwner());
            RequestBuffer.request(() -> {
                privateChannel.sendMessage("Bot is currently active in " + String.valueOf(client.getGuilds().size()) + " servers.\n" +
                        "Joined guild with name: "  + "\"" + event.getGuild().getName() + "\"\n" +
                        "Servers user count (incl bots): " + event.getGuild().getTotalMemberCount() + "\n" +
                        "Servers user count (excl. bots): " +
                        event.getGuild().getUsers().stream().filter(user -> !user.isBot() )
                                .collect(Collectors.toList()).size());
            });
        } else {
            logger.info("Could not update the guilds information at this time. Joined a new guild.");
        }
    }

    @EventSubscriber
    public void onGuildLeft(GuildLeaveEvent event) {
        IDiscordClient client = event.getClient();
        if (client.isReady()) {
            client.changeStreamingPresence(StatusType.ONLINE, "On " + String.valueOf(client.getGuilds().size()) + " servers | !sbhelp", null);
            IPrivateChannel privateChannel = client.getOrCreatePMChannel(client.getApplicationOwner());
            RequestBuffer.request(() -> {
                privateChannel.sendMessage("Bot is currently active in " + String.valueOf(client.getGuilds().size()) + " servers.\n" +
                        "Left guild with name: " + "\"" + event.getGuild().getName() + "\"\n" +
                        "Servers user count (incl bots): " + event.getGuild().getTotalMemberCount() + "\n" +
                        "Servers user count (excl. bots): " +
                        event.getGuild().getUsers().stream().filter(user -> !user.isBot() )
                                .collect(Collectors.toList()).size());
            });
        } else {
            logger.info("Could not update the guilds information at this time. Left a guild.");
        }
    }

}
