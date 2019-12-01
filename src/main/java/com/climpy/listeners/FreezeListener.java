package com.climpy.listeners;

import com.climpy.ClimpyLibrary;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.utils.ColorUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;


public class FreezeListener
  implements Listener
{
  private final ClimpyLibrary utilities = ClimpyLibrary.getInstance();
  
  private final Set<UUID> frozen = new HashSet();

  
  private AlertTask alertTask;

  
  public boolean isFrozen(Player player) { return this.frozen.contains(player.getUniqueId()); }


  
  public void setFreeze(CommandSender sender, Player target, boolean status) {
    if (status) {
      
      this.frozen.add(target.getUniqueId());
      this.alertTask = new AlertTask(target);
      this.alertTask.runTaskTimerAsynchronously(this.utilities, 0L, 100L);
      
      if (sender instanceof Player) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
        Bukkit.getServer().getConsoleSender().sendMessage((new ColorUtils()).translateFromString("&7[Uyarı] &c" + user.getRankType().getColor() + target.getName() + " &aartık donmuş durumda değil, kaldıran kişi; &f" + sender.getName() + "&a."));
        for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
          if (target.isOp() && user.getRankType().isAboveOrEqual(RankType.MOD)) {
            if (staff.equals(sender)) {

              sender.sendMessage((new ColorUtils()).translateFromString("&f" + user.getRankType().getColor() + user.getRankType().getColor() + target.getName() + " &aşu anda dondurulmuş durumda."));
              
              continue;
            }
            staff.sendMessage((new ColorUtils()).translateFromString("&7[Uyarı] &c" + user.getRankType().getColor() + target.getName() + " &aartık donmuş durumda değil, kaldıran kişi; &f" + sender.getName() + "&a."));
          }
        
        }
      
      } else {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
        sender.sendMessage((new ColorUtils()).translateFromString("&f" + user.getRankType().getColor() + target.getName() + " &aşu anda dondurulmuş durumda."));
        for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
          if (target.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD)) {
            staff.sendMessage((new ColorUtils()).translateFromString("&7[Uyarı] &c" + user.getRankType().getColor() + target.getName() + " &aartık donmuş durumda değil, kaldıran kişi; &f" + sender.getName() + "&a."));
          }
        }
      
      } 
    } else {
      
      this.alertTask.cancel();
      this.frozen.remove(target.getUniqueId());
      target.sendMessage((new ColorUtils()).translateFromString("&aArtık dondurulmuş değilsin."));
      
      if (sender instanceof Player) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
        Bukkit.getServer().getConsoleSender().sendMessage((new ColorUtils()).translateFromString("&7[Uyarı] &c" + user.getRankType().getColor() + target.getName() + " &aartık donmuş durumda değil, kaldıran kişi; &f" + sender.getName() + "&a."));
        for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
          if (target.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD)) {
            if (staff.equals(sender)) {

              sender.sendMessage((new ColorUtils()).translateFromString("&f" + user.getRankType().getColor() + target.getName() + " &aartık dondurulmuş değil."));
              
              continue;
            }
            staff.sendMessage((new ColorUtils()).translateFromString("&7[Uyarı] &c" + user.getRankType().getColor() + target.getName() + " &aartık donmuş durumda değil, kaldıran kişi; &f" + sender.getName() + "&a."));
          }
        
        }
      
      } else {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(sender.getName());
        sender.sendMessage((new ColorUtils()).translateFromString("&f" + user.getRankType().getColor() + target.getName() + " &aartık dondurulmuş değil."));
        for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
          if (!target.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD)) {
            staff.sendMessage((new ColorUtils()).translateFromString("&7[Uyarı] &c" + user.getRankType().getColor() + target.getName() + " &aartık donmuş durumda değil, kaldıran kişi; &f" + sender.getName() + "&a."));
          }
        } 
      } 
    } 
  }

  
  @EventHandler
  public void onPlayerMove(PlayerMoveEvent event) {
    Player player = event.getPlayer();
    
    Location from = event.getFrom();
    Location to = event.getTo();
    if (isFrozen(player))
    {
      
      if (from.getBlockX() != to.getBlockX() || from.getBlockZ() != to.getBlockZ())
      {
        event.setTo(from);
      }
    }
  }

  
  @EventHandler
  public void onProjectileLaunch(ProjectileLaunchEvent event) {
    if (event.getEntity().getShooter() instanceof Player) {
      
      Player player = (Player)event.getEntity().getShooter();
      if (isFrozen(player)) {
        
        event.setCancelled(true);
        player.sendMessage((new ColorUtils()).translateFromString("&cDondurulmuş durumdayken bu öğe ile etkileşime giremezsiniz."));
      } 
    } 
  }

  
  @EventHandler
  public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
    Player player = event.getPlayer();
    if (isFrozen(player))
    {
      if (!event.getMessage().startsWith("/helpop") && 
        !event.getMessage().startsWith("/faction chat") && 
        !event.getMessage().startsWith("/fac chat") && 
        !event.getMessage().startsWith("/f chat") && 
        !event.getMessage().startsWith("/faction c") && 
        !event.getMessage().startsWith("/fac c") && 
        !event.getMessage().startsWith("/f c") && 
        !event.getMessage().startsWith("/helpop") && 
        !event.getMessage().startsWith("/request") && 
        !event.getMessage().startsWith("/msg") && 
        !event.getMessage().startsWith("/message") && 
        !event.getMessage().startsWith("/reply")) {
        
        event.setCancelled(true);
        player.sendMessage((new ColorUtils()).translateFromString("&cDondurulmuş durumdayken komutları kullanamazsınız."));
      } 
    }
  }

  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event) {
    Player player = event.getPlayer();
    if (event.getBlock() != null)
    {
      if (isFrozen(player)) {
        
        event.setCancelled(true);
        player.sendMessage((new ColorUtils()).translateFromString("&cDondurulmuş durumdayken blok yerleştiremezsiniz."));
      } 
    }
  }

  
  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    Player player = event.getPlayer();
    if (event.getBlock() != null)
    {
      if (isFrozen(player)) {
        
        event.setCancelled(true);
        player.sendMessage((new ColorUtils()).translateFromString("&cDondurulmuş durumdayken blokları kıramazsınız."));
      } 
    }
  }

  
  @EventHandler
  public void onPlayerKick(PlayerKickEvent event) {
    Player player = event.getPlayer();
    if (isFrozen(player)) {
      
      this.alertTask.cancel();
      this.frozen.remove(player.getUniqueId());
    } 
  }

  
  public Player getPlayer(Entity entity) {
    if (entity instanceof Player)
    {
      return (Player)entity;
    }
    if (entity instanceof Projectile) {
      
      Projectile projectile = (Projectile)entity;
      if (projectile.getShooter() != null && projectile.getShooter() instanceof Player)
      {
        return (Player)projectile.getShooter();
      }
    } 
    return null;
  }

  
  @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    Player damager = getPlayer(event.getDamager());
    Player damaged = getPlayer(event.getEntity());
    if (damager != null && damaged != null && damaged != damager) {
      
      if (this.utilities.getFreezeListener().isFrozen(damager)) {
        
        damager.sendMessage((new ColorUtils()).translateFromString("&cDonmuş oyunculara saldıramazsınız."));
        event.setCancelled(true);
      } 
      
      if (this.utilities.getFreezeListener().isFrozen(damaged)) {
        
        damager.sendMessage((new ColorUtils()).translateFromString("&cSaldıramazsın " + damaged.getName() + " çünkü şu anda donmuş."));
        event.setCancelled(true);
      } 
    } 
  }

  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getName());
    if (isFrozen(player)) {
      for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
        if (player.isOp() && user.getRankType().isAboveOrEqual(RankType.MOD)) {
          player.sendMessage((new ColorUtils()).translateFromString("&7[Uyarı] &6" + player.getName() + " &cdondurulmuş iken oyundan çıktı. "));
        } 
      } 
    }
  }

  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    if (isFrozen(player)) {
      
      this.alertTask = new AlertTask(player);
      this.alertTask.runTaskTimerAsynchronously(this.utilities, 0L, 100L);
      
      for (Player staff : Bukkit.getServer().getOnlinePlayers()) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getName());
        if (player.isOp() && user.getRankType().isAboveOrEqual(RankType.MOD)) {

          player.sendMessage((new ColorUtils()).translateFromString("&7[Uyarı] &6" + player.getName() + " &cdondurulmuş iken oyundan çıktı. "));
        } 
      } 
    } 
  }

  
  private class AlertTask
    extends BukkitRunnable
  {
    private final Player player;
    
    public AlertTask(Player player) { this.player = player; }



    
    public void run() {
      if (FreezeListener.this.isFrozen(this.player)) {
        
        this.player.setHealth(this.player.getMaxHealth());
        this.player.setFireTicks(0);
        this.player.setFoodLevel(20);
        this.player.setSaturation(3.0F);
        for (PotionEffect potionEffect : this.player.getActivePotionEffects())
        {
          this.player.removePotionEffect(potionEffect.getType());
        }
        
        this.player.sendMessage((new ColorUtils()).translateFromString(" "));
        this.player.sendMessage((new ColorUtils()).translateFromString("&f▉▉▉▉&c▉&f▉▉▉▉"));
        this.player.sendMessage((new ColorUtils()).translateFromString("&f▉▉▉&c▉&6▉&c▉&f▉▉▉"));
        this.player.sendMessage((new ColorUtils()).translateFromString("&f▉▉&c▉&6▉&0▉&6▉&c▉&f▉▉ &4&lÇıkış yapmayın!"));
        this.player.sendMessage((new ColorUtils()).translateFromString("&f▉▉&c▉&6▉&0▉&6▉&c▉&f▉▉ &eEğer oturumu kapatırsanız, yasaklanacaksınız!"));
        this.player.sendMessage((new ColorUtils()).translateFromString("&f▉&c▉&6▉▉&0▉&6▉▉&c▉&f▉ &fDiscord'u indirin ve bağlanın:"));
        this.player.sendMessage((new ColorUtils()).translateFromString("&f▉&c▉&6▉▉▉▉▉&c▉&f▉ &fdiscord.gg/climpylib"));
        this.player.sendMessage((new ColorUtils()).translateFromString("&c▉&6▉▉▉&0▉&6▉▉▉&c▉"));
        this.player.sendMessage((new ColorUtils()).translateFromString("&c▉▉▉▉▉▉▉▉▉"));
        this.player.sendMessage((new ColorUtils()).translateFromString(" "));
      } 
    }
  }
}
