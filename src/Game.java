/*
    This class represents a Steam game from the users library.
*/

package com.mycompany.firstdiscordbot;

/**
 *
 * @author Jack
 */
public class Game {
 
    private String gameName;
    private boolean playedOrNot;
    private String hoursPlayed;
    
    public Game(String playedGame, String hoursPlayed) {
        this.gameName = playedGame;
        this.hoursPlayed = hoursPlayed;
        this.playedOrNot = true;
    }
    
    public Game(String game) {
        this.gameName = game;
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
