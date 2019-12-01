package com.climpy.essentials;

import com.climpy.ClimpyLibrary;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class HealCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
		Player p = (Player) sender;

		if (sender instanceof Player && !p.isOp() && !user.getRankType().isAboveOrEqual(RankType.MVIP)) {
			sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MVIP.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
			return true;
		}

		if (cmd.getName().equalsIgnoreCase("heal")) {
			
			if (args.length == 0) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cKonsol bunu kullanamaz."));
					return true;
				}
				
				p.sendMessage("§aKendinizi iyileştirdiniz.");
				p.setHealth(20);
				p.setFireTicks(0);
				for (PotionEffect po : p.getActivePotionEffects()) {
					p.removePotionEffect(po.getType());
				}
				p.setFoodLevel(20);
				p.setSaturation(10);
				return true;
			}
			if (args.length > 1) {
				sender.sendMessage("§bDoğru Kullanım: §6/heal [oyuncu]");
				return true;
			}
			Player t = Bukkit.getPlayer(args[0]);
			if (t == null) {
			sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cOyuncu bulunamadı."));
				return true;
			}
			sender.sendMessage("§f" + ClimpyLibrary.getInstance().getSuffix(t) + t.getName() + " §aiyileştirildi.");
			t.sendMessage("§f" + ClimpyLibrary.getInstance().getSuffix(p) + sender.getName() + " §aadlı yönetici tarafından iyileştirildiniz.");
			t.setHealth(20);
			t.setFireTicks(0);
			for (PotionEffect to : t.getActivePotionEffects()) {
				t.removePotionEffect(to.getType());
			}
			t.setFoodLevel(20);
			t.setSaturation(10);
			return true;
		}
		return true;
	}
	
}
