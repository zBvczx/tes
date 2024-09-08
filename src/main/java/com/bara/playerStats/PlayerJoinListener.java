package com.bara.playerStats;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.entity.Player;

public class PlayerJoinListener implements Listener {

    private final DataStorage dataStorage;

    public PlayerJoinListener(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        dataStorage.registerPlayer(player);
    }
}