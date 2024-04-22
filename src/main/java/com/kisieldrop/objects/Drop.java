package com.kisieldrop.objects;

import org.bukkit.Material;

public class Drop
{
    private final Material material;
    private final String name;
    private final double chance;
    private final double bonus;
    private final int minAmount;
    private final int maxAmount;
    private final int blockY;
    private final int experience;
    private final boolean fortune;

    public Drop(Material material, String name, double chance, double bonus, int minAmount, int maxAmount, int blockY, int experience, boolean fortune) {
        this.material = material;
        this.name = name;
        this.chance = chance;
        this.bonus = bonus;
        this.minAmount = minAmount;
        this.maxAmount = maxAmount;
        this.blockY = blockY;
        this.experience = experience;
        this.fortune = fortune;
    }

    public Material getMaterial() {
        return material;
    }

    public String getCustomName() {
        return name;
    }

    public double getChance() {
        return chance;
    }

    public double getBonus() {
        return bonus;
    }

    public int getMinAmount() {
        return minAmount;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public int getBlockY() {
        return blockY;
    }

    public int getExperience() {
        return experience;
    }

    public boolean isFortune() {
        return fortune;
    }

}



