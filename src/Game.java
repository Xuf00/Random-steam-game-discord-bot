/*
    This class represents a Steam game from the users library.
*/

package com.mycompany.firstdiscordbot;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Jack
 */
public class Game {
    
    //private String allGames = new ArrayList();
    private String gameName;
    private boolean playedOrNot;
    private String hoursPlayed;
    
    /*public Games(ArrayList<String> allGames) {
        this.allGames = new ArrayList<>(allGames);
    }*/
    
    public Game(String playedGame, String hoursPlayed) {
        this.gameName = playedGame;
        this.hoursPlayed = hoursPlayed;
        this.playedOrNot = true;
    }
    
    public Game(String unplayedGame) {
        this.gameName = unplayedGame;
        this.playedOrNot = false;
    }
    
    public String getGameName() {
        return gameName;
    }
    
    public boolean getPlayStatus() {
        return playedOrNot;
    }
    
    public String getHoursPlayed() {
        return hoursPlayed;
    }
}
