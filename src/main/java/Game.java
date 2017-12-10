/*
    This class represents a Steam game from the users library.
*/

/**
 *
 * @author Jack
 */
public class Game {
 
    private String gameName;
    private boolean played;
    private String hoursPlayed;
    private String gameID;
    
    public Game(String gameID, String playedGame, String hoursPlayed) {
        this.gameID = gameID;
        this.gameName = playedGame;
        this.hoursPlayed = hoursPlayed;
        this.played = true;
    }
    
    public Game(String gameID, String game) {
        this.gameID = gameID;
        this.gameName = game;
        this.played = false;
    }
    
    public String getGameName() {
        return gameName;
    }
    
    public boolean getPlayStatus() {
        return played;
    }
    
    public String getHoursPlayed() {
        return hoursPlayed;
    }

    public String getGameID() { return gameID; }
}
