package com.discord.randsteamgamebot.command.botinfo;

import com.discord.randsteamgamebot.command.BotCommand;
import com.discord.randsteamgamebot.domain.BotCommandArgs;
import com.discord.randsteamgamebot.utils.BotUtils;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.RequestBuffer;

import java.util.List;

public class ServerCountCommand implements BotCommand {

    @Override
    public boolean matches(List<String> arguments) {
        return arguments.size() == 1 && arguments.get(0).equals("servers");
    }

    @Override
    public void execute(BotCommandArgs commandArgs) {
        MessageReceivedEvent event = commandArgs.getEvent();
        IDiscordClient client = event.getClient();
        IChannel channel = event.getChannel();

        try {
            int serverCount = client.getGuilds().size();
            RequestBuffer.request(() -> { channel.sendMessage("I am currently in " + serverCount + " servers."); });
        } catch (Exception ex) {
            throw new IllegalStateException(ex);
        }
    }
}
