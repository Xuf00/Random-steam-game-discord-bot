package com.discord.randsteamgamebot.domain;

import junit.framework.TestCase;

import java.util.ArrayList;

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

}