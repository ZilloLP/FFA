package de.zillolp.ffa.database;

import de.zillolp.ffa.FFA;
import de.zillolp.ffa.profiles.PlayerProfile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

public class DatabaseManager {
    private final FFA plugin;
    private final DatabaseConnector databaseConnector;
    private final String playerTable;

    public DatabaseManager(FFA plugin) {
        this.plugin = plugin;
        this.databaseConnector = plugin.getDatabaseConnector();
        this.playerTable = plugin.getPluginConfig().getFileConfiguration().getString("PLAYER_TABLE", "ffa_players");
        initialize();
    }

    private void initialize() {
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("CREATE TABLE IF NOT EXISTS " + playerTable + "(UUID VARCHAR(64), NAME VARCHAR(64), KILLS BIGINT, DEATHS BIGINT);");
            preparedStatement.executeUpdate();
            preparedStatement.closeOnCompletion();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void createPlayer(UUID uuid, String name) {
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO " + playerTable + "(UUID, NAME, KILLS, DEATHS) VALUES (?, ?, ?, ?);");
            preparedStatement.setString(1, uuid.toString());
            preparedStatement.setString(2, name);
            preparedStatement.setLong(3, 0);
            preparedStatement.setLong(4, 0);
            preparedStatement.executeUpdate();
            preparedStatement.closeOnCompletion();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public boolean playerExists(UUID uuid, String name) {
        boolean isExisting = false;
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT UUID, NAME FROM " + playerTable + " WHERE UUID= ?");
            preparedStatement.setString(1, uuid.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                if (!(name.equalsIgnoreCase(resultSet.getString("NAME")))) {
                    PreparedStatement preparedStatement1 = connection.prepareStatement("UPDATE " + playerTable + " SET NAME= ? WHERE UUID= ?");
                    preparedStatement1.setString(1, name);
                    preparedStatement1.setString(2, uuid.toString());
                    preparedStatement1.executeUpdate();
                    preparedStatement1.closeOnCompletion();
                }
                isExisting = resultSet.getString("UUID") != null;
            }
            resultSet.close();
            preparedStatement.closeOnCompletion();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return isExisting;
    }

    public void loadProfiles() {
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT UUID FROM " + playerTable);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UUID uuid = UUID.fromString(resultSet.getString("UUID"));
                HashMap<UUID, PlayerProfile> playerProfiles = plugin.getPlayerManager().getPlayerProfiles();
                if (playerProfiles.containsKey(uuid)) {
                    continue;
                }
                playerProfiles.put(uuid, new PlayerProfile(plugin, uuid));
            }
            resultSet.close();
            preparedStatement.closeOnCompletion();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void loadProfile(PlayerProfile playerProfile) {
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM " + playerTable + " WHERE UUID= ?");
            preparedStatement.setString(1, playerProfile.getUuid().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                playerProfile.setName(resultSet.getString("NAME"));
                playerProfile.setKills(resultSet.getLong("KILLS"));
                playerProfile.setDeaths(resultSet.getLong("DEATHS"));
            }
            resultSet.close();
            preparedStatement.closeOnCompletion();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void uploadProfile(PlayerProfile playerProfile) {
        try (Connection connection = databaseConnector.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE " + playerTable + " SET KILLS= ?, DEATHS= ? WHERE UUID= ?");
            preparedStatement.setLong(1, playerProfile.getKills());
            preparedStatement.setLong(2, playerProfile.getDeaths());
            preparedStatement.setString(3, playerProfile.getUuid().toString());
            preparedStatement.executeUpdate();
            preparedStatement.closeOnCompletion();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }
}
