package com.kisieldrop.utils;

import net.md_5.bungee.api.ChatColor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HexUtil {
    private static final Pattern HEX_PATTERN = Pattern.compile("&#([a-fA-F0-9]{6})");

    public static String color(String text) {
        Matcher matcher = HEX_PATTERN.matcher(text);
        StringBuilder result = new StringBuilder(text.length());

        while (matcher.find()) {
            String hexColor = matcher.group(1);
            ChatColor chatColor = ChatColor.of("#" + hexColor);
            if (chatColor != null) {
                matcher.appendReplacement(result, chatColor.toString());
            }
        }
        matcher.appendTail(result);

        return ChatColor.translateAlternateColorCodes('&', result.toString());
    }
}

