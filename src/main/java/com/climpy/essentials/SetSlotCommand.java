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

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getServer;

public class SetSlotCommand implements CommandExecutor {


    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
        Player player = (Player) sender;

        if (sender instanceof Player && !player.isOp() && user.getRankType().isAboveOrEqual(RankType.ADMIN)) {
            sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.ADMIN.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("§bDoğru Kullanım: §6/" + label + " [değer]");
            return true;
        }

        try {
            changeSlots(Integer.parseInt(args[0]));

            sender.sendMessage(ChatColor.GREEN + "Sunucuya girebilecek maksimum oyuncu sayısı " + ChatColor.WHITE + args[0] + ChatColor.GREEN + " olarak değiştirildi.");
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "Geçersiz bir değer girildi.");
        } catch (ReflectiveOperationException e) {
            sender.sendMessage(ChatColor.RED + "Bir hata oluştu, konsola bakınız.");

            Bukkit.getLogger().log(Level.SEVERE, "Maksimum oyuncu guncellenirken bir hata olustu ", e);
        }

        return true;
    }



    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) { return Collections.emptyList(); }



    private String getConfigString(String key) { return ChatColor.translateAlternateColorCodes('&', ClimpyLibrary.getInstance().getConfig().getString(key)); }


    private void changeSlots(int slots) throws ReflectiveOperationException {
        Method serverGetHandle = getServer().getClass().getDeclaredMethod("getHandle", new Class[0]);

        Object playerList = serverGetHandle.invoke(getServer(), new Object[0]);
        Field maxPlayersField = playerList.getClass().getSuperclass().getDeclaredField("maxPlayers");

        maxPlayersField.setAccessible(true);
        maxPlayersField.set(playerList, Integer.valueOf(slots));
    }

    private void updateServerProperties() {
        Properties properties = new Properties();
        File propertiesFile = new File("server.properties");

        try {
            InputStream is = new FileInputStream(propertiesFile);
            try { properties.load(is);
                is.close(); } catch (Throwable throwable) { try { is.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }
                try {
                    throw throwable;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String maxPlayers = Integer.toString(getServer().getMaxPlayers());

            if (properties.getProperty("max-players").equals(maxPlayers)) {
                return;
            }

            Bukkit.getLogger().info("Maksimum oyuncular server.properties'e kaydediliyor..");
            properties.setProperty("max-players", maxPlayers);

            OutputStream os = new FileOutputStream(propertiesFile);
            try { properties.store(os, "Minecraft server properties");
                os.close(); } catch (Throwable throwable) { try { os.close(); } catch (Throwable throwable1) { throwable.addSuppressed(throwable1); }  throw throwable; }
        } catch (IOException e) {
            Bukkit.getLogger().log(Level.SEVERE, "Sunucu ozelliklerinde maksimum oyuncu kaydedilirken hata olustu ", e);
        }
    }


}
