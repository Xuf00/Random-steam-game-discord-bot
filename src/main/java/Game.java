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
    
    public Game(String gameID, String playedGame, Integer minutesPlayed) {
        this.gameID = gameID;
        this.gameName = playedGame;
        this.minutesPlayed = minutesPlayed;
        this.playedOrNot = true;
    }
    
    public Game(String gameID, String game) {
        this.gameID = gameID;
        this.gameName = game;
        this.minutesPlayed = 0;
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
}
