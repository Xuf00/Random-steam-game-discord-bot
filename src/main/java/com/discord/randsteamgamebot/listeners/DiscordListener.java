package com.discord.randsteamgamebot.listeners;

/* This class is used to listen to message events on discord,
    and respond accordingly to them based upon the contents
    of the message.
*/

import com.discord.randsteamgamebot.crawler.SteamCrawler;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

/**
 *
 * @author Jack Johnson
 */
public class DiscordListener {
    
    // Executes when a message is received.
    @EventSubscriber
    public void onMessageEvent(MessageReceivedEvent event) {
        IChannel channel = event.getChannel();
        IMessage message = event.getMessage();
        
        // Split the users message up based on whitespace
        String[] splitStr = message.getContent().substring(1).split(" ");
        int argLength = splitStr.length;

        if (argLength <= 0 || argLength > 3) {
            return ;
        }
        
        if (message.getContent().charAt(0) == '!') {
            String steamName;

            if (splitStr[0].equals("sbcommands")) {
                commandList(channel);
            }
            else if (splitStr[0].equals("rgame")) {
                if (argLength < 2) {
                    commandList(channel);
                    return ;
                }

                steamName = splitStr[1];
                SteamCrawler crawler = new SteamCrawler(channel, steamName);
                
                if (argLength == 3) {
                    String playedOrNot = splitStr[2];
                    switch (playedOrNot) {
                        case "played":
                            crawler.randPlayedGame();
                            return ;
                        case "unplayed":
                            crawler.randUnplayedGame();
                            return ;
                        default:
                            commandList(channel);
                            return ;
                    }
                }
                crawler.randGame();
            }
            else if (splitStr[0].equals("mostplayed")) {
                if (argLength < 2 || argLength > 2) {
                    commandList(channel);
                    return ;
                }

                steamName = splitStr[1];
                SteamCrawler crawler = new SteamCrawler(channel, steamName);
                crawler.mostPlayedGames();
            }
        }
    }
 
    // Build and display the list of commands to the user
    private void commandList(IChannel channel) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withColor(41, 128, 185);
        builder.appendDescription("Grabs all of a users games on Steam and selects a random game. "
                + "User can filter whether or not they want a random game they haven't played before.");
        builder.appendField("Commands   ", "!rgame [name/17 digit ID]"
                + "           " + "\n!rgame [name/17 digit ID] [played/unplayed]"
                + "           " + "\n!mostplayed [name/17 digit ID]", true);
        builder.appendField("Example", "!rgame Xufoo\n!rgame 76561198054740594 played\n!mostplayed Xufoo", true);
        RequestBuffer.request(() -> channel.sendMessage(builder.build()));
    }
}
