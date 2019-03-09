package com.campaigns.domain;

/**
 *
 * @author andrew
 */
public enum Status {
    ACTIVE(1),
    PAUSED(2),
    FINISHED(3);
    private final int value;
    private Status(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
    public static Status fromInteger(int x) {
        switch(x) {
        case 1:
            return ACTIVE;
        case 2:
            return PAUSED;
        case 3:
            return FINISHED;
        }
        return null;
    }
}
