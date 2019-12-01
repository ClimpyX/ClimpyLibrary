package com.climpy.essentials;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TimeCommand implements CommandExecutor {
	
    @Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
        Player player = (Player) sender;

        if (sender instanceof Player && !player.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD)) {
            sender.sendMessage(org.bukkit.ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MOD.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
            return true;
        }
	
        if (sender instanceof Player) {
            if (cmd.getName().equalsIgnoreCase("day")) {
                if (args.length == 0) {
                    player.getWorld().setFullTime(0);
                    player.sendMessage("§aOyun zamanı §fgündüz §aolarak değiştirildi.");

                    return true;
                }
            }
            else if ((cmd.getName().equalsIgnoreCase("night")) && (args.length == 0)) {
                player.getWorld().setFullTime(16000);
                player.sendMessage("§aOyun zamanı §fgece §aolarak değiştirildi.");
                return true;
            }
        } else {
            sender.sendMessage("§cSadece oyuncular kullanabilir.");
        }
        return true;
    }
}