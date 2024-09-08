package com.bara.playerStats;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

public class PlayerStats extends JavaPlugin {

    private DataStorage dataStorage;

    @Override
    public void onEnable() {
        // Save default configuration
        saveDefaultConfig();
        FileConfiguration config = getConfig();

        // Initialize data storage based on config
        if (config.getBoolean("Using-MYSQL")) {
            dataStorage = new MySQLStorage(this);
        } else {
            dataStorage = new FileStorage(this);
        }

        // Register event listeners
        Bukkit.getPluginManager().registerEvents(new PlayerKillListener(dataStorage), this);
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(dataStorage), this);

        // Register commands
        getCommand("stats").setExecutor(new StatsCommand(dataStorage, this));
        getCommand("resetstats").setExecutor(new ResetStatsCommand(dataStorage, this));

        // Register PlaceholderAPI expansion if PlaceholderAPI is available
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new PlayerStatsExpansion(dataStorage).register();
        }
    }

    @Override
    public void onDisable() {
        // Close data storage connections
        dataStorage.close();
    }
}
