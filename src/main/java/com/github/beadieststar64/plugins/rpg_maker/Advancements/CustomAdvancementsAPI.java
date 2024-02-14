package com.github.beadieststar64.plugins.rpg_maker.Advancements;

import org.bukkit.NamespacedKey;

import javax.swing.text.Style;

public interface CustomAdvancementsAPI {

    NamespacedKey key;
    String icon;
    String message;
    Style style;

    void start();
    void createAdvancement();
    void grantAdvancement();
    void removeAdvancement();

    public static enum Style {
        GOAL,
        TASK,
        CHALLENGE
    }
}
