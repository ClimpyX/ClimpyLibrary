package com.climpy.utils;

import org.bukkit.ChatColor;

public class C
{
    public static String color(final String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
