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

    public static Class<?> ENTITY_PLAYER_CLASS, ENTITY_HUMAN_CLASS, ENTITY_LIVING_CLASS, CRAFT_PLAYER_CLASS, CRAFT_HUMAN_CLASS, CRAFT_LIVING_CLASS;

    public static Method GET_CRAFT_PLAYER_HANDLE_METHOD, IS_USING_ITEM, GET_ACTIVE_HAND, CLEAR_ACTIVE_HAND;

    public static Object MAIN_HAND, OFF_HAND;

    public static boolean needInitClass = true, needInitMethod = true, needInitField = true;

    public static void init() {
        if (isSupportVersion()) {
            if (needInitClass)
                initClass();
            needInitClass = false;
            if (needInitMethod)
                initMethod();
            needInitMethod = false;
            if (needInitField)
                initField();
            needInitField = false;
        } else {
            throw new RuntimeException("This server already patched SwitchItem!");
        }
    }

    private static void initClass() {
        ENTITY_PLAYER_CLASS = getNMSClass("EntityPlayer");
        CRAFT_PLAYER_CLASS = getOBCClass("entity.CraftPlayer");
        ENTITY_HUMAN_CLASS = getNMSClass("EntityHuman");
        CRAFT_HUMAN_CLASS = getOBCClass("entity.CraftHumanEntity");
        ENTITY_LIVING_CLASS = getNMSClass("EntityLiving");
        CRAFT_LIVING_CLASS = getOBCClass("entity.CraftLivingEntity");
    }

    private static void initMethod() {
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
        }
    }

    private static void initField() {
        if (Version.isNewerThanOrEquals(ServerVersion.V_1_9)) {
            final Class<?> ENUM_HAND_CLASS = getNMSClass("EnumHand");
            Object[] enum_hands = ENUM_HAND_CLASS.getEnumConstants();
            MAIN_HAND = enum_hands[0];
            OFF_HAND = enum_hands[1];
        }
    }

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
        Method m;
        try {
            m = clazz.getDeclaredMethod(methodName);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
        m.setAccessible(true);
        return m;
    }

    public static boolean isSupportVersion() {
        return Version.isSupport();
    }

    public static Object getCraftPlayer(Player player) {
        return CRAFT_PLAYER_CLASS.cast(player);
    }

    public static Object getEntityPlayer(Player player) {
        Object craftPlayer = getCraftPlayer(player);
        try {
            return GET_CRAFT_PLAYER_HANDLE_METHOD.invoke(craftPlayer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
