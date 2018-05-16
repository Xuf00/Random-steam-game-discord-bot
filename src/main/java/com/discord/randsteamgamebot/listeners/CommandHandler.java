package com.discord.randsteamgamebot.listeners;

import com.discord.randsteamgamebot.crawler.SteamCrawler;
import com.discord.randsteamgamebot.utils.BotUtils;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.api.IShard;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.api.internal.ShardImpl;
import sx.blah.discord.api.internal.json.requests.PrivateChannelCreateRequest;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.Channel;
import sx.blah.discord.handle.impl.obj.Message;
import sx.blah.discord.handle.impl.obj.PrivateChannel;
import sx.blah.discord.handle.impl.obj.User;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.RequestBuffer;

import java.sql.Timestamp;
import java.util.*;

import static com.discord.randsteamgamebot.utils.BotUtils.commandList;

public class CommandHandler {

    private static Map<String, Command> commandMap = new HashMap<>();
    public static IUser appOwner = null;

    static {
        commandMap.put("sbhelp", (event, args) -> {
            commandList(event.getChannel());
        });

        commandMap.put("rgame", (event, args) -> {
            if (args.size() < 1 || args.size() > 2) {
                commandList(event.getChannel());
                return ;
            }

            String steamName = args.get(0);

            SteamCrawler crawler = new SteamCrawler(event.getChannel(), steamName);

            if (args.size() == 2 && args.get(1).equals("played")) {
                crawler.randPlayedGame();
                return ;
            } else if (args.size() == 2 && args.get(1).equals("unplayed")) {
                crawler.randUnplayedGame();
                return ;
            }

            crawler.randGame();
        });

        commandMap.put("mostplayed", (event, args) -> {
            String steamName = args.get(0);

            SteamCrawler crawler = new SteamCrawler(event.getChannel(), steamName);
            crawler.mostPlayedGames();
        });

        commandMap.put("leastplayed", (event, args) -> {
            String steamName = args.get(0);

            SteamCrawler crawler = new SteamCrawler(event.getChannel(), steamName);
            crawler.leastPlayedGames();
        });
    }

    @EventSubscriber
    public void onGuildJoined(GuildCreateEvent event) {
        IDiscordClient client = event.getClient();
        client.changeStreamingPresence(StatusType.ONLINE, "On " + String.valueOf(client.getGuilds().size() + " servers."), null);
        IPrivateChannel privateChannel = client.getOrCreatePMChannel(client.getApplicationOwner());
        RequestBuffer.request(() -> {
            privateChannel.sendMessage("Bot is currently hosted on " + String.valueOf(client.getGuilds().size()) + " servers.\n" +
                    "Joined guild with name: "  + "\"" + event.getGuild().getName() + "\"");
        });
    }

    @EventSubscriber
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] argArray = event.getMessage().getContent().split(" ");

        if (argArray.length == 0) {
            return ;
        }

        if (!argArray[0].startsWith(BotUtils.BOT_PREFIX)) {
            return ;
        }

        String commandStr = argArray[0].substring(1);

        List<String> argsList = new ArrayList<>(Arrays.asList(argArray));
        argsList.remove(0);

        /*if (commandMap.containsKey(commandStr))
            commandMap.get(commandStr).runCommand(event, argsList);*/

    }

}
