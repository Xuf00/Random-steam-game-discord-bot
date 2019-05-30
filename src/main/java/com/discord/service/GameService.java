package com.discord.service;

import com.discord.randsteamgamebot.domain.Game;
import com.discord.randsteamgamebot.domain.SteamUser;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.discord.randsteamgamebot.utils.BotUtils.STEAM_API_KEY;

public class GameService {

    private static Logger logger = LoggerFactory.getLogger(GameService.class);

    private GameService() {

    }

    private static class Holder {
        private static final GameService gameService = new GameService();
    }

    public static GameService getInstance() {
        return Holder.gameService;
    }

    // Cache the users games for 10 minutes
    private LoadingCache<String, ArrayList<Game>> gameCache = CacheBuilder.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(10, TimeUnit.MINUTES)
            .build(
                    new CacheLoader<String, ArrayList<Game>>() {
                        public ArrayList<Game> load(String steam64Id) {
                            return fetchGameData(steam64Id);
                        }
                    });

    public ArrayList<Game> getAllUsersGames(String steam64Id) {
        ArrayList<Game> allGames = new ArrayList<>();
        try {
            allGames = gameCache.get(steam64Id);
        } catch (ExecutionException ex) {
            logger.error(ex.getMessage());
        }
        return allGames;
    }

    /**
     * Retrieve all of the users games
     * @param steam64Id The Steam 64 bit ID of the user
     * @return All of the users Steam games
     */
    private ArrayList<Game> fetchGameData(String steam64Id) {
        ArrayList<Game> allUsersSteamGames = new ArrayList<>();
        try {
            HttpResponse<JsonNode> response = Unirest.get("http://api.steampowered.com/IPlayerService/GetOwnedGames/v1/" +
                    "?key=" + STEAM_API_KEY +
                    "&include_appinfo=1" +
                    "&include_played_free_games=1" +
                    "&steamid=" + steam64Id +
                    "&format=json")
                    .asJson();
            allUsersSteamGames = parseJSON(response);
            return allUsersSteamGames;
        } catch (UnirestException ex) {
            logger.error(ex.getMessage());
        }
        return allUsersSteamGames;
    }

    /**
     * Parse the JSON returned by steam, contains user game information
     * @param response The response from the Steam API, in JSON format
     * @return The list of games
     */
    private ArrayList<Game> parseJSON(HttpResponse<JsonNode> response) {
        JSONObject steamGameInfo = response.getBody().getObject().getJSONObject("response");

        if (steamGameInfo == null || steamGameInfo.length() == 0) {
            return null;
        }

        JSONArray allSteamGames = steamGameInfo.getJSONArray("games");
        ArrayList<Game> allGames = new ArrayList<>();

        for (int i = 0; i < allSteamGames.length(); i++) {
            JSONObject gameInfo = allSteamGames.getJSONObject(i);
            String gameName = gameInfo.getString("name");
            String playTime = String.valueOf(gameInfo.getInt("playtime_forever"));
            String appId = String.valueOf(gameInfo.getInt("appid"));
            if (playTime.contentEquals("0")) {
                allGames.add(new Game(appId, gameName));
            }
            else {
                allGames.add(new Game(appId, gameName, Integer.valueOf(playTime)));
            }
        }
        return allGames;
    }

}
