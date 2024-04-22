package com.kisieldrop.events;

import com.kisieldrop.Main;
import com.kisieldrop.managers.DropManager;
import com.kisieldrop.managers.UserManager;
import com.kisieldrop.objects.Drop;
import com.kisieldrop.objects.User;
import com.kisieldrop.utils.HexUtil;
import com.kisieldrop.utils.RandomUtil;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BlockBreakListener implements Listener
{
    private final DropManager dropManager;
    private final UserManager userManager;
    private final List<Material> pickaxes = Arrays.asList(Material.WOODEN_PICKAXE, Material.STONE_PICKAXE, Material.IRON_PICKAXE, Material.DIAMOND_PICKAXE, Material.NETHERITE_PICKAXE);

    public BlockBreakListener(DropManager dropManager, UserManager userManager) {
        this.dropManager = dropManager;
        this.userManager = userManager;
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) throws SQLException {
        Player player = event.getPlayer();
        User user = userManager.getUser(player);
        Block block = event.getBlock();
        List<Drop> items = dropManager.getDrop();
        ItemStack itemInHand = player.getInventory().getItemInHand();
        ItemMeta itemMeta = itemInHand.getItemMeta();

        if(player.getGameMode() != GameMode.SURVIVAL || block.getType() != Material.STONE || !pickaxes.contains(itemInHand.getType())) {
            return;
        }

       if(!user.isCancelled()) {
           block.setType(Material.AIR);
           event.setCancelled(true);
       }

        for (Drop drop : items) {

            int amount  = RandomUtil.getRandInt(drop.getMinAmount(), drop.getMaxAmount());
            ItemStack itemStack = new ItemStack(drop.getMaterial());
            double chance = drop.getChance();

            if(block.getLocation().getBlockY() <= drop.getBlockY()) {

                if(user.getTurboDrop() > System.currentTimeMillis()) {
                    chance *= drop.getBonus();
                }

                if(Main.getInstance().getConfig().getLong("turbodrop") > System.currentTimeMillis()) {
                    chance *= drop.getBonus();
                }

                if(RandomUtil.getChance(chance)) {
                    if(!user.getDisableDrops().contains(drop.getMaterial().toString())) {
                        if (itemMeta != null && itemMeta.getEnchantLevel(Enchantment.LOOT_BONUS_BLOCKS) >= 3 && drop.isFortune()) {
                            itemStack.setAmount(amount);
                        } else {
                            itemStack.setAmount(drop.getMinAmount());
                        }

                        giveItem(player, itemStack);
                        player.giveExp(drop.getExperience());
                        //ActionbarAPI.sendActionbar(player, HexUtil.color("&6Trafileś na: " + drop.getCustomName() + " &7( x&6" + itemStack.getAmount() + " &7)"));
                        player.sendMessage(HexUtil.color("&f» &7Trafileś na: " + drop.getCustomName() + " &7( x&6" + itemStack.getAmount() + " &7)."));
                    }
                }
            }
        }
    }

    public void giveItem(Player player, ItemStack item) {
        Inventory inventory = player.getInventory();
        HashMap<Integer, ItemStack> notStored = inventory.addItem(item);
        for (Map.Entry<Integer, ItemStack> e : notStored.entrySet()) {
            player.getWorld().dropItemNaturally(player.getLocation(), e.getValue());
            player.updateInventory();
        }
    }
}
