package com.github.z7087.AntiSwitchItem;

import com.github.z7087.AntiSwitchItem.utils.SpigotReflectionUtil;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class AntiSwitchItem extends JavaPlugin {

    @Override
    public void onLoad() {
        SpigotReflectionUtil.init();
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new SwitchItemListener(), this);
    }

    @Override
    public void onDisable() {
    }

}
