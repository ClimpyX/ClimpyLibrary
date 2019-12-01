package com.climpy.essentials;

import com.climpy.ClimpyLibrary;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class ListCommand implements CommandExecutor, TabCompleter {
  private final ClimpyLibrary climpyLibrary = ClimpyLibrary.getInstance();

  private String getStaffOnline() {
    String message = "";
    for (Player player : Bukkit.getServer().getOnlinePlayers()) {
      User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getName());
      if (!climpyLibrary.getStaffModeListener().isStaffModeActive(player) && !climpyLibrary.getStaffModeListener().isVanished(player) && ! !user.getRankType().isAboveOrEqual(RankType.MOD)) {
        message = message + ClimpyLibrary.getInstance().getSuffix(player) + player.getName() + ", ";
      }
    } 
    
    if (message.length() > 2) {
      message = message.substring(0, message.length() - 2);
    } if (message.length() == 0) {
      message = "Şu anda tüm yöneticiler bu sunucuda aktif bulunmuyor.";
    }
    return message;
  }

  public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
    if (arguments.length >= 0) {

      User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());

      user.getRankType().getAllRanks();
      sender.sendMessage((new ColorUtils()).translateFromString("&eÇevrim içi Oyuncular&7: &a" + Bukkit.getServer().getOnlinePlayers().size() + "&7/&a" + Bukkit.getServer().getMaxPlayers()));
      sender.sendMessage((new ColorUtils()).translateFromString("&eMevcut Personel Üyeler&7: &a" + getStaffOnline()));
    } 
    return true;
  }



  
  public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) { return Collections.emptyList(); }
}
