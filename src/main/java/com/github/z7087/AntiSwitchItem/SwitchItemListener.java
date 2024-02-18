package com.github.z7087.AntiSwitchItem;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

import org.bukkit.craftbukkit.v1_8_R3.entity.CraftHumanEntity;

import net.minecraft.server.v1_8_R3.EntityHuman;


public class SwitchItemListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemHeldEvent(PlayerItemHeldEvent event) {
        if (event.getPreviousSlot() != event.getNewSlot()) {
            EntityHuman playerNMS = ((CraftHumanEntity)event.getPlayer()).getHandle();
            if (playerNMS.bS()) {
                playerNMS.bV();
            }
        }
    }

}
