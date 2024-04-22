package com.kisieldrop;


import com.kisieldrop.inventories.*;
import com.kisieldrop.commands.*;
import com.kisieldrop.events.*;
import com.kisieldrop.managers.*;
import com.kisieldrop.mysql.*;
import org.bukkit.plugin.java.*;
import java.sql.*;

public class Main extends JavaPlugin {
    private static Main instance;
    private final DropManager dropManager;
    private final UserManager userManager;
    private final DropInventory dropInventory;
    private final Database database;

    public Main() {
        this.dropManager = new DropManager();
        this.database = new Database();
        this.userManager = new UserManager(database);
        this.dropInventory = new DropInventory(dropManager, userManager, database);

    }

    public static Main getInstance() {
        return instance;
    }
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        dropManager.importDrop();


        try {
            this.database.initializeDatabase();
        } catch (SQLException exception) {
            exception.fillInStackTrace();
            System.out.println("[KisielDrop] Could not initialize database.");
        }


        this.getServer().getPluginManager().registerEvents(new BlockBreakListener(dropManager, userManager), this);
        this.getCommand("drop").setExecutor(new DropCommand(dropInventory));
        this.getCommand("turbodrop").setExecutor(new TurboDropCommand(userManager, database));
    }
    public void onDisable() {

    }
}
