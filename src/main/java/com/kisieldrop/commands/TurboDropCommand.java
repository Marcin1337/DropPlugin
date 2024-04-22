package com.kisieldrop.commands;

import com.kisieldrop.Main;
import com.kisieldrop.managers.UserManager;
import com.kisieldrop.mysql.Database;
import com.kisieldrop.objects.User;
import com.kisieldrop.utils.HexUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import java.sql.SQLException;

public class TurboDropCommand implements CommandExecutor {

    private final UserManager userManager;
    private final Database database;

    public TurboDropCommand(UserManager userManager, Database database) {
        this.userManager = userManager;
        this.database = database;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] args) {
        Player player = (Player) sender;

        if (!player.hasPermission("kisieldrop.admin")) {
            player.sendMessage(HexUtil.color("&cBrak Uprawnień."));
            return true;
        }

        if (args.length != 2) {
            player.sendMessage(HexUtil.color("&cPoprawne Użycie: /turbodrop <gracz/serwer> <czas>"));
            return true;
        }

        if(args[0].equalsIgnoreCase("serwer")) {
            long time;
            try {
                time = Long.parseLong(args[1]);
            } catch (NumberFormatException exception) {
                player.sendMessage(HexUtil.color("&cNieprawidłowy format czasu."));
                return true;
            }

            FileConfiguration configuration = Main.getInstance().getConfig();
            configuration.set("turbodrop", System.currentTimeMillis() + time);
            Main.getInstance().saveConfig();
            return true;
        }

        Player target = Bukkit.getPlayerExact(args[0]);
        if (target == null) {
            player.sendMessage(HexUtil.color("&cPodany gracz jest offline."));
            return true;
        }

        long time;
        try {
            time = Long.parseLong(args[1]);
        } catch (NumberFormatException exception) {
            player.sendMessage(HexUtil.color("&cNieprawidłowy format czasu."));
            return true;
        }

        try {
            User user = userManager.getUser(target);
            user.setTurboDrop(System.currentTimeMillis() + time);
            database.updateUser(user);
            player.sendMessage(HexUtil.color("&cTurboDrop został nadany dla gracza &6" + target.getName() + "&c."));
        } catch (SQLException exception) {
            exception.fillInStackTrace();
        }

        return true;
    }
}
