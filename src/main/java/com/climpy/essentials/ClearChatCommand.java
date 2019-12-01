package com.climpy.essentials;

import com.climpy.ClimpyLibrary;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Iterator;

public class ClearChatCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        Player player = (Player)sender;

        if (sender == null) {
            sender.sendMessage(ChatColor.RED + "Sadece oyuncular kullanabilir.");
            return true;
        }

        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());

        if (sender instanceof Player && !player.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD)) {
            sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MOD.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
            return true;
        }


        if (arguments.length == 0) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bDoğru Kullanım: &6/" + label + " [Sebep]"));
        } else {
            Iterator var6 = Bukkit.getServer().getOnlinePlayers().iterator();

            while (var6.hasNext()) {
                Player online = (Player)var6.next();
                online.sendMessage(new String[101]);
                online.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b" + ClimpyLibrary.getInstance().getSuffix(player) + sender.getName() + " &aadlı yetkili sohbeti &f'" + StringUtils.join(arguments, ' ') + "' &asebebiyle sildi."));
            }
        }
        return false;
    }
}
