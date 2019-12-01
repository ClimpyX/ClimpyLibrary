package com.climpy.listeners;


import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.Arrays;
import java.util.List;

public class CommandPreprocessFixListener implements Listener {
    private List<String> muteCommands = Arrays.asList("/msg", "/m", "/tell", "/whisper", "/w", "/mesaj", "/pm", "/privatemessage", "/t", "/replay", "/resend", "/cevapla", "/r");
    private List<String> commands = Arrays.asList("//calc", "//eval", "//solve", "/bukkit:", "/me", "/bukkit:me", "/minecraft:", "/minecraft:me",  "/worldedit:", "/about", "/?", "/icanhasbukkit", "/ver", "/bukkit:ver", "/bukkit:version", "/version", "");

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void onPlayerCommandPreprocessEvent(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        Player player = event.getPlayer();
        User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getName());

        if (player == null) {
            return;
        }

        if (message.replace("/", "").split(" ")[0].equalsIgnoreCase("stop")) {
            event.setCancelled(true);

            if (!user.getRankType().isAboveOrEqual(RankType.OWNER)) {
                Bukkit.getServer().reload();
            }

            return;
        }

        for (String command : this.commands) {
            String[] space_message = message.split(" ");

            if (space_message[0].toLowerCase().equals(command)) {
               // player.sendMessage(ChatColor.RED + "Üzgünüm, bu komutu kullanmanız daha iyi bir oyun keyfi için kapatıldı :)");
                event.setCancelled(true);
                return;
            }
        }

        //   PunishmentManager punishmentManager = BasicPlugin.getInstance().getPunishmentManager();
        //  Punishment punishment = punishmentManager.getPunishment(user.getUniqueUUID(), PunishmentType.IP_MUTE, PunishmentType.MUTE);
        //  for (String command : this.muteCommands) {
        //    String[] space_message = message.split(" ");

        // if (punishment != null && space_message[0].toLowerCase().equals(command)) {
        //      player.sendMessage(ChatColor.RED + "Susturulmuş iken bu komutu kullanmazsınız.");
        //       event.setCancelled(true);
        // }
        //    }
    }
}
