package com.discord.randsteamgamebot.listeners;

import com.discord.randsteamgamebot.utils.BotUtils;
import discord4j.core.DiscordClient;
import discord4j.core.event.domain.message.MessageCreateEvent;
import discord4j.core.object.entity.Message;

public class CommandHandler {

    /*private static Map<String, Command> commandMap = new HashMap<>();
    private ExecutorService executorService = Executors.newFixedThreadPool(20);

    static {
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
        if (event.getAuthor().isBot()) {
            return ;
        }

        if (argArray.length == 0) {
            return ;
        }

        if (!argArray[0].startsWith(BotUtils.BOT_PREFIX)) {
            return ;
        }

        String commandStr = argArray[0].substring(1);

        List<String> argsList = new ArrayList<>(Arrays.asList(argArray));
        argsList.remove(0);

        if (commandStr.equals("sbhelp")) {
            commandList(event.getChannel());
            return ;
        }

        if (commandMap.containsKey(commandStr)) {
            executorService.submit(() -> {
                Future<IMessage> message = BotUtils.sendInitialMessage(event.getChannel(), event.getAuthor(), argsList.get(0));
                commandMap.get(commandStr).runCommand(event, message, argsList);
            });
        }
    }*/

    public void onMessageReceived(DiscordClient client) {
        client.getEventDispatcher().on(MessageCreateEvent.class)
                .map(MessageCreateEvent::getMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> message.getContent().orElse("").equalsIgnoreCase("!ping"))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage("Pong!"))
                .subscribe();
    }
}
