package com.climpy.listeners;

import com.climpy.ClimpyLibrary;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    private ClimpyLibrary climpyLibrary;

    @EventHandler
    public void Playerjoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (!ClimpyLibrary.enabled) {
            String kickmsg = ChatColor.translateAlternateColorCodes('&', "&c[ClimpyLibrary]\n\n&eSunucu açılamıyor, sorun olabilir.\nDaha fazla sorun oluşmaması için girişler otomatik kapandı!\n&bDetaylı bilgiler için &6&nww.climpylibrary.com");
            player.kickPlayer(kickmsg);
            return;
        }
        event.setJoinMessage(null);
    }

}
