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
    
    public Game(String gameID, String playedGame, Integer minutesPlayed) {
        this.gameID = gameID;
        this.gameName = playedGame;
        this.minutesPlayed = minutesPlayed;
        playTimeToHoursAndMinutes(minutesPlayed);
        this.playedOrNot = true;
    }
    
    public Game(String gameID, String game) {
        this.gameID = gameID;
        this.gameName = game;
        this.minutesPlayed = 0;
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

    private void playTimeToHoursAndMinutes(Integer minutes_Played) {
        int hoursPlayed = minutes_Played / 60;
        int minutesPlayed = minutes_Played % 60;
        if (hoursPlayed == 0) {
            this.gamePlayedTime = minutesPlayed + " minutes";
        }
        this.gamePlayedTime = hoursPlayed + " hour(s) and " + minutesPlayed + " minutes";
    }
}
