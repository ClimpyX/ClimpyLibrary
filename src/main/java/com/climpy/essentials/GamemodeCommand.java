package com.climpy.essentials;

import com.climpy.ClimpyLibrary;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.utils.C;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

public class GamemodeCommand implements CommandExecutor {
    public static boolean canSee(CommandSender sender, Player target) { return (target != null && (!(sender instanceof Player) || ((Player)sender).canSee(target))); }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player target, player = (Player) sender;
        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());

        if (!player.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD)) {
            sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MOD.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
            return true;
        }


        if (args.length < 1) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bDoğru Kullanım: &6/" + label + " [Oyun Modu] <oyuncu>"));
            return true;
        }
        GameMode mode = getGameModeByName(args[0]);
        if (mode == null) {
            sender.sendMessage(ChatColor.RED + "Oyun modu '" + ChatColor.WHITE + args[0] + ChatColor.RED + "' bulunamadı.");
            return true;
        }

        if (args.length > 1) {
            if (!player.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD)) {
                target = Bukkit.getPlayer(args[1]);
            } else {
                target = null;
            }
        } else {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&bDoğru Kullanım: &6/" + label + " [Oyun Modu] <oyuncu>"));
                return true;
            }

            target = (Player) sender;
        }

        if (target != null && canSee(sender, target)) {
            if (target.getGameMode() == mode) {
                sender.sendMessage("" + ClimpyLibrary.getInstance().getSuffix(player) + target.getName() + ChatColor.RED + " oyunusunun oyun modu zaten " + ChatColor.WHITE + mode.name() + '.');
                return true;
            }
            target.setGameMode(mode);
            sender.sendMessage(C.color(user.getRankType().getColor() + player.getName() + " &aiçin oyun modu &f'&f&l" + mode.name() + "&f' &aolarak değiştirildi."));
            return true;
        } else {

            sender.sendMessage(String.format("" + ChatColor.RED + "Oyuncu adı '" + ChatColor.RED + ChatColor.BOLD + args[1] + ChatColor.RED + "' bulunamadı.", new Object[0]));
            return true;
        }
    }

    private GameMode getGameModeByName(String id) {
        id = id.toLowerCase(Locale.ENGLISH);
        return (!id.equalsIgnoreCase("gmc") && !id.contains("creat") && !id.equalsIgnoreCase("1") && !id.equalsIgnoreCase("c")) ? ((!id.equalsIgnoreCase("gms") && !id.contains("survi") && !id.equalsIgnoreCase("0") && !id.equalsIgnoreCase("s")) ? ((!id.equalsIgnoreCase("gma") && !id.contains("advent") && !id.equalsIgnoreCase("2") && !id.equalsIgnoreCase("a")) ? ((!id.equalsIgnoreCase("gmt") && !id.contains("toggle") && !id.contains("cycle") && !id.equalsIgnoreCase("t")) ? null : null) : GameMode.ADVENTURE) : GameMode.SURVIVAL) : GameMode.CREATIVE;
    }
}
