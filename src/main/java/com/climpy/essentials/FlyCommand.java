package com.climpy.essentials;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
		if (s.equalsIgnoreCase("fly")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Konsol bunu kullanamaz");
				return true;
			}

			User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
			Player player = (Player) sender;

			if (sender instanceof Player && !player.isOp() && !user.getRankType().isAboveOrEqual(RankType.VIPPLUS)) {
				sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.VIPPLUS.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
				return true;
			}


			if (args.length == 0) {
				player.setAllowFlight(!player.getAllowFlight());
				player.sendMessage(player.getAllowFlight() ? "§bUçuş modunuz §daçıldı." : "§bUçuş modunuz §ckapatıldı.");
			}
			else if (args[0].equalsIgnoreCase("on")) {
				if (player.getAllowFlight()) {
					player.sendMessage("" + ChatColor.RED + "Zaten uçuş modunuz açık.");
				} else {
					player.setAllowFlight(true);
					player.sendMessage("§bUçuş modu §daçıldı.");
				}
			} else if (args[0].equalsIgnoreCase("off")) {
				if (!player.getAllowFlight()) {
					player.sendMessage("" + ChatColor.RED + "Zaten uçuş modunuz kapalı.");
				} else {
					player.setAllowFlight(false);
					player.sendMessage("§bUçuş modu §ckapatıldı.");
				}
			}

			return true;
		}
		return false;
	}
}
