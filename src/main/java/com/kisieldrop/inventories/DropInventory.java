package com.kisieldrop.inventories;


import com.kisieldrop.Main;
import com.kisieldrop.mysql.Database;
import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.BaseGui;
import dev.triumphteam.gui.guis.Gui;
import dev.triumphteam.gui.guis.GuiItem;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import com.kisieldrop.managers.DropManager;
import com.kisieldrop.managers.UserManager;
import com.kisieldrop.objects.Drop;
import com.kisieldrop.objects.User;
import com.kisieldrop.utils.HexUtil;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Objects;

public class DropInventory {

    private final DropManager dropManager;
    private final UserManager userManager;
    private final Database database;

    public DropInventory(DropManager dropManager, UserManager userManager, Database database) {
        this.dropManager = dropManager;
        this.userManager = userManager;
        this.database = database;
    }

    public void showInventory(Player player) throws SQLException {

        dropManager.getDrop();
        User user = userManager.getUser(player);

        String title = "&8Drop z kamienia.";


        BaseGui gui = Gui.gui()
                .title(Component.text(HexUtil.color(title)))
                .rows(6)
                .create();

        Integer[] yellowGlass = {1, 7, 9, 17, 36, 44, 46, 52};
        Integer[] whiteGlass = {2, 3, 4, 5, 6, 18, 26, 27, 35, 47, 48, 49, 50, 51};
        Integer[] orangeGlass = {0, 8, 45, 53};

        Arrays.stream(yellowGlass).forEach(slot -> gui.setItem(slot, ItemBuilder.from(Material.YELLOW_STAINED_GLASS_PANE).setName(" ").asGuiItem()));
        Arrays.stream(whiteGlass).forEach(slot -> gui.setItem(slot, ItemBuilder.from(Material.WHITE_STAINED_GLASS_PANE).setName(" ").asGuiItem()));
        Arrays.stream(orangeGlass).forEach(slot -> gui.setItem(slot, ItemBuilder.from(Material.ORANGE_STAINED_GLASS_PANE).setName(" ").asGuiItem()));

        GuiItem limeDye = ItemBuilder
                .from(Material.LIME_DYE)
                .setName(HexUtil.color("&aWłącz cały drop&7."))
                .asGuiItem();

        GuiItem redDye = ItemBuilder
                .from(Material.RED_DYE)
                .setName(HexUtil.color("&cWyłącz cały drop&7."))
                .asGuiItem();

        GuiItem book_and_quill = ItemBuilder
                .from(Material.LEGACY_BOOK_AND_QUILL)
                .setName(HexUtil.color("&cInformacje"))
                .setLore("",
                        HexUtil.color("&f» &7TurboDrop dla &aGracz'a&8: " + (user.getTurboDrop() > System.currentTimeMillis() ? "&a✔" : "&c✖")),
                        HexUtil.color("&f» &7TurboDrop dla &aSerwera&8: " + (Main.getInstance().getConfig().getLong("turbodrop") > System.currentTimeMillis() ? "&a✔" : "&c✖")),
                        "")
                .asGuiItem();

        GuiItem cobblestone = ItemBuilder
                .from(Material.COBBLESTONE)
                .setName(HexUtil.color("&7Drop bruku"))
                .setLore("", HexUtil.color("&f» &7ᴀᴋᴛʏᴡɴʏ&8: &6" + (user.isCancelled() ? "&a✔" : "&c✖")), "")
                .asGuiItem();

        gui.setItem(37,limeDye);
        gui.setItem(38, redDye);
        gui.setItem(40, book_and_quill);
        gui.setItem(43, cobblestone);

        gui.setDefaultClickAction(event -> {
            gui.disableItemDrop();
            gui.disableItemPlace();
            gui.disableItemSwap();
            gui.disableItemTake();
        });

        cobblestone.setAction(event -> {
            user.setCancelled(!user.isCancelled());
            try {
                database.updateUser(user);
                showInventory(player);
            } catch (SQLException exception) {
                exception.fillInStackTrace();
            }
        });

        limeDye.setAction(event -> {
            for(Drop drop : dropManager.getDrop()) {
                user.removeCancel(drop.getMaterial().toString());
                try {
                    database.updateUser(user);
                    showInventory(player);
                } catch (SQLException exception) {
                    exception.fillInStackTrace();
                }
            }
        });

        redDye.setAction(event -> {
            for(Drop drop : dropManager.getDrop()) {
                user.addCancel(drop.getMaterial().toString());
                try {
                    database.updateUser(user);
                    showInventory(player);
                } catch (SQLException exception) {
                    exception.fillInStackTrace();
                }
            }
        });


        for(Drop drop : dropManager.getDrop()) {
            GuiItem guiItem = ItemBuilder
                    .from(drop.getMaterial())
                    .setName(HexUtil.color(drop.getCustomName()))
                    .setLore("",
                            HexUtil.color("&f» &7ѕᴢᴀɴѕᴀ&8: &6" + drop.getChance()),
                            HexUtil.color("&f» &7ᴇxᴘ&8: &6" + drop.getExperience()),
                            HexUtil.color("&f» &7ᴡʏᴘᴀᴅᴀ ᴘᴏɴɪᴢᴇᴊ&8: &6Y<" + drop.getBlockY()),
                            HexUtil.color("&f» &7ɪʟᴏᴏᴄ&8: &6" + drop.getMinAmount() + "-" + drop.getMaxAmount()),
                            HexUtil.color("&f» &7ꜰᴏʀᴛᴜɴᴀ&8: &6" + (drop.isFortune() ? "&a✔" : "&c✖")),
                            HexUtil.color("&f» &7ᴀᴋᴛʏᴡɴʏ&8: &6" + (user.getDisableDrops().contains(drop.getMaterial().toString()) ? "&c✖" : "&a✔")),
                            "")

                    .asGuiItem();

            guiItem.setAction(event -> {
                if(user.getDisableDrops().contains(Objects.requireNonNull(event.getCurrentItem()).getType().toString())) {
                    user.removeCancel(event.getCurrentItem().getType().toString());
                } else {
                    user.addCancel((event.getCurrentItem().getType()).toString());
                }
                try {
                    database.updateUser(user);
                    showInventory(player);
                } catch (SQLException exception) {
                    exception.fillInStackTrace();
                }
            });

            gui.addItem(guiItem);
        }
            gui.open(player);
    }
}
