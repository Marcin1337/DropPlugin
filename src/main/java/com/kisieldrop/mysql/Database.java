package com.kisieldrop.mysql;

import com.google.gson.Gson;
import com.kisieldrop.Main;
import com.kisieldrop.objects.User;
import java.sql.*;
import java.util.List;

public class Database {

    private Connection connection;

    public Connection getConnection() throws SQLException {
        if (connection != null) {
            return connection;
        }

        String host = Main.getInstance().getConfig().getString("mysql.host");
        int port = Main.getInstance().getConfig().getInt("mysql.port");
        String table = Main.getInstance().getConfig().getString("mysql.table");
        String username = Main.getInstance().getConfig().getString("mysql.username");
        String password = Main.getInstance().getConfig().getString("mysql.password");

        connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + table, username, password);

        System.out.println("[KisielDrop] Connected with Database.");

        return connection;
    }

    public void initializeDatabase() throws SQLException {
        Statement statement = getConnection().createStatement();

        String sql = "CREATE TABLE IF NOT EXISTS kisiel_drop (playerName varchar(16), uuid varchar(36), turboDrop bigint, isCancelled boolean, disableDrops text)";

        statement.execute(sql);

        statement.close();
    }

    public User getUserByUUID(String uuid) throws SQLException {
        PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM kisiel_drop WHERE uuid = ?");
        statement.setString(1, uuid);

        ResultSet resultSet = statement.executeQuery();

        User user;

        if (resultSet.next()) {
            Gson gson = new Gson();
            List<String> disableDrops = gson.fromJson(resultSet.getString("disableDrops"), List.class);
            user = new User(resultSet.getString("playerName"), resultSet.getString("uuid"), resultSet.getLong("turboDrop"), resultSet.getBoolean("isCancelled"), disableDrops);
            statement.close();
            return user;
        }

        statement.close();

        return null;
    }

    public void createUser(User user) throws SQLException {
        Gson gson = new Gson();
        String disableDropsJson = gson.toJson(user.getDisableDrops());

        PreparedStatement statement = getConnection().prepareStatement("INSERT INTO kisiel_drop(playerName, uuid, turboDrop, isCancelled, disableDrops) VALUES (?, ?, ?, ?, ?)");
        statement.setString(1, user.getPlayerName());
        statement.setString(2, user.getUuid());
        statement.setLong(3, user.getTurboDrop());
        statement.setBoolean(4, user.isCancelled());
        statement.setString(5, disableDropsJson);

        statement.executeUpdate();

        statement.close();
    }

    public void updateUser(User user) throws SQLException {
        Gson gson = new Gson();
        String disableDropsJson = gson.toJson(user.getDisableDrops());

        PreparedStatement statement = getConnection().prepareStatement("UPDATE kisiel_drop SET turboDrop = ?, isCancelled = ?, disableDrops = ? WHERE uuid = ?");
        statement.setLong(1, user.getTurboDrop());
        statement.setBoolean(2, user.isCancelled());
        statement.setString(3, disableDropsJson);
        statement.setString(4, user.getUuid());

        statement.executeUpdate();

        statement.close();
    }
}
