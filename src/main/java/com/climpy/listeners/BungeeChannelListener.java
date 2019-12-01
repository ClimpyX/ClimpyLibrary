package com.climpy.listeners;

import com.climpy.ClimpyLibrary;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.UUID;
import java.util.logging.Level;

  public class BungeeChannelListener implements PluginMessageListener, Listener {
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
      if (channel.equals("BungeeCord")) {
       // System.out.println("Bungeecord veri temkin edildi.");
      }

      User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getName());
      if (channel.equalsIgnoreCase("WDL|INIT")) {

        if (!user.getRankType().isAboveOrEqual(RankType.MOD))
          return;
        kickPlayer(player.getUniqueId(), ChatColor.RED + "Oopps! Sunucudan atıldınız.\nGirişi sağlayabilmek için, WDL modunu kapatmanız gerekiyor.");
      }
    }


    public static void kickPlayer(UUID uniqueUUID, String reason) {
      Validate.notNull(uniqueUUID, "Giriş değeri boş olamaz!");
      Validate.notNull(reason, "Giriş değeri boş olamaz!");

      ByteArrayDataOutput out = ByteStreams.newDataOutput();
      Player player = Bukkit.getPlayer(uniqueUUID);

      try {
        out.writeUTF("KickPlayer");
        out.writeUTF(player.getName());
        out.writeUTF(reason);

        player.sendPluginMessage(ClimpyLibrary.getInstance(), "BungeeCord", out.toByteArray());
      } catch (Exception ex) {
        ClimpyLibrary.getInstance().getLogger().log(Level.WARNING, ex.getMessage());
      }
    }
}
