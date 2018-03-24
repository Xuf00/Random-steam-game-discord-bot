package com.discord.randsteamgamebot.listeners;

import com.discord.randsteamgamebot.crawler.SteamCrawler;
import com.discord.randsteamgamebot.utils.BotUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;

import java.util.*;

import static com.discord.randsteamgamebot.utils.BotUtils.commandList;

public class CommandHandler {

    private static Map<String, Command> commandMap = new HashMap<>();

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

        if (commandMap.containsKey(commandStr))
            commandMap.get(commandStr).runCommand(event, argsList);

    }

}
