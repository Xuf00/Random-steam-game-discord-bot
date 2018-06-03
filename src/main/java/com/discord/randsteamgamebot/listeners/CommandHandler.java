package com.discord.randsteamgamebot.listeners;

import com.discord.randsteamgamebot.crawler.SteamCrawler;
import com.discord.randsteamgamebot.domain.SteamUser;
import com.discord.randsteamgamebot.utils.BotUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;

import java.util.*;

import static com.discord.randsteamgamebot.utils.BotUtils.commandList;
import static java.util.stream.Collectors.joining;

public class CommandHandler {

    private static Map<String, Command> commandMap = new HashMap<>();
    public static IUser appOwner = null;


    static {
        commandMap.put("sbhelp", (event, args) -> {
            commandList(event.getChannel());
        });

        commandMap.put("rgame", (event, args) -> {
            if (args.size() < 1) {
                commandList(event.getChannel());
                return ;
            }

            String steamName = args.get(0);
            SteamUser steamUser = SteamUser.attemptToCreateSteamUser(steamName);
            if (steamUser == null) {
                event.getChannel().sendMessage("This profile is either private or does not exist, set your privacy to public and try again.");
                return ;
            }

            SteamCrawler crawler = new SteamCrawler(event.getChannel(), steamUser);

            if (args.size() == 2 && args.get(1).equals("played")) {
                crawler.randPlayedGame();
                return ;
            } else if (args.size() == 2 && args.get(1).equals("unplayed")) {
                crawler.randUnplayedGame();
                return ;
            } else if (args.size() >= 2) {
                //String genres = args.stream().skip(1).collect(joining(" "));
                String genres = args.get(1);
                crawler.randGameByGenre(genres);
                return ;
            }
            crawler.randGame();
        });

        commandMap.put("mostplayed", (event, args) -> {
            String steamName = args.get(0);

            SteamUser steamUser = SteamUser.attemptToCreateSteamUser(steamName);
            if (steamUser == null) {
                event.getChannel().sendMessage("This profile is either private or does not exist, set your privacy to public and try again.");
                return ;
            }
            SteamCrawler crawler = new SteamCrawler(event.getChannel(), steamUser);
            crawler.mostPlayedGames();
        });

        commandMap.put("leastplayed", (event, args) -> {
            String steamName = args.get(0);

            SteamUser steamUser = SteamUser.attemptToCreateSteamUser(steamName);
            if (steamUser == null) {
                event.getChannel().sendMessage("This profile is either private or does not exist, set your privacy to public and try again.");
                return ;
            }
            SteamCrawler crawler = new SteamCrawler(event.getChannel(), steamUser);
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

        if (event.getAuthor().isBot()) {
            return ;
        }

        String commandStr = argArray[0].substring(1);

        List<String> argsList = new ArrayList<>(Arrays.asList(argArray));
        argsList.remove(0);

        if (commandMap.containsKey(commandStr))
            commandMap.get(commandStr).runCommand(event, argsList);

    }

}
