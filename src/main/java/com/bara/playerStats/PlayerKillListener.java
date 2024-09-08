package com.bara.playerStats;

import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.entity.Player;

public class PlayerKillListener implements Listener {

    private final DataStorage dataStorage;

    public PlayerKillListener(DataStorage dataStorage) {
        this.dataStorage = dataStorage;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player victim && event.getDamager() instanceof Player attacker) {
            dataStorage.registerPlayer(victim);
            dataStorage.registerPlayer(attacker);
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = victim.getKiller();

        dataStorage.registerPlayer(victim);

        if (killer != null) {
            dataStorage.registerPlayer(killer);
            dataStorage.recordKill(killer);
        }

        dataStorage.recordDeath(victim);
    }
}
