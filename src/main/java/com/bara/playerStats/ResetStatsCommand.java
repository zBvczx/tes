package com.bara.playerStats;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

public class ResetStatsCommand implements CommandExecutor {

    private final DataStorage dataStorage;
    private final PlayerStats plugin;

    public ResetStatsCommand(DataStorage dataStorage, PlayerStats plugin) {
        this.dataStorage = dataStorage;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // Check if the sender has the necessary permission
        if (!sender.hasPermission("playerstats.admin") && !sender.isOp()) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
            return true;
        }

        // Check if the correct number of arguments is provided
        if (args.length != 2 || !"all".equalsIgnoreCase(args[0])) {
            sender.sendMessage(ChatColor.RED + "Usage: /resetstats all <playername>");
            return true;
        }

        Player targetPlayer = Bukkit.getPlayerExact(args[1]);
        if (targetPlayer == null) {
            sender.sendMessage(ChatColor.RED + "Player not found.");
            return true;
        }

        // Reset stats based on storage type
        dataStorage.resetPlayerStats(targetPlayer);

        sender.sendMessage(ChatColor.GREEN + "Statistics for " + targetPlayer.getName() + " have been reset.");
        return true;
    }
}
