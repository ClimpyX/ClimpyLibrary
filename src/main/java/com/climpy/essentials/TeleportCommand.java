package com.climpy.essentials;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandExecutor {
	public boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
		Player player = (Player) sender;

		if (sender instanceof Player && !player.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD)) {
			sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MOD.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
			return true;
		}

		if (args.length == 1) {
			Player p = (Player) sender;
			Player t = Bukkit.getPlayer(args[0]);
			if (t == null) {
				sender.sendMessage(ChatColor.RED + "Girilen oyuncu adı sunucu verilerinde bulunamıyor.");
				return true;
			}
			p.teleport(t);
			sender.sendMessage(ChatColor.GREEN + t.getName() + ChatColor.BLUE + " oyuncusuna ışınlandın.");
			return true;
		}

		if (args.length == 2) {
			if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
				sender.sendMessage(ChatColor.RED + "Yetkiye sahip değilsin.");
				return true;
			}

			Player p = Bukkit.getPlayer(args[0]);
			Player t = Bukkit.getPlayer(args[1]);
			if (p == null) {
				sender.sendMessage(ChatColor.RED + "Oyuncu " + ChatColor.WHITE + args[0] + ChatColor.RED + " çevrimdışı.");
				return true;
			}
			if (t == null) {
				sender.sendMessage(ChatColor.RED + "Oyuncu " + ChatColor.WHITE + args[1] + ChatColor.RED + " çevrimdışı.");
				return true;
			}
			p.teleport(t);
			sender.sendMessage("§a" + p.getName() + " §9oyuncusunu §6" + t.getName() + " §9oyuncusuna ışınladın.");
			return true;
		}

		sender.sendMessage("§bDoğru Kullanım: §6/" + commandLabel + " [Oyuncu] [isteğe bağlı: 2. Oyuncu]");
		
		if (cmd.getName().equalsIgnoreCase("teleportall")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "konsol agla krdsm mal misin");
				return true;
			}
			Player p = (Player) sender;
			if (!player.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD)) {
				sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MOD.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
				return true;
			}
			
			if (args.length > 0) {
				sender.sendMessage("§bDoğru Kullanım: §6/" + commandLabel);
				return true;
			}
			for (Player online : Bukkit.getOnlinePlayers()) {
				if (online != p) {
					online.teleport(p);
					online.sendMessage("§a" + p.getName() + " §9isimli oyuncu herkesi kendine ışınlandı.");
				}
			}
			p.sendMessage("§9Tüm çevrim içi oyuncular şu anda size ışınlandı.");
			return true;
		}
		
		
		if (cmd.getName().equalsIgnoreCase("tphere")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "konsol agla krdsm mal misin");
				return true;
			}
			if (!player.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD)) {
				sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MOD.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
				return true;
			}
			
			if (args.length == 1) {
				Player p = (Player) sender;
				Player t = Bukkit.getPlayer(args[0]);
				if (t == null) {
					sender.sendMessage(ChatColor.RED + "Oyuncu " + ChatColor.WHITE + args[0] + ChatColor.RED + " çevrimdışı.");
					return true;
				}
				t.teleport(p);
				p.sendMessage("§9Sen §a" + t.getName() + " §9oyuncusunu kendinize ışınlandınız.");
				t.sendMessage("§a" + p.getName() + " §9isimli oyuncu kendisini size ışınladı.");
				return true;
			}
				sender.sendMessage("§bDoğru Kullanım: §6/" + commandLabel + " [Oyuncu]");
				return true;
		}
		
		
		if (cmd.getName().equalsIgnoreCase("tppos")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "konsol agla krdsm mal misin");
				return true;
			}
			if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
				sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MOD.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
				return true;
			}

			Player p = (Player) sender;
			
			if (args.length < 3 || args.length > 3) {
				sender.sendMessage("§bDoğru Kullanım: §6/" + commandLabel + " [x] [y] [z]");
				return true;
			}
			if (!isDouble(args[0]) || !isDouble(args[1]) || !isDouble(args[2])) {
				sender.sendMessage("§bDoğru Kullanım: §6/" + commandLabel + " [x] [y] [z]");
				return true;
			}
			double x = Double.parseDouble(args[0])+0.5;
			double y = Double.parseDouble(args[1])+0.5;
			double z = Double.parseDouble(args[2])+0.5;
			Location loc = new Location(p.getWorld(), x, y, z, p.getLocation().getYaw(), p.getLocation().getPitch());
			p.teleport(loc);
			p.sendMessage("§9Belirtilen kordinatlara ışınlandınız. §7(§a" + x + "§d, §a" + y + "§d, §a" + z + "§7)");
				return true;
		}
		return true;
	}
}
