package com.kisieldrop.managers;

import com.kisieldrop.Main;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import com.kisieldrop.objects.Drop;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DropManager
{
    private ArrayList<Drop> items;
    public void importDrop() {
        items = new ArrayList<>();

        ConfigurationSection dropSection = Main.getInstance().getConfig().getConfigurationSection("Drop");
        if (dropSection != null) {
            for (String key : dropSection.getKeys(false)) {
                String customName = dropSection.getString(key + ".name");
                Material material = Material.matchMaterial((Objects.requireNonNull(dropSection.getString(key + ".material")).toUpperCase()));
                double chance = dropSection.getDouble(key + ".chance");
                double bonus = dropSection.getDouble(key + ".bonus");
                int minAmount = dropSection.getInt(key + ".minAmount");
                int maxAmount = dropSection.getInt(key + ".maxAmount");
                int blockY = dropSection.getInt(key + ".blockY");
                int experience = dropSection.getInt(key + ".experience");
                boolean fortune = dropSection.getBoolean(key + ".fortune");

                Drop drop = new Drop(material, customName, chance, bonus, minAmount, maxAmount, blockY, experience, fortune);
                items.add(drop);
            }
            Main.getInstance().getLogger().info("[KisielDrop] Successfully imported:" + " " + items.size() + " " + "objects.");
        }
    }

    public List<Drop> getDrop() {
        return items;
    }
}
