package com.climpy.essentials;

import com.climpy.ClimpyLibrary;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.google.common.collect.ImmutableList;
import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;

public class KillCommand implements CommandExecutor, TabExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
    Player player = Bukkit.getPlayer(sender.getName());
    if (sender instanceof Player) {
      Player targetPlayer;
      User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());

      if (sender instanceof Player && !player.isOp() && user.getRankType().isAboveOrEqual(RankType.MODPLUS)) {
        sender.sendMessage(ChatColor.RED + "Bu komutu gerçekleştirmek için " + RankType.MODPLUS.getDisplayName() + ChatColor.RED + " veya üzeri rütbe gerekiyor.");
        return true;
      }

      if (args.length == 1) {
        targetPlayer = Bukkit.getPlayerExact(args[0]);
        if (targetPlayer == null) {
          sender.sendMessage(ChatColor.RED + "Oyuncu bulunamadı.");
          return true;
        } 
      } else {
        targetPlayer = player;
      } 

      
      if (targetPlayer.isDead()) {
        sender.sendMessage(ChatColor.RED + " WTF! " + ClimpyLibrary.getInstance().getSuffix(player) + targetPlayer.getName() + ChatColor.RED + " zaten ölü!");
        return true;
      }

      
      EntityDamageEvent entityDamageEvent = new EntityDamageEvent(targetPlayer, EntityDamageEvent.DamageCause.SUICIDE, '?');
      Bukkit.getPluginManager().callEvent(entityDamageEvent);

      
      if (entityDamageEvent.isCancelled()) {
        return true;
      }
      
      entityDamageEvent.getEntity().setLastDamageCause(entityDamageEvent);
      targetPlayer.setHealth(0.0D);
      
      if (targetPlayer == sender) {
        sender.sendMessage(ChatColor.AQUA + "Ahh! Bu acıttı gibi görünüyor.");
      } else {
        sender.sendMessage(ChatColor.AQUA + "" + targetPlayer.getName() + " için acıttın ama.");
        targetPlayer.sendMessage(ChatColor.GRAY + "GG WP!");
      } 
      
      return true;
    } 
    sender.sendMessage("nop..r.");
    return true;
  }


  
  public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
    Validate.notNull(sender, "Sender boş olamaz");
    Validate.notNull(args, "Arguments boş olamaz");
    Validate.notNull(label, "Alias boş olamaz");
    
    if (args.length == 1) {
      List<String> onlinePlayers = new ArrayList<String>();
      
      for (Player player : Bukkit.getOnlinePlayers()) {
        if (player.canSee(player) && player.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
          onlinePlayers.add(player.getName());
        }
      } 
      
      return onlinePlayers;
    } 
    
    return ImmutableList.of();
  }
}
