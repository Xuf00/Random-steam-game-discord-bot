package com.discord.randsteamgamebot.domain;

import com.discord.randsteamgamebot.utils.BotUtils;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import junit.framework.Assert;
import org.json.JSONObject;
import org.junit.Test;

public class BotUserTest {

    @Test
    public void createSteamUser() throws Exception {
        BotUtils.STEAM_API_KEY = System.getProperty("steam.api.key");
        BotUser botUser = BotUser.createSteamUser("76561197984432884");
        Assert.assertNotNull(botUser);
    }

    @Test
    public void getUserProfileName() throws UnirestException {
        HttpResponse<JsonNode> jsonNodeHttpResponse = Unirest.get("http://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/?key=" + System.getProperty("steam.api.key") +
                "&steamids=76561198360844516").asJson();

        JSONObject response = jsonNodeHttpResponse.getBody().getObject().getJSONObject("response");
        System.out.println(response.getJSONArray("players").getJSONObject(0).get("personaname"));
    }

}