package com.discord.randsteamgamebot.domain;

import junit.framework.Assert;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public class GameTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void testGetAllGames() throws Exception {
        Runtime runtime = Runtime.getRuntime();
        long memoryUsedBefore = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Used memory before: " + memoryUsedBefore);
        ArrayList<Game> allGames = Game.getAllGames("76561198080291846");
        long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory increased: " + (usedMemoryAfter-memoryUsedBefore));
        Thread.sleep(10000);
        long memoryNow = runtime.totalMemory() - runtime.freeMemory();
        System.out.println("Memory now: " + memoryNow);
    }

    public void testChooseRandGame() {
        List<Game> games = new ArrayList<>();

        Game game = new Game("2546", "Test game");
        Game game1 = new Game("456565", "Another game");

        games.add(game);
        games.add(game1);

        Game game2 = Game.chooseRandGame(games);
        Assert.assertNotNull(game2);
    }

}