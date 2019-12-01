package com.climpy.listeners;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.user.User;
import com.climpy.utils.ColorUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class DieEvent implements Listener {


    @EventHandler
    public void playerDeathEvent(PlayerDeathEvent event)  {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(event.getEntity().getName());
        User killerUser = ProfilePlugin.getInstance().getUserManager().getUser(event.getEntity().getKiller().getName());

        if(event.getEntity().getKiller() == null){
            event.setDeathMessage(new ColorUtils().translateFromString("&f&l➥ " + user.getRankType().getColor() + event.getEntity().getName() + " &döldü!"));
            event.getEntity().getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aÖldüğünüz koordinatlar: &7[&cX: " + event.getEntity().getPlayer().getLocation().getBlockX() + " &eY: " + event.getEntity().getPlayer().getLocation().getBlockY() + " &aZ: " + event.getEntity().getPlayer().getLocation().getBlockZ() + "&7]"));
        }else if(event.getEntity() instanceof Player){
            event.setDeathMessage(new ColorUtils().translateFromString("&f&l➥ " + user.getRankType().getColor() + event.getEntity().getName() + "&d, " + killerUser.getRankType().getColor() + event.getEntity().getKiller().getName() + " &dtarafından öldürüldü!"));
            event.getEntity().getPlayer().sendMessage(ChatColor.translateAlternateColorCodes('&', "&aÖldüğünüz koordinatlar: &7[&cX: " + event.getEntity().getPlayer().getLocation().getBlockX() + " &eY: " + event.getEntity().getPlayer().getLocation().getBlockY() + " &aZ: " + event.getEntity().getPlayer().getLocation().getBlockZ() + "&7]"));
        }
    }
}
