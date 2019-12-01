package com.climpy.essentials;

import com.climpy.profile.listeners.ChannelListener;
import com.climpy.utils.C;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HubCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command label, String s, String[] args) {
        Player player = (Player) sender;

        ChannelListener.sendToServer(player, "lobby");
        sender.sendMessage(C.color("&aHub sunucusuna gönderiliyorsunuz.. (#1)"));
        return true;
    }
}
