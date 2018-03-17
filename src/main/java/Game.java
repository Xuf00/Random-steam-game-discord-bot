/*
    This class represents a Steam game from the users library.
*/

/**
 *
 * @author Jack
 */
public class Game {
 
    private String gameName;
    private boolean playedOrNot;
    private String hoursPlayed;
    private String gameID;
    
    public Game(String gameID, String playedGame, String hoursPlayed) {
        this.gameID = gameID;
        this.gameName = playedGame;
        this.hoursPlayed = hoursPlayed;
        this.playedOrNot = true;
    }
    
    public Game(String gameID, String game) {
        this.gameID = gameID;
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

    public String getGameID() { return gameID; }
}
