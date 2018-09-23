package com.discord.randsteamgamebot.listeners;

import com.discord.randsteamgamebot.randomizer.GameRandomizer;
import com.discord.randsteamgamebot.domain.SteamUser;
import com.discord.randsteamgamebot.utils.BotUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.discord.randsteamgamebot.utils.BotUtils.commandList;
import static com.discord.randsteamgamebot.utils.BotUtils.editMessage;
import static java.util.stream.Collectors.joining;

public class CommandHandler {

    private static Map<String, Command> commandMap = new HashMap<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(20);

    static {
        commandMap.put("sbhelp", (event, message, args) -> {
            commandList(event.getChannel());
        });

        commandMap.put("rgame", (event, message, args) -> {
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
            steamUser.setDiscordRequester(event.getAuthor());

            GameRandomizer crawler = new GameRandomizer(message, steamUser);

            if (args.size() == 2 && args.get(1).equals("played")) {
                crawler.randPlayedGame();
                return ;
            } else if (args.size() == 2 && args.get(1).equals("unplayed")) {
                crawler.randUnplayedGame();
                return ;
            } else if (args.size() >= 2 && args.get(1).equals("tag")) {
                String tag = args.stream().skip(2).collect(joining(" ")).toLowerCase();
                crawler.randGameByTag(tag);
                return ;
            } else if (args.size() >= 2) {
                String genres = args.stream().skip(1).collect(joining(" ")).toLowerCase();
                crawler.randGameByGenre(genres);
                return ;
            }
            crawler.randGame();
        });

        commandMap.put("mostplayed", (event, message, args) -> {
            String steamName = args.get(0);

            SteamUser steamUser = SteamUser.attemptToCreateSteamUser(steamName);
            if (steamUser == null) {
                event.getChannel().sendMessage("This profile is either private or does not exist, set your privacy to public and try again.");
                return ;
            }
            steamUser.setDiscordRequester(event.getAuthor());
            GameRandomizer crawler = new GameRandomizer(message, steamUser);
            crawler.mostPlayedGames();
        });

        commandMap.put("leastplayed", (event, message, args) -> {
            String steamName = args.get(0);

            SteamUser steamUser = SteamUser.attemptToCreateSteamUser(steamName);
            if (steamUser == null) {
                event.getChannel().sendMessage("This profile is either private or does not exist, set your privacy to public and try again.");
                return ;
            }
            steamUser.setDiscordRequester(event.getAuthor());
            GameRandomizer crawler = new GameRandomizer(message, steamUser);
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

        if (commandMap.containsKey(commandStr)) {
            executorService.submit(() -> {
                Future<IMessage> message = BotUtils.sendInitialMessage(event.getChannel(), event.getAuthor(), argsList.get(0));
                commandMap.get(commandStr).runCommand(event, message, argsList);
            });
        }
    }

}
