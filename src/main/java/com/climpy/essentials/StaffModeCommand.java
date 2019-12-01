package com.climpy.essentials;

import com.climpy.ClimpyLibrary;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.utils.ColorUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class StaffModeCommand implements CommandExecutor, TabCompleter {
  private final ClimpyLibrary climpyLibrary = ClimpyLibrary.getInstance();

  public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
      User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
      Player player = (Player) sender;


      if (!player.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD)) {
          sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MOD.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
          return true;
      }


        if (arguments.length > 0) {
            player.sendMessage((new ColorUtils()).translateFromString("&cKullanım: /" + label));
            return true;
        }

        if (this.climpyLibrary.getStaffModeListener().isStaffModeActive(player)) {
            this.climpyLibrary.getStaffModeListener().setStaffMode(player, false);
            player.sendMessage((new ColorUtils()).translateFromString("&aPersonel modu &ckapalı&a."));
        } else {
            this.climpyLibrary.getStaffModeListener().setStaffMode(player, true);
            player.sendMessage((new ColorUtils()).translateFromString("&aPersonel modu &aaçıldı&a."));
        }

        return true;
  }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) {
      if (arguments.length > 1)
      {
        return Collections.emptyList();
      }
      return null;
    }
}
