package com.climpy.essentials;

import com.climpy.ClimpyLibrary;
import com.climpy.utils.ConfigUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OPCommand implements CommandExecutor, Listener {
  ConfigUtils OpConfig = new ConfigUtils(ClimpyLibrary.getInstance().mainConfig, "op");

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String commandLable, String[] args) {
    CommandSender player = sender;
    CommandSender console = sender;
    if (cmd.getName().equalsIgnoreCase("op")) {
      if (sender instanceof org.bukkit.command.ConsoleCommandSender) {
        if (args.length == 1) {
          Player s = Bukkit.getPlayerExact(args[0]);
          if (s != null) {
            s.setOp(true);
            console.sendMessage("§f" + args[0] + " §aadlı oyuncuya artık OP.");
            s.sendMessage("§aKonsol tarafından sizin adınıza OP verildi.");
            log("§aKonsoldan §f" + args[0] + " §aisimli oyuncu op yapıldı.");
            return false;
          } 
          console.sendMessage("§cOyuncu oyunda değil.");
          log("§aKonsol §f" + args[0] + "§a adlı oyuncu OP yapmak istedi ancak oyuncu oyunda değildi.");
          return false;
        } 
        
        console.sendMessage("§bDoğru Kullanım: §6/op [OyuncuAdı] <şifre>");
        return false;
      } 
      if (sender instanceof Player) {
        if (player.isOp()) {
          if (args.length == 2) {
            if (args[1].equalsIgnoreCase(OpConfig.getString("pass"))) {
              
              Player s = Bukkit.getPlayerExact(args[0]);
              if (s != null) {
                s.setOp(true);
                if (player != s) {
                  player.sendMessage("§f" + args[0] + " §aadlı oyuncuya OP verdiniz.");
                  s.sendMessage("§c" + player.getName() + " §atarafından sizin için OP verildi.");
                  log(player.getName() + " isimli oyuncu " + args[0] + " isimli oyuncuya OP verdi.");
                } else {
                  player.sendMessage("§aKendinize OP verdiniz.");
                  return false;
                } 
                return true;
              } 
              player.sendMessage("§cOyuncu oyunda değil.");
              log(" §f" + player.getName() + " isimli oyuncu " + args[0] + " isimli oyuncuyu op yapmak istedi ancak oynucu oyunda değildi.");
              return false;
            } 
            
            player.sendMessage("§cHatalı bir şifre girdiniz.");
            log(" §f" + player.getName() + " isimli oyuncu " + args[0] + " isimli oyuncuyu op yapmak istedi ancak hatalı şifre girdi.");
            return false;
          }

          console.sendMessage("§bDoğru Kullanım: §6/op [OyuncuAdı] <şifre>");
          return false;
        } 

        
        player.sendMessage("§cBu komutu kullanamazsınız.");
        log(player.getName() + " isimli oyuncu op komutunu kullanmayı denedi ancak yetkisi yoktu.");
        return false;
      } 
    } else {
      
      if (cmd.getName().equalsIgnoreCase("deop")) {
        if (sender instanceof org.bukkit.command.ConsoleCommandSender) {
          if (args.length == 1) {
            Player s = Bukkit.getPlayerExact(args[0]);
            if (s != null) {
              s.setOp(false);
              console.sendMessage("§f" + args[0] + " §aadlı oyuncu DEOP yapıldı.");
              s.sendMessage("§aKONSOL tarafından sizin için DEOP yapıldınız.");
              log("Konsoldan " + args[0] + " adlı oyuncu DEOP yapıldı.");
            } else {
              console.sendMessage("§cOyuncu oyunda degil.");
              log("Konsoldan " + args[0] + " isimli oyuncu deop yapılmak istendi ancak oyuncu oyunda değildi.");
              return false;
            } 
          } else {
            console.sendMessage("§bDoğru Kullanım: §6/deop <Oyuncu Adı>");
          } 
        } else if (sender instanceof Player) {
          if (player.isOp()) {
            if (args.length == 2) {
              if (args[1].equalsIgnoreCase(OpConfig.getString("pass"))) {
                
                Player s = Bukkit.getPlayerExact(args[0]);
                if (s != null) {
                  s.setOp(false);
                  if (player != s) {
                    player.sendMessage("§f" + args[0] + " §aadlı oyuncuyu DEOP yaptınız.");
                    s.sendMessage("§f" + player.getName() + " §atarafından sizin için DeOP yapıldınız.");
                    log(player.getName() + " adl§ oyuncu " + args[0] + " isimli oyuncuyu deop yaptı.");
                  } else {
                    player.sendMessage("§aKendinizi DEOP yapt§n§z.");
                  } 
                  return true;
                } 
                player.sendMessage("§cOyuncu oyunda değil.");
                log(player.getName() + " isimli OP, " + args[0] + " isimli oyuncuyu deop yapmak istedi ancak oyuncu oyunda değildi.");
                return false;
              } 
              
              player.sendMessage("§cHatalı bir şifre girdiniz.");
              log(player.getName() + " isimli OP, " + args[0] + " isimli oyuncuyu deop yapmak istedi ancak Hatalı şifre girdi.");
              return false;
            } 
            
            player.sendMessage("§bDoğru Kullanım: §6/deop <Oyuncu Adı> [şifre..]");
            return false;
          } 

          
          player.sendMessage("§cBu komutu kullanmaya yetkiniz yok.");
          log(player.getName() + " isimli oyuncu deop komutunu kullanmay§ denedi ancak yetkisi yoktu.");
          return false;
        } 

        
        return false;
      }  if (cmd.getName().equalsIgnoreCase("opsifre") && player.isOp()) {
        if (sender instanceof Player) {
          player.sendMessage("§cŞifreli OP şifresi oyun içerisinden değiştirilemez.");
          return false;
        } 
        if (args.length != 3) {
          console.sendMessage("§cKullanım: /opsifre <Eski şifre..> [Yeni şifre..] [Yeni şifre..]");
          return true;
        } 
        if (args.length == 3 && args[0]
          .equalsIgnoreCase(OpConfig.getString("sifre"))) {
          
          if (args[1].equals(args[2])) {
            OpConfig.set("sifre", args[1]);
            OpConfig.configSave();
            console.sendMessage("§aOP sifresi §b" + args[1] + "§a olarak degistirildi.");
          } else {
            console.sendMessage("§cSifreler uyusmuyor.");
            return false;
          } 
        } else {
          console.sendMessage("§cHatali sifre girdiniz.");
          return false;
        } 
        
        return true;
      } 
      
      player.sendMessage("§cBu komutu kullanmaya yetkiniz yok.");
      log(player.getName() + " isimli oyuncu OP şifresini değiştirmeyi denedi.");
      return false;
    } 
    return false;
  }

  
  public void log(String message) {
    for (Player OPlar : Bukkit.getServer().getOnlinePlayers()) {
      if (OPlar.isOp()) {
        OPlar.sendMessage("§7[Log] §f" + message);
      }
    } 
    
    DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    Date date = new Date();
    
    try {
      File dataFolder = ClimpyLibrary.getInstance().getDataFolder();
      if (!dataFolder.exists()) {
        dataFolder.mkdir();
      }
      File saveTo = new File(ClimpyLibrary.getInstance().getDataFolder(), "op-log.txt");
      if (!saveTo.exists()) {
        saveTo.createNewFile();
      }
      FileWriter fw = new FileWriter(saveTo, true);
      PrintWriter pw = new PrintWriter(fw);
      pw.println(dateFormat.format(date) + " | " + message);
      pw.flush();
      pw.close();
    }
    catch (IOException e) {
      
      e.printStackTrace();
    } 
  }
}
