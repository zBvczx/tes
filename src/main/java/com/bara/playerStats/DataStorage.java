package com.bara.playerStats;

import org.bukkit.entity.Player;

public interface DataStorage {

    void registerPlayer(Player player);

    void recordKill(Player player);

    void recordDeath(Player player);

    int getKills(Player player);

    int getDeaths(Player player);

    double getKDR(Player player);

    int getKillstreak(Player player);

    int getTopKillstreak(Player player);

    void resetPlayerStats(Player player);


    void close();

}
