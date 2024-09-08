package com.bara.playerStats;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;

public class PlayerStatsExpansion extends PlaceholderExpansion {

    private final DataStorage dataStorage;

    public PlayerStatsExpansion(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @Override
    public String onPlaceholderRequest(Player player, String identifier) {
        // Ensure the player is valid
        if (player == null) {
            return null;
        }

        // Handle the %playerstats_sky_kills% placeholder
        if (identifier.equals("sky_kills")) {
            int kills = dataStorage.getKills(player);
            return String.valueOf(kills);
        }

        // Handle the %playerstats_sky_deaths% placeholder
        if (identifier.equals("sky_deaths")) {
            int deaths = dataStorage.getDeaths(player);
            return String.valueOf(deaths);
        }

        // Handle the %playerstats_sky_kdr% placeholder
        if (identifier.equals("sky_kdr")) {
            double kdr = dataStorage.getKDR(player);
            return String.format("%.2f", kdr); // Format KDR to 2 decimal places
        }

        // Handle the %playerstats_sky_killstreak% placeholder
        if (identifier.equals("sky_killstreak")) {
            int killstreak = dataStorage.getKillstreak(player);
            return String.valueOf(killstreak);
        }

        // Handle the %playerstats_sky_topkillstreak% placeholder
        if (identifier.equals("sky_topkillstreak")) {
            int topKillstreak = dataStorage.getTopKillstreak(player);
            return String.valueOf(topKillstreak);
        }

        return null;  // Return null for unsupported placeholders
    }

    @Override
    public String getIdentifier() {
        return "playerstats"; // The identifier for the expansion
    }

    @Override
    public String getAuthor() {
        return "YourName"; // Your name or the author of the expansion
    }

    @Override
    public String getVersion() {
        return "1.0"; // The version of the expansion
    }
}
