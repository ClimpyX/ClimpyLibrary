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


public class InspectCommand implements CommandExecutor, TabCompleter {
  private final ClimpyLibrary library = ClimpyLibrary.getInstance();

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
    if (sender instanceof Player) {

      Player player = (Player)sender;
      User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());

      if (sender instanceof Player && !player.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD)) {
        sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MOD.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
        return true;
      }
        
        if (arguments.length == 0 || arguments.length > 1)
        {
          player.sendMessage((new ColorUtils()).translateFromString("&cKullanım: /" + label + " <oyuncuİsmi>"));
        }
        else
        {
          Player target = Bukkit.getServer().getPlayerExact(arguments[0]);
          if (target == null)
          {
            player.sendMessage((new ColorUtils()).translateFromString("&cOyuncu '" + arguments[0] + "' bulunamadı."));


          }
          else if (target.equals(player))
          {
            player.sendMessage((new ColorUtils()).translateFromString("&cKendini kontrol edemezsin."));
          }
          else
          {
            this.library.getStaffModeListener().openInspectionMenu(player, target);
            player.sendMessage((new ColorUtils()).translateFromString("&aŞimdi inceliyorsunuz: &f" + ClimpyLibrary.getInstance().getSuffix(player) + target.getName() + "&a."));
          }

      }
    
    } else {
      
      sender.sendMessage((new ColorUtils()).translateFromString("&cagla kudur mal"));
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
