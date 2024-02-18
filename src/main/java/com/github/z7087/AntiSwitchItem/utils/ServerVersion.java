package com.github.z7087.AntiSwitchItem.utils;

public enum ServerVersion {

    V_1_7_10,
    V_1_8, V_1_8_3, V_1_8_8,
    V_1_9, V_1_9_4,
    V_1_10,
    V_1_11,
    V_1_12,
    V_1_13, V_1_13_2,
    V_1_14,
    V_1_15,
    NOT_SUPPORT;

    public static ServerVersion fromNMSVersion(final String version) {
        switch (version) {
            case "v1_7_R4":
                return V_1_7_10;

            case "v1_8_R1":
                return V_1_8;
            case "v1_8_R2":
                return V_1_8_3;
            case "v1_8_R3":
                return V_1_8_8;

            case "v1_9_R1":
                return V_1_9;
            case "v1_9_R2":
                return V_1_9_4;

            case "v1_10_R1":
                return V_1_10;

            case "v1_11_R1":
                return V_1_11;

            case "v1_12_R1":
                return V_1_12;

            case "v1_13_R1":
                return V_1_13;
            case "v1_13_R2":
                return V_1_13_2;

            case "v1_14_R1":
                return V_1_14;

            case "v1_15_R1":
                return V_1_15;

            default:
                return NOT_SUPPORT;
        }
    }

    public boolean isNewerThan(ServerVersion target) {
        return this.ordinal() > target.ordinal();
    }

    public boolean isOlderThan(ServerVersion target) {
        return this.ordinal() < target.ordinal();
    }

    public boolean isNewerThanOrEquals(ServerVersion target) {
        return this.ordinal() >= target.ordinal();
    }

    public boolean isOlderThanOrEquals(ServerVersion target) {
        return this.ordinal() <= target.ordinal();
    }

    public boolean isNotSupport() {
        return this == NOT_SUPPORT;
    }

    public boolean isSupport() {
        return this != NOT_SUPPORT;
    }
}
