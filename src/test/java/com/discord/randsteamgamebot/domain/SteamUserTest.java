package com.discord.randsteamgamebot.domain;

import com.discord.randsteamgamebot.utils.BotUtils;
import junit.framework.Assert;
import org.junit.Test;

public class SteamUserTest {

    @Test
    public void createSteamUser() throws Exception {
        BotUtils.STEAM_API_KEY = System.getProperty("steam.api.key");
        SteamUser steamUser = SteamUser.attemptToCreateSteamUser("Xufoo");
        Assert.assertNotNull(steamUser);
    }

}