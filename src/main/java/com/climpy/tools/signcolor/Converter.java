package com.climpy.tools.signcolor;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public final class Converter implements Listener {
  public static String convert(String line) { return line.replaceAll("&0", String.valueOf(ChatColor.BLACK)).replaceAll("&1", String.valueOf(ChatColor.DARK_BLUE)).replaceAll("&2", String.valueOf(ChatColor.DARK_GREEN)).replaceAll("&3", String.valueOf(ChatColor.DARK_AQUA)).replaceAll("&4", String.valueOf(ChatColor.DARK_RED)).replaceAll("&5", String.valueOf(ChatColor.DARK_PURPLE)).replaceAll("&6", String.valueOf(ChatColor.GOLD)).replaceAll("&7", String.valueOf(ChatColor.GRAY)).replaceAll("&8", String.valueOf(ChatColor.DARK_GRAY)).replaceAll("&9", String.valueOf(ChatColor.BLUE)).replaceAll("&a", String.valueOf(ChatColor.GREEN)).replaceAll("&b", String.valueOf(ChatColor.AQUA)).replaceAll("&c", String.valueOf(ChatColor.RED)).replaceAll("&d", String.valueOf(ChatColor.LIGHT_PURPLE)).replaceAll("&e", String.valueOf(ChatColor.YELLOW)).replaceAll("&f", String.valueOf(ChatColor.WHITE)).replaceAll("&k", String.valueOf(ChatColor.MAGIC)).replaceAll("&l", String.valueOf(ChatColor.BOLD)).replaceAll("&m", String.valueOf(ChatColor.STRIKETHROUGH)).replaceAll("&n", String.valueOf(ChatColor.UNDERLINE)).replaceAll("&o", String.valueOf(ChatColor.ITALIC)).replaceAll("&r", String.valueOf(ChatColor.RESET)); }


  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onSignCreate(SignChangeEvent event) {
    for (int i = 0; i < event.getLines().length; i++) {

      String line = event.getLine(i);
      String newLine = Converter.convert(line);
      event.setLine(i, newLine);
    }
  }


  @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
  public void onAsynchChat(AsyncPlayerChatEvent event) {
    String msg = event.getMessage();
    String newMsg = Converter.convert(msg);
    event.setMessage(newMsg);
  }
}
