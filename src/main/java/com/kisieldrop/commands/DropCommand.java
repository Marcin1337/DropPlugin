package com.kisieldrop.commands;

import com.kisieldrop.inventories.DropInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class DropCommand implements CommandExecutor {

    private final DropInventory dropInventory;

    public DropCommand(DropInventory dropInventory) {
        this.dropInventory = dropInventory;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, String[] args) {
        Player player = (Player)sender;

        try {
            dropInventory.showInventory(player);
        } catch (SQLException exception) {
            exception.fillInStackTrace();
        }

        return true;
    }
}
