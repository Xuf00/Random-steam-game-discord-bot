package com.discord.randsteamgamebot.utils;

import junit.framework.Assert;
import junit.framework.TestCase;
import sx.blah.discord.api.IDiscordClient;

public class BotUtilsTest extends TestCase {

    public void testCreateClient() throws Exception {
        String botToken = "";
        IDiscordClient client = BotUtils.createClient(botToken);
        Assert.assertNotNull("Token is incorrect", client);
    }

}