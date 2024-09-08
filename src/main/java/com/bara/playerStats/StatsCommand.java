package com.bara.playerStats;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.ChatColor;

public class StatsCommand implements CommandExecutor {

    private final DataStorage dataStorage;
    private final PlayerStats plugin;

    public StatsCommand(DataStorage dataStorage, PlayerStats plugin) {
        this.dataStorage = dataStorage;
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player player) {
            // Check permission for /stats command
            if (!player.hasPermission("playerstats.stats")) {
                player.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                return true;
            }

            Player targetPlayer;

            if (args.length == 0) {
                // No arguments, show stats for the player executing the command
                targetPlayer = player;
            } else {
                // Check if the player is trying to view their own stats
                if (args[0].equalsIgnoreCase(player.getName())) {
                    targetPlayer = player;
                } else {
                    // Check permission for viewing other players' stats
                    if (!player.hasPermission("playerstats.stats.others")) {
                        player.sendMessage(ChatColor.RED + "You do not have permission to view other players' statistics.");
                        return true;
                    }

                    targetPlayer = Bukkit.getPlayerExact(args[0]);
                    if (targetPlayer == null) {
                        player.sendMessage(ChatColor.RED + "Player not found.");
                        return true;
                    }
                }
            }

            // Retrieve statistics
            int kills = dataStorage.getKills(targetPlayer);
            int deaths = dataStorage.getDeaths(targetPlayer);
            double kdr = dataStorage.getKDR(targetPlayer);
            int killstreak = dataStorage.getKillstreak(targetPlayer);
            int topKillstreak = dataStorage.getTopKillstreak(targetPlayer);

            // Prepare the messages from config with color support
            FileConfiguration config = plugin.getConfig();
            String[] messages = config.getStringList("stats-command").toArray(new String[0]);
            String playerName = (targetPlayer.equals(player)) ? config.getString("me") : targetPlayer.getName();

            // Display statistics
            for (String message : messages) {
                message = ChatColor.translateAlternateColorCodes('&', message)
                        .replace("%player%", playerName)
                        .replace("%kills%", String.valueOf(kills))
                        .replace("%deaths%", String.valueOf(deaths))
                        .replace("%kdr%", String.format("%.2f", kdr))
                        .replace("%killstreak%", String.valueOf(killstreak))
                        .replace("%topkillstreak%", String.valueOf(topKillstreak));
                player.sendMessage(message);
            }
        } else {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
        }
        return true;
    }
}
