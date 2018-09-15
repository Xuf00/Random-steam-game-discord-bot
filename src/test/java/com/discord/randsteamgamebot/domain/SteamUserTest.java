package com.discord.randsteamgamebot.domain;

import com.discord.randsteamgamebot.utils.BotUtils;
import junit.framework.Assert;
import junit.framework.TestCase;

public class SteamUserTest extends TestCase {

    public void testAttemptToCreateSteamUser() throws Exception {
        BotUtils.STEAM_API_KEY = System.getProperty("steam.api.key");
        SteamUser steamUser = SteamUser.attemptToCreateSteamUser("Xufoo");
        Assert.assertNotNull(steamUser);
    }

}