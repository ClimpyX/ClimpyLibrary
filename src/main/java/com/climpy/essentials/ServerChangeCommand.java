package com.climpy.essentials;

import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.listeners.ChannelListener;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.utils.C;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ServerChangeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());

        if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
            sender.sendMessage(ChatColor.RED + "Sunucu değiştirme yetkisi sadece " + RankType.MOD.getDisplayName() + ChatColor.RED + " üzeri için geçerlidir.");
        }

        if (args.length < 1) {
            sender.sendMessage(ChatColor.RED + "Kullanım: " + label + " <sunucuAdı>");
            return true;
        }

        ChannelListener.sendToServer(player, args[0]);
        sender.sendMessage(C.color("&f" + args[0] + " &aisimli sunucuya aktarılıyorsun.."));
        return false;
    }
}
