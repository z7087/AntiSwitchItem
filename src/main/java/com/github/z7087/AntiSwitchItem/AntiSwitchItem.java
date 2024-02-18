package com.github.z7087.AntiSwitchItem;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiSwitchItem extends JavaPlugin {

    @Override
    public void onLoad() {
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new SwitchItemListener(), this);
    }

    @Override
    public void onDisable() {
    }

}
