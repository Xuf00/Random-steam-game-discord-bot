/*
    This class is used to crawl the Steam page of a user
    to retrieve their games and Steam display name, provides
    methods to return a random game
*/

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.RequestBuffer;

/**
 *
 * @author Jack
 */
public class SteamCrawler {

    private IChannel channel;
    private Connection steamConnection;
    private int totalGamesVal;
    private String steamName;
    private String steam64Id;
    private static final String steamApiToken = "";
    
    public SteamCrawler(IChannel channel, String profileID) {
        this.channel = channel;
        steamConnection = Jsoup.connect("http://steamcommunity.com/");
        initializeProfile(profileID);
    }

    /**
     * Initialize the crawler with the users profile ID
     */
    private void initializeProfile(String profileID) {
        steam64Id = createSteam64Id(profileID);

        String title = "";
        try {
            title = Jsoup.connect("http://steamcommunity.com/profiles/" + steam64Id).get().title();
        } catch (IOException ex) {
            throw new IllegalStateException();
        }

        if (title.isEmpty()) { return ; }

        steamName = title.substring(19, title.length());
    }

    /**
     * Choose a random game for the user to play.
     */
    public void randGame() {
        ArrayList<Game> allGames = getAllGames(steam64Id);
        Game randGame = chooseRandGame(allGames);
        String installLink = "steam://run/" + randGame.getGameID();

        sendMessage(steamName + "owns " + allGames.size() + " games.\n"
                    + "I'd recommend " + steamName + " plays **" + randGame.getGameName() + "**.\n" +
                    "Install or play the game: " + installLink);
    }

    /**
     * Choose and return a random game for the user that they have already played before,
     * will also display the amount of games that they have played already along with
     * the percentage of games they have played
     */
    public void randPlayedGame() {

        ArrayList<Game> allGames = getAllGames(steam64Id);
        if (noGamesOwned(allGames)) { return ; }
        ArrayList<Game> playedGames = filterGames(allGames, true);

        if (noGamesOwned(playedGames)) {
            sendMessage("You haven't played any games yet.");
        }

        int playedGameVal = playedGames.size();
        int gamePlayedPercent = (playedGameVal * 100 / totalGamesVal);
        
        Game randPlayedGame = chooseRandGame(playedGames);
        String installLink = "steam://run/" + randPlayedGame.getGameID();

        sendMessage(steamName + " has played " + playedGameVal + " of their games out of "
                    + totalGamesVal + " (" + gamePlayedPercent + "%)" + ".\n"
                    + "I recommend that " + steamName + " plays **" + randPlayedGame.getGameName()
                    + "**.\nThere is currently " + randPlayedGame.getHoursPlayed() + " played on this game.\n"
                    + "Install or play the game: " + installLink);
    }

    /**
     * Choose and return a random game for the user that they have not yet played,
     * will also display the amount of games that they haven't played along with
     * the percentage of games they have not yet played
     */
    public void randUnplayedGame() {

        ArrayList<Game> allGames = getAllGames(steam64Id);
        if (noGamesOwned(allGames)) { return ; }
        ArrayList<Game> unplayedGames = filterGames(allGames, false);

        if (noGamesOwned(unplayedGames)) {
            sendMessage("You've played all of your games already.");
            return ;
        }

        int unplayedGameVal = unplayedGames.size();
        int gamePlayedPercent = (unplayedGameVal * 100 / totalGamesVal);
        
        Game randUnplayedGame = chooseRandGame(unplayedGames);
        String installLink = "steam://run/" + randUnplayedGame.getGameID();

        sendMessage(steamName + " hasn't played " + unplayedGameVal + " of their games out of "
                + totalGamesVal + " (" + gamePlayedPercent + "%)" + ".\n"
                + "I recommend that " + steamName + " plays **" + randUnplayedGame.getGameName() + "**.\n"
                + "Install or play the game: " + installLink);
    }

    /**
     * Return the users most played games on Steam,
     * nicely formatted and then output to them
     */
    public void mostPlayedGames() {

        ArrayList<Game> allGames = getAllGames(steam64Id);
        if (noGamesOwned(allGames)) { return ; }
        ArrayList<Game> playedGames = filterGames(allGames, true);

        if(noGamesOwned(playedGames)) {
            sendMessage("You haven't played any games yet.");
            return;
        }
        if (playedGames.size() < 5) {
            sendMessage("You need to have played five games.");
            return;
        }

        EmbedBuilder builder = new EmbedBuilder();
        builder.withColor(41, 128, 185);
        builder.withDesc("The top five most played games on Steam for this user.");
        builder.withTitle(steamName + " most played games\n");

        builder.appendField("Game Name",
                playedGames.get(0).getGameName() + "\n" +
                playedGames.get(1).getGameName() + "\n" +
                playedGames.get(2).getGameName() + "\n" +
                playedGames.get(3).getGameName() + "\n" +
                playedGames.get(4).getGameName(), true);

        builder.appendField( "Hours Played",
                playedGames.get(0).getHoursPlayed() + "\n" +
                        playedGames.get(1).getHoursPlayed() + "\n" +
                        playedGames.get(2).getHoursPlayed() + "\n" +
                        playedGames.get(3).getHoursPlayed() + "\n" +
                        playedGames.get(4).getHoursPlayed(), true);

        RequestBuffer.request(() ->
                channel.sendMessage(builder.build()));
    }

