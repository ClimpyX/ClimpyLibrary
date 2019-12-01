package com.climpy.essentials;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.utils.ToolUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class BroadcastCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
    Player player = (Player) sender;

    if (sender instanceof Player && !player.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD)) {
      sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MOD.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
      return true;
    }


    if (args.length < 1) {
      sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bDoğru Kullanım: &6/" + label + " [Mesaj]"));
      return true;
    }

    String broadcastMessage = ToolUtils.joinStrings(args, 0);
    if (broadcastMessage.length() < 4) {
      sender.sendMessage(ChatColor.RED + "En az 3 kelime girmelisin.");
      return true;
    }

    Bukkit.broadcastMessage(ChatColor.DARK_GRAY + "[" + ChatColor.YELLOW.toString() + ChatColor.BOLD + "DUYURU" + ChatColor.DARK_GRAY + "] " + ChatColor.translateAlternateColorCodes('&', broadcastMessage));
    return true;
  }
}
