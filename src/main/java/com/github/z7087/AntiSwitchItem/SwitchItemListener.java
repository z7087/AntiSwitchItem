package com.github.z7087.AntiSwitchItem;

import com.github.z7087.AntiSwitchItem.utils.ServerVersion;
import com.github.z7087.AntiSwitchItem.utils.SpigotReflectionUtil;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

import java.lang.reflect.InvocationTargetException;


public class SwitchItemListener implements Listener {
    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemHeldEvent(PlayerItemHeldEvent event) {
        if (event.getPreviousSlot() != event.getNewSlot()) {
            try {
                Object entityPlayer = SpigotReflectionUtil.getEntityPlayer(event.getPlayer());
                if ((boolean)(SpigotReflectionUtil.IS_USING_ITEM.invoke(entityPlayer, SpigotReflectionUtil.EMPTY))) {
                    if (SpigotReflectionUtil.Version.isNewerThanOrEquals(ServerVersion.V_1_9) && SpigotReflectionUtil.GET_ACTIVE_HAND.invoke(entityPlayer, SpigotReflectionUtil.EMPTY) != SpigotReflectionUtil.MAIN_HAND)
                        return;
                    SpigotReflectionUtil.CLEAR_ACTIVE_HAND.invoke(entityPlayer, SpigotReflectionUtil.EMPTY);
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}
