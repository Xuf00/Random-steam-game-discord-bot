package com.discord.randsteamgamebot.utils;

import java.util.*;

/**
 * This class holds information about the currently available genres on Steam
 */
public final class GameGenres {
    public static final Map<String, String> gameGenreMap = Collections.unmodifiableMap(
            new HashMap<String, String>() {{
                put("early access", "Early access");
                put("videos", "Videos");
                put("action", "Action");
                put("strategy", "Strategy");
                put("indie", "Indie");
                put("rpg", "RPG");
                put("video production", "Video production");
                put("racing", "Racing");
                put("sports", "Sports");
                put("utilities", "Utilities");
                put("design & illustration", "Design & illustration");
                put("photo editing", "Photo editing");
                put("software training", "Software training");
                put("left early access", "Left early access");
                put("free to play", "Free to play");
                put("adventure", "Adventure");
                put("simulation", "Simulation");
                put("animation & modeling", "Animation & modeling");
                put("casual", "Casual");
                put("massively multiplayer", "Massively multiplayer");
                put("audio production", "Audio production");
                put("web publishing", "Web publishing");
                put("education", "Education");
                put("accounting", "Accounting");
            }}
    );

}
