package com.climpy.essentials;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class InvseeCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
		Player p = (Player) sender;

		if (sender instanceof Player && !p.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD)) {
			sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MOD.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
			return true;
		}

			if (args.length == 0 || args.length > 1) {
				p.sendMessage(ChatColor.RED + "§bDoğru Kullanım: §6/invsee <oyuncu>");
				return true;
			}
			Player t = Bukkit.getPlayer(args[0]);
			if (t == null) {
				sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cOyuncu bulunamadı."));
				return true;
			}
			if (!p.isOp()) {
					sender.sendMessage(ChatColor.RED + "Yüksek izinli oyuncuların envanterini göremezsiniz.");
					return true;
					}
			p.openInventory(t.getInventory());
		return true;
	}
}
