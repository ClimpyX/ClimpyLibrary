package com.climpy.essentials;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.user.User;
import com.climpy.utils.PingUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;

public class PingCommand implements CommandExecutor, TabCompleter {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) {
        Player p = (Player)sender;
        User user = ProfilePlugin.getInstance().getUserManager().getUser(p.getName());

        if (arguments.length > 1) {
            sender.sendMessage(ChatColor.AQUA + "Kullanım: /" + ChatColor.GOLD + label + " <oyuncuAdı>");
            return true;
        }
        if (arguments.length == 0) {
            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.GREEN + "Ping değeri: " + ChatColor.WHITE + PingUtil.getPing((Player)sender) + "ms.");
            }
            else {

                sender.sendMessage(ChatColor.RED + "Bu komutu yanlizca oyundan kullanabilirsiniz");
            }
        }

        if (arguments.length == 1) {
            Player target = Bukkit.getServer().getPlayerExact(arguments[0]);
            if (target == null) {
                sender.sendMessage(ChatColor.RED + "Böyle bir oyuncu yok.");
            } else {
                sender.sendMessage("" + ChatColor.GREEN + user.getRankType().getColor() + target.getName() + ChatColor.GREEN + " adlı oyuncunun ping değeri: " + ChatColor.WHITE + ChatColor.BOLD +
                        PingUtil.getPing(target) + "ms.");
            }
        }

        return true;
    }

    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] arguments) {
        if (arguments.length > 1) {
            return Collections.emptyList();
        }

        return null;
    }
}