    /**
     * Helpful method for choosing a random game
     * @param games The games in which to choose a random game from
     * @return A random game
     */
    private Game chooseRandGame(ArrayList<Game> games) {
        Random r = new Random();
        int rand = r.nextInt(games.size());
        return games.get(rand);
    }

    /**
     * Retrieve all of the users games
     * @param steam64Id The Steam 64 bit ID of the user
     * @return All of the users Steam games
     */
    private ArrayList<Game> getAllGames(String steam64Id) {
        ArrayList<Game> allGames = new ArrayList<>();
        HttpResponse<JsonNode> request;
        try {
            request = Unirest.get("http://api.steampowered.com/IPlayerService/GetOwnedGames/v1/" +
                    "?key=" + steamApiToken +
                    "&include_appinfo=1" +
                    "&include_played_free_games=" +
                    "&steamid=" + steam64Id +
                    "&format=json")
                    .asJson();
        } catch (UnirestException ex) {
            throw new IllegalStateException();
        }

        JSONObject steamGameInfo = request.getBody().getObject().getJSONObject("response");
        JSONArray allSteamGames = steamGameInfo.getJSONArray("games");

        for (int i = 0; i < allSteamGames.length(); i++) {
            JSONObject gameInfo = allSteamGames.getJSONObject(i);
            String gameName = gameInfo.getString("name");
            String playTime = String.valueOf(gameInfo.getInt("playtime_forever"));
            String appId = String.valueOf(gameInfo.getInt("appid"));
            if (playTime.contentEquals("0")) {
                allGames.add(new Game(appId, gameName));
            }
            else {
                String timePlayed = convertToHoursAndMinutes(playTime);
                allGames.add(new Game(appId, gameName, timePlayed));
            }
        }

        totalGamesVal = steamGameInfo.getInt("game_count");
        return allGames;
    }

    /**
     * Filter games by whether they've been played or not
     * @param games The games to filter
     * @param played Whether or not the game has been played
     * @return
     */
    private ArrayList<Game> filterGames(ArrayList<Game> games, boolean played) {
        ArrayList<Game> temp = new ArrayList<>();
        for (Game game : games) {
            if (game.getPlayStatus() == played) {
                temp.add(game);
            }
        }
        return temp;
    }

    /**
     * Send a message on the discord channel
     * @param message Message to send
     */
    private void sendMessage(String message) {
        RequestBuffer.request(() ->
            channel.sendMessage(message));
    }

    /**
     * Create the URL required based on the users ID
     * @param profileID The users steam profile ID/name
     */
    private String createSteam64Id(String profileID) {
        String profileURL;
        if (profileID.matches("\\d+")) {
            return profileID;
        }
        profileURL = "https://steamcommunity.com/id/"
                + profileID + "/?xml=1";
        Document doc;
        Elements steamId64 = null;
        try {
            doc = steamConnection.url(profileURL).get();
            steamId64 = doc.select("steamID64");
        } catch (IOException | NullPointerException ex) {
            ex.printStackTrace();
        }
        return steamId64.text();
    }

    /**
     * Convert minutes to hours and minutes
     * @param minutes_Played The minutes played of a game
     * @return A formatted string of the hours and minutes played
     */
    private String convertToHoursAndMinutes(String minutes_Played) {
        int minsPlayed = Integer.valueOf(minutes_Played);
        int hoursPlayed = minsPlayed / 60;
        int minutesPlayed = minsPlayed % 60;
        if (hoursPlayed == 0) {
            return minutesPlayed + " minutes.";
        }
        return hoursPlayed + " hour(s) and " + minutesPlayed + " minutes.";
    }

    /**
     * Check if the user actually owns any games
     * @param games The games to pass in
     * @return Whether or not at least one game exists
     */
    private boolean noGamesOwned(ArrayList<Game> games) {
        if (games.isEmpty()) {
            sendMessage("No games owned or private profile.");
            return true;
        }
        return false;
    }
}
