package com.happy3w.ideamgr.model;

/**
 * Created by ysgao on 4/6/16.
 */
public enum EnumIdeaStatus {

    None(0, "None"),
    Processing(1, "Processing"),
    Finished(2, "Finished"),
    Canceled(3, "Canceled");

    private final int value;
    private final String name;
    EnumIdeaStatus(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static EnumIdeaStatus parse(int value) {
        for (EnumIdeaStatus a : EnumIdeaStatus.values()) {
            if (a.value == value) {
                return a;
            }
        }
        return null;
    }

    public static EnumIdeaStatus parse(String name) {
        if (name == null || name.isEmpty()) {
            return None;
        }
        for (EnumIdeaStatus t : EnumIdeaStatus.values()) {
            if (t.name.equalsIgnoreCase(name)) {
                return t;
            }
        }
        return null;
    }
}
