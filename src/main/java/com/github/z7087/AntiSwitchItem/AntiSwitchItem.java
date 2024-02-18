package com.github.z7087.AntiSwitchItem;

import com.github.z7087.AntiSwitchItem.utils.SpigotReflectionUtil;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;


public class AntiSwitchItem extends JavaPlugin {
    protected boolean state = false;

    @Override
    public void onLoad() {
        final Class<?> loadUtil = (new SpigotReflectionUtil()).getClass();
        state = true;
    }

    @Override
    public void onEnable() {
        if (state)
            Bukkit.getPluginManager().registerEvents(new SwitchItemListener(), this);
    }

    @Override
    public void onDisable() {
    }

}
