package com.climpy.utils;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;



public class PlayerUtil
{
  public static HashMap<String, String> player = new HashMap();
  public static HashMap<Player, Integer> ScramblePoints = new HashMap();
  public static HashMap<Player, Boolean> upgrades = new HashMap();
  public static HashMap<Player, Boolean> flight = new HashMap();
  public static ArrayList<Player> Scrambled = new ArrayList();
  public static ArrayList<String> masked = new ArrayList();


  
  public static void startFlight(Player player) {
    if (!player.getAllowFlight()) player.setAllowFlight(true); 
    if (!player.isFlying()) player.setFlying(true);
  
  }
  
  public static void stopFlight(Player player) {
    if (player.isFlying() || player.getAllowFlight() == true) {
      player.setFlying(false);
      player.setAllowFlight(false);
    } 
  }


  
  public static boolean hasAvailableUpgrades(Player player) { return upgrades.containsKey(player); }


  
  public static void enableUpgrades(Player player) {
    if (!upgrades.containsKey(player)) {
      upgrades.put(player, Boolean.valueOf(true));
    }
  }

  
  public static void disableUpgrades(Player player) {
    if (upgrades.containsKey(player)) {
      upgrades.remove(player);
    }
  }


  
  public static boolean hasFlight(Player player) { return flight.containsKey(player); }


  
  public static void enableFlight(Player player) {
    if (!flight.containsKey(player)) {
      flight.put(player, Boolean.valueOf(true));
    }
  }

  
  public static void disableFlight(Player player) {
    if (flight.containsKey(player)) {
      flight.remove(player);
    }
  }

  
  public static void startSpecialFlight(Player player) {
    if (!player.getAllowFlight()) {
      player.setAllowFlight(true);
    }
    if (player.isFlying()) {
      player.setFlying(false);
    }
  }


}
