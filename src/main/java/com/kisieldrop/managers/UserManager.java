package com.kisieldrop.managers;

import com.kisieldrop.mysql.Database;
import org.bukkit.entity.Player;
import com.kisieldrop.objects.User;

import java.sql.SQLException;
import java.util.ArrayList;

public class UserManager {

    private final Database database;
    public UserManager(Database database) {
        this.database = database;
    }

    public User getUser(Player player) throws SQLException {
        User user = database.getUserByUUID(player.getUniqueId().toString());

        if(user == null) {
            user = new User(player.getName(), player.getUniqueId().toString(), 0L, true, new ArrayList<>());
            database.createUser(user);
        }
           return user;
    }
}
