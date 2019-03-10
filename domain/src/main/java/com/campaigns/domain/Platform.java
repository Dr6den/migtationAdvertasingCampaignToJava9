package com.campaigns.domain;

/**
 *
 * @author andrew
 */
public enum Platform {
    WEB(0),
    ANDROID(1),
    IOS(2);
    private final int value;
    private Platform(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
    public static Platform fromInteger(int x) {
        switch(x) {
        case 0:
            return WEB;
        case 1:
            return ANDROID;
        case 2:
            return IOS;
        }
        return null;
    }
}
