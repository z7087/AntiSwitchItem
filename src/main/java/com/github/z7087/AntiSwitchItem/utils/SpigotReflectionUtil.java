package com.github.z7087.AntiSwitchItem.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;

public class SpigotReflectionUtil {
    // from packetevents
    public static final String MODIFIED_PACKAGE_NAME = Bukkit.getServer().getClass().getPackage().getName()
            .replace(".", ",").split(",")[3];

    public static final String LEGACY_NMS_PACKAGE = "net.minecraft.server." + MODIFIED_PACKAGE_NAME + ".";
    public static final String OBC_PACKAGE = "org.bukkit.craftbukkit." + MODIFIED_PACKAGE_NAME + ".";

    public static final ServerVersion Version = ServerVersion.fromNMSVersion(MODIFIED_PACKAGE_NAME);

    public static final Class<?> ENTITY_PLAYER_CLASS, ENTITY_HUMAN_CLASS, ENTITY_LIVING_CLASS, CRAFT_PLAYER_CLASS, CRAFT_HUMAN_CLASS, CRAFT_LIVING_CLASS;

    public static final Method GET_CRAFT_PLAYER_HANDLE_METHOD, IS_USING_ITEM, GET_ACTIVE_HAND, CLEAR_ACTIVE_HAND;

    public static final boolean hasOffHand;
    public static final Object MAIN_HAND, OFF_HAND;

    public static final Object[] EMPTY = new Object[0];
    public static final Class[] EMPTYC = new Class[0];

    public static Class<?> getNMSClass(final String name) {
        try {
            return Class.forName(LEGACY_NMS_PACKAGE + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Class<?> getOBCClass(final String name) {
        try {
            return Class.forName(OBC_PACKAGE + name);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Method getMethod(final Class<?> clazz, final String methodName) {
        return getMethod(clazz, methodName, EMPTYC);
    }

    public static Method getMethod(final Class<?> clazz, final String methodName, final Class<?>... parameterTypes) {
        try {
            final Method m = clazz.getDeclaredMethod(methodName, parameterTypes);
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean isNotSupportVersion() {
        return Version.isNotSupport();
    }

    public static Object getCraftPlayer(final Player player) {
        return CRAFT_PLAYER_CLASS.cast(player);
    }

    public static Object getEntityPlayer(final Player player) {
        final Object craftPlayer = getCraftPlayer(player);
        try {
            return GET_CRAFT_PLAYER_HANDLE_METHOD.invoke(craftPlayer, EMPTY);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    static {
        if (isNotSupportVersion())
            throw new RuntimeException("This server already patched SwitchItem!");

        ENTITY_PLAYER_CLASS = getNMSClass("EntityPlayer");
        CRAFT_PLAYER_CLASS = getOBCClass("entity.CraftPlayer");
        ENTITY_HUMAN_CLASS = getNMSClass("EntityHuman");
        CRAFT_HUMAN_CLASS = getOBCClass("entity.CraftHumanEntity");
        ENTITY_LIVING_CLASS = getNMSClass("EntityLiving");
        CRAFT_LIVING_CLASS = getOBCClass("entity.CraftLivingEntity");

        GET_CRAFT_PLAYER_HANDLE_METHOD = getMethod(CRAFT_PLAYER_CLASS, "getHandle");
        switch (Version) {
            case V_1_7_10:
                IS_USING_ITEM = getMethod(ENTITY_HUMAN_CLASS, "by");
                GET_ACTIVE_HAND = null;
                CLEAR_ACTIVE_HAND = getMethod(ENTITY_HUMAN_CLASS, "bB");
                break;

            case V_1_8:
                IS_USING_ITEM = getMethod(ENTITY_HUMAN_CLASS, "bR");
                GET_ACTIVE_HAND = null;
                CLEAR_ACTIVE_HAND = getMethod(ENTITY_HUMAN_CLASS, "bU");
                break;
            case V_1_8_3:
            case V_1_8_8:
                IS_USING_ITEM = getMethod(ENTITY_HUMAN_CLASS, "bS");
                GET_ACTIVE_HAND = null;
                CLEAR_ACTIVE_HAND = getMethod(ENTITY_HUMAN_CLASS, "bV");
                break;

            case V_1_9:
                IS_USING_ITEM = getMethod(ENTITY_LIVING_CLASS, "cs");
                GET_ACTIVE_HAND = getMethod(ENTITY_LIVING_CLASS, "ct");
                CLEAR_ACTIVE_HAND = getMethod(ENTITY_LIVING_CLASS, "cz");
                break;
            case V_1_9_4:
                IS_USING_ITEM = getMethod(ENTITY_LIVING_CLASS, "ct");
                GET_ACTIVE_HAND = getMethod(ENTITY_LIVING_CLASS, "cu");
                CLEAR_ACTIVE_HAND = getMethod(ENTITY_LIVING_CLASS, "cA");
                break;

            case V_1_10:
                IS_USING_ITEM = getMethod(ENTITY_LIVING_CLASS, "cx");
                GET_ACTIVE_HAND = getMethod(ENTITY_LIVING_CLASS, "cy");
                CLEAR_ACTIVE_HAND = getMethod(ENTITY_LIVING_CLASS, "cE");
                break;

            case V_1_11:
                IS_USING_ITEM = getMethod(ENTITY_LIVING_CLASS, "isHandRaised");
                GET_ACTIVE_HAND = getMethod(ENTITY_LIVING_CLASS, "cz");
                CLEAR_ACTIVE_HAND = getMethod(ENTITY_LIVING_CLASS, "cF");
                break;

            case V_1_12:
                IS_USING_ITEM = getMethod(ENTITY_LIVING_CLASS, "isHandRaised");
                GET_ACTIVE_HAND = getMethod(ENTITY_LIVING_CLASS, "cH");
                CLEAR_ACTIVE_HAND = getMethod(ENTITY_LIVING_CLASS, "cN");
                break;

            case V_1_13:
            case V_1_13_2:
                IS_USING_ITEM = getMethod(ENTITY_LIVING_CLASS, "isHandRaised");
                GET_ACTIVE_HAND = getMethod(ENTITY_LIVING_CLASS, "cU");
                CLEAR_ACTIVE_HAND = getMethod(ENTITY_LIVING_CLASS, "da");
                break;

            case V_1_14:
                IS_USING_ITEM = getMethod(ENTITY_LIVING_CLASS, "isHandRaised");
                GET_ACTIVE_HAND = getMethod(ENTITY_LIVING_CLASS, "getRaisedHand");
                CLEAR_ACTIVE_HAND = getMethod(ENTITY_LIVING_CLASS, "dp");
                break;

            case V_1_15:
                IS_USING_ITEM = getMethod(ENTITY_LIVING_CLASS, "isHandRaised");
                GET_ACTIVE_HAND = getMethod(ENTITY_LIVING_CLASS, "getRaisedHand");
                CLEAR_ACTIVE_HAND = getMethod(ENTITY_LIVING_CLASS, "dH");
                break;

            default:
                throw new RuntimeException("How possible here...");
        }

        if (Version.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            hasOffHand = true;
            final Class<?> ENUM_HAND_CLASS = getNMSClass("EnumHand");
            final Object[] enum_hands = ENUM_HAND_CLASS.getEnumConstants();
            MAIN_HAND = enum_hands[0];
            OFF_HAND = enum_hands[1];
        } else {
            hasOffHand = false;
            MAIN_HAND = null;
            OFF_HAND = null;
        }
    }

}
