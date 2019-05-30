package com.discord.randsteamgamebot.domain;

import com.discord.randsteamgamebot.utils.BotUtils;
import com.discord.service.GameService;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class GameUnitTest {

    GameService gameService = GameService.getInstance();

    @BeforeClass
    public static void beforeClass() {
        BotUtils.STEAM_API_KEY = System.getProperty("steam.api.key");
    }

    @Test
    public void getAllGamesMemoryUsageTest() throws Exception {
        Runtime runtime = Runtime.getRuntime();
        long memoryUsedBefore = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used memory before: " + memoryUsedBefore);
        gameService.getAllUsersGames("76561198080291846");
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory increased: " + (usedMemoryAfter-memoryUsedBefore));
    }

    @Test
    public void chooseRandGame() {
        List<Game> games = new ArrayList<>();

        Game game = new Game("2546", "Test game");
        Game game1 = new Game("456565", "Another game");

        games.add(game);
        games.add(game1);

        Game game2 = Game.chooseRandGame(games);
        assertNotNull(game2);
    }

    @Test
    public void getAllGames() throws Exception {
        ArrayList<Game> allGames = gameService.getAllUsersGames("76561198080291846");
        assertNotNull(allGames);
    }

    @Test
    public void filterGames() {
        Game game = new Game("1234556", "Test Game");
        Game game2 = new Game("145456566", "Test Played Game", 700);
        List<Game> games = new ArrayList<>();
        games.add(game);
        games.add(game2);

        ArrayList<Game> filteredGames = Game.filterGames(games, true);

        assertEquals("Retrieved played game.", "Test Played Game", filteredGames.get(0).getGameName());
    }
}