package com.discord.randsteamgamebot.utils;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This class holds information about the currently available genres on Steam
 */
public final class GameGenres {
    public static final Map<String, String> gameGenreMap = new HashMap<>(
            new HashMap<String, String>() {{
                put("earlyaccess", "Early access");
                put("videos", "Videos");
                put("action", "Action");
                put("strategy", "Strategy");
                put("indie", "Indie");
                put("rpg", "RPG");
                put("racing", "Racing");
                put("sport", "Sports");
                put("sports", "Sports");
                put("free", "Free to play");
                put("f2p", "Free to play");
                put("freetoplay", "Free to play");
                put("adventure", "Adventure");
                put("simulation", "Simulation");
                put("casual", "Casual");
                put("multiplayer", "Massively multiplayer");
                put("massivelymultiplayer", "Massively multiplayer");
                put("education", "Education");

                // These can also be used but they're not games so they're not listed on the helper message...
                put("videoproduction", "Video production");
                put("softwaretraining", "Software training");
                put("animation&modeling", "Animation & modeling");
                put("audioproduction", "Audio production");
                put("accounting", "Accounting");
                put("webpublishing", "Web publishing");
                put("utilities", "Utilities");
                put("design&illustration", "Design & illustration");
                put("photoediting", "Photo editing");
            }}
    );

}
