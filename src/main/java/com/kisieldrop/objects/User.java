package com.kisieldrop.objects;

import java.util.List;

public class User
{
    private final String playerName;
    private final String uuid;
    private long turboDrop;
    private boolean isCancelled;
    private final List<String> disableDrops;

    public User(String playerName, String uuid, long turboDrop, boolean isCancelled, List<String> disableDrops) {
        this.playerName = playerName;
        this.uuid = uuid;
        this.turboDrop = turboDrop;
        this.isCancelled = isCancelled;
        this.disableDrops = disableDrops;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getUuid() {
        return uuid;
    }

    public long getTurboDrop() {
        return turboDrop;
    }
    public void setTurboDrop(long turboDrop) {
        this.turboDrop = turboDrop;
    }
    public boolean isCancelled() {
        return isCancelled;
    }
    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }
    public List<String> getDisableDrops() {
        return this.disableDrops;
    }
    public void removeCancel(String material) {
        this.disableDrops.remove(material);
    }
    public void addCancel(String material) {
        if(!this.disableDrops.contains(material)) this.disableDrops.add((material));
    }

}
