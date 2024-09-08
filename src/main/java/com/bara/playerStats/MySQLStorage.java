package com.bara.playerStats;

import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MySQLStorage implements DataStorage {

    private final PlayerStats plugin;
    private Connection connection;

    public MySQLStorage(PlayerStats plugin) {
        this.plugin = plugin;
        setupDatabase();
    }

    private void setupDatabase() {
        String url = plugin.getConfig().getString("MySQL.url");
        String user = plugin.getConfig().getString("MySQL.user");
        String password = plugin.getConfig().getString("MySQL.password");

        try {
            connection = DriverManager.getConnection(url, user, password);
            try (PreparedStatement stmt = connection.prepareStatement(
                    "CREATE TABLE IF NOT EXISTS player_stats (" +
                            "uuid VARCHAR(36) PRIMARY KEY, " +
                            "name VARCHAR(100), " +
                            "kills INT DEFAULT 0, " +
                            "deaths INT DEFAULT 0, " +
                            "kdr DOUBLE DEFAULT 0.0, " +
                            "killstreak INT DEFAULT 0, " +
                            "topkillstreak INT DEFAULT 0)")) {
                stmt.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void registerPlayer(Player player) {
        String uuid = player.getUniqueId().toString();
        try (PreparedStatement stmt = connection.prepareStatement(
                "INSERT INTO player_stats (uuid, name) VALUES (?, ?) " +
                        "ON DUPLICATE KEY UPDATE name = VALUES(name)")) {
            stmt.setString(1, uuid);
            stmt.setString(2, player.getName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void recordKill(Player player) {
        String uuid = player.getUniqueId().toString();
        try (PreparedStatement stmt = connection.prepareStatement(
                "UPDATE player_stats SET kills = kills + 1, killstreak = killstreak + 1, " +
                        "topkillstreak = GREATEST(topkillstreak, killstreak), " +
                        "kdr = IF(deaths = 0, kills + 0.0, kills / deaths) WHERE uuid = ?")) {
            stmt.setString(1, uuid);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void recordDeath(Player player) {
        String uuid = player.getUniqueId().toString();
        try (PreparedStatement stmt = connection.prepareStatement(
                "UPDATE player_stats SET deaths = deaths + 1, killstreak = 0, " +
                        "kdr = IF(deaths + 1 = 0, kills + 0.0, kills / (deaths + 1)) WHERE uuid = ?")) {
            stmt.setString(1, uuid);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getKills(Player player) {
        String uuid = player.getUniqueId().toString();
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT kills FROM player_stats WHERE uuid = ?")) {
            stmt.setString(1, uuid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("kills");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getDeaths(Player player) {
        String uuid = player.getUniqueId().toString();
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT deaths FROM player_stats WHERE uuid = ?")) {
            stmt.setString(1, uuid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("deaths");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public double getKDR(Player player) {
        String uuid = player.getUniqueId().toString();
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT kdr FROM player_stats WHERE uuid = ?")) {
            stmt.setString(1, uuid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("kdr");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    @Override
    public int getKillstreak(Player player) {
        String uuid = player.getUniqueId().toString();
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT killstreak FROM player_stats WHERE uuid = ?")) {
            stmt.setString(1, uuid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("killstreak");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int getTopKillstreak(Player player) {
        String uuid = player.getUniqueId().toString();
        try (PreparedStatement stmt = connection.prepareStatement(
                "SELECT topkillstreak FROM player_stats WHERE uuid = ?")) {
            stmt.setString(1, uuid);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("topkillstreak");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public void resetPlayerStats(Player player) {
        String uuid = player.getUniqueId().toString();
        try (PreparedStatement stmt = connection.prepareStatement(
                "UPDATE player_stats SET kills = 0, deaths = 0, kdr = 0.0, killstreak = 0, topkillstreak = 0 WHERE uuid = ?")) {
            stmt.setString(1, uuid);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
