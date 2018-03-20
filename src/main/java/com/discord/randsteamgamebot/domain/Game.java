package com.discord.randsteamgamebot.domain;
/**
 * Represents a steam game from the users library
 */

/**
 *
 * @author Jack
 */
public class Game {
 
    private String gameName;
    private boolean playedOrNot;
    private String gameID;
    private Integer minutesPlayed;
    private String gamePlayedTime;
    private String installLink;
    
    public Game(String gameID, String playedGame, Integer minutesPlayed) {
        this.gameID = gameID;
        this.gameName = playedGame;
        this.minutesPlayed = minutesPlayed;
        this.installLink = "steam://run/" + gameID;
        playTimeToHoursAndMinutes(minutesPlayed);
        this.playedOrNot = true;
    }
    
    public Game(String gameID, String game) {
        this.gameID = gameID;
        this.gameName = game;
        this.minutesPlayed = 0;
        this.installLink = "steam://run/" + gameID;
        playTimeToHoursAndMinutes(minutesPlayed);
        this.playedOrNot = false;
    }


    
    public String getGameName() {
        return gameName;
    }
    
    public boolean getPlayStatus() {
        return playedOrNot;
    }
    
    public Integer getMinutesPlayed() {
        return minutesPlayed;
    }

    public String getGameID() { return gameID; }

    public String getGamePlayedTime() {
        return gamePlayedTime;
    }

    public String getInstallLink() {
        return installLink;
    }

    private void playTimeToHoursAndMinutes(Integer minutes_Played) {
        int hoursPlayed = minutes_Played / 60;
        int minutesPlayed = minutes_Played % 60;
        if (hoursPlayed == 0) {
            this.gamePlayedTime = minutesPlayed + " minute(s)";
            return ;
        }
        this.gamePlayedTime = hoursPlayed + " hour(s) and " + minutesPlayed + " minutes";
    }
}
