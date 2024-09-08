package com.bara.playerStats;

import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class FileStorage implements DataStorage {

    private final PlayerStats plugin;
    private final File dataFolder;

    public FileStorage(PlayerStats plugin) {
        this.plugin = plugin;
        this.dataFolder = new File(plugin.getDataFolder(), "data"); // Changed folder name back to "data"
        if (!dataFolder.exists()) {
            dataFolder.mkdirs();
        }
    }

    @Override
    public void registerPlayer(Player player) {
        File playerFile = new File(dataFolder, player.getUniqueId() + ".yml");
        if (!playerFile.exists()) {
            try {
                playerFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Initialize new player data
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
            playerData.set("name", player.getName());
            playerData.set("kills", 0);
            playerData.set("deaths", 0);
            playerData.set("killstreak", 0);
            playerData.set("topkillstreak", 0);
            playerData.set("kdr", 0.0);
            try {
                playerData.save(playerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void recordKill(Player player) {
        File playerFile = new File(dataFolder, player.getUniqueId() + ".yml");
        if (playerFile.exists()) {
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
            int kills = playerData.getInt("kills", 0) + 1;
            int killstreak = playerData.getInt("killstreak", 0) + 1;
            int topKillstreak = Math.max(playerData.getInt("topkillstreak", 0), killstreak);
            int deaths = playerData.getInt("deaths", 0);
            double kdr = deaths == 0 ? kills + 0.0 : kills / (double) deaths;

            playerData.set("kills", kills);
            playerData.set("killstreak", killstreak);
            playerData.set("topkillstreak", topKillstreak);
            playerData.set("kdr", kdr);

            try {
                playerData.save(playerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void recordDeath(Player player) {
        File playerFile = new File(dataFolder, player.getUniqueId() + ".yml");
        if (playerFile.exists()) {
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
            int deaths = playerData.getInt("deaths", 0) + 1;
            int killstreak = 0;
            int kills = playerData.getInt("kills", 0);
            double kdr = deaths == 0 ? kills + 0.0 : kills / (double) deaths;

            playerData.set("deaths", deaths);
            playerData.set("killstreak", killstreak);
            playerData.set("kdr", kdr);

            try {
                playerData.save(playerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getKills(Player player) {
        File playerFile = new File(dataFolder, player.getUniqueId() + ".yml");
        if (playerFile.exists()) {
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
            return playerData.getInt("kills", 0);
        }
        return 0;
    }

    @Override
    public int getDeaths(Player player) {
        File playerFile = new File(dataFolder, player.getUniqueId() + ".yml");
        if (playerFile.exists()) {
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
            return playerData.getInt("deaths", 0);
        }
        return 0;
    }

    @Override
    public double getKDR(Player player) {
        File playerFile = new File(dataFolder, player.getUniqueId() + ".yml");
        if (playerFile.exists()) {
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
            return playerData.getDouble("kdr", 0.0);
        }
        return 0.0;
    }

    @Override
    public int getKillstreak(Player player) {
        File playerFile = new File(dataFolder, player.getUniqueId() + ".yml");
        if (playerFile.exists()) {
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
            return playerData.getInt("killstreak", 0);
        }
        return 0;
    }

    @Override
    public int getTopKillstreak(Player player) {
        File playerFile = new File(dataFolder, player.getUniqueId() + ".yml");
        if (playerFile.exists()) {
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
            return playerData.getInt("topkillstreak", 0);
        }
        return 0;
    }

    @Override
    public void resetPlayerStats(Player player) {
        File playerFile = new File(dataFolder, player.getUniqueId() + ".yml");
        if (playerFile.exists()) {
            FileConfiguration playerData = YamlConfiguration.loadConfiguration(playerFile);
            playerData.set("kills", 0);
            playerData.set("deaths", 0);
            playerData.set("kdr", 0.0);
            playerData.set("killstreak", 0);
            playerData.set("topkillstreak", 0);
            try {
                playerData.save(playerFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void close() {
        // No action needed for file-based storage
    }
}
