package com.climpy.listeners;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class ServerMotdListener implements Listener {

    @EventHandler
    public void getMotd(ServerListPingEvent e) {
        e.setMotd(ChatColor.translateAlternateColorCodes('&', "&eClimpyLib kullanılıyor."));
    }
}
