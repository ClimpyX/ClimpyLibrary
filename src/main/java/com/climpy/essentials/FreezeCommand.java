package com.climpy.essentials;

import com.climpy.ClimpyLibrary;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;


public class FreezeCommand implements CommandExecutor, TabCompleter {
  private final ClimpyLibrary climpyLibrary = ClimpyLibrary.getInstance();

    @Override
   public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
    User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
    Player player = (Player) sender;

    if (sender instanceof Player && !player.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD)) {
      sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MOD.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
      return true;
    }

    if (arguments.length == 0 || arguments.length > 1) {
      sender.sendMessage((new ColorUtils()).translateFromString("&cKullanım: /" + label + " <oyuncuİsmi>"));
    } else {
      Player target = Bukkit.getServer().getPlayerExact(arguments[0]);
        User targetUser = ProfilePlugin.getInstance().getUserManager().getUser(target.getName());

      if (target == null) {
        sender.sendMessage((new ColorUtils()).translateFromString("&cOyuncu '" + arguments[0] + "' bulunamadı."));
        return false;

      } else if (target.equals(sender)) {
        sender.sendMessage((new ColorUtils()).translateFromString("&cKendini donduramazsın."));
        return false;
      }

      if (sender instanceof Player) {
        if (targetUser.getRankType().isAboveOrEqual(RankType.MOD)) {
          sender.sendMessage((new ColorUtils()).translateFromString("&cBir personel üyesini donduramazsınız."));
        } else if (climpyLibrary.getFreezeListener().isFrozen(target)) {
          climpyLibrary.getFreezeListener().setFreeze(sender, target, false);
        } else {
          climpyLibrary.getFreezeListener().setFreeze(sender, target, true);
        }


      } else if (climpyLibrary.getFreezeListener().isFrozen(target)) {
        climpyLibrary.getFreezeListener().setFreeze(sender, target, false);
      } else {
        climpyLibrary.getFreezeListener().setFreeze(sender, target, true);

      }

    }
    return false;
  }



  
  public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) {
    if (arguments.length > 1)
    {
      return Collections.emptyList();
    }
    return null;
  }
}
