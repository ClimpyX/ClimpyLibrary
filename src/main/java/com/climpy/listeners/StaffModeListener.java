package com.climpy.listeners;

import com.climpy.ClimpyLibrary;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.utils.ColorUtils;
import com.climpy.utils.TimeUtils;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class StaffModeListener implements Listener {
  private final ClimpyLibrary climpyLibrary = ClimpyLibrary.getInstance();
  
  private final Set<UUID> staffMode = new HashSet();
  private final Set<UUID> staffChat = new HashSet();
  private final Set<UUID> vanished = new HashSet();
  
  private final HashMap<UUID, UUID> inspectedPlayer = new HashMap();
  private final HashMap<UUID, ItemStack[]> contents = new HashMap();
  private final HashMap<UUID, ItemStack[]> armorContents = new HashMap();

  public Player getInspectedPlayer(Player player) { return Bukkit.getServer().getPlayer((UUID)this.inspectedPlayer.get(player.getUniqueId())); }
  
  public boolean isVanished(Player player) { return this.vanished.contains(player.getUniqueId()); }

  public boolean isStaffChatActive(Player player) { return this.staffChat.contains(player.getUniqueId()); }

  public boolean isStaffModeActive(Player player) { return this.staffMode.contains(player.getUniqueId()); }
  
  public boolean hasPreviousInventory(Player player) { return (this.contents.containsKey(player.getUniqueId()) && this.armorContents.containsKey(player.getUniqueId())); }
  
  public void saveInventory(Player player) {
    this.contents.put(player.getUniqueId(), player.getInventory().getContents());
    this.armorContents.put(player.getUniqueId(), player.getInventory().getArmorContents());
  }

  public void loadInventory(Player player) {
    PlayerInventory playerInventory = player.getInventory();
    playerInventory.setContents((ItemStack[])this.contents.get(player.getUniqueId()));
    playerInventory.setArmorContents((ItemStack[])this.armorContents.get(player.getUniqueId()));
    this.contents.remove(player.getUniqueId());
    this.armorContents.remove(player.getUniqueId());
  }
  
  public void setStaffChat(Player player, boolean status) {
    if (status) {
      User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getName());
      if ((!user.getRankType().isAboveOrEqual(RankType.MOD))) {
        this.staffChat.add(player.getUniqueId());
      }
    }
    else {
      
      this.staffChat.remove(player.getUniqueId());
    } 
  }
  
  public void setStaffMode(Player player, boolean status) {
    User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getName());
    if (status) {
      if ((user.getRankType().isAboveOrEqual(RankType.MOD))) {
        this.staffMode.add(player.getUniqueId());
        saveInventory(player);
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setArmorContents(new ItemStack[] { new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR) });
        playerInventory.clear();
        setItems(player);
        setVanished(player, true);
        player.updateInventory();
        if (player.getGameMode() == GameMode.SURVIVAL) {
          player.setGameMode(GameMode.CREATIVE);
        }
      }
      else {
        player.sendMessage((new ColorUtils()).translateFromString("&cPersonel modunu etkinleştirme izniniz yok."));
      }
    
    } else {
      
      this.staffMode.remove(player.getUniqueId());
      PlayerInventory playerInventory = player.getInventory();
      playerInventory.setArmorContents(new ItemStack[] { new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR) });
      playerInventory.clear();
      setVanished(player, false);
      if (hasPreviousInventory(player))
      {
        loadInventory(player);
      }
      player.updateInventory();
      if (!user.getRankType().isAboveOrEqual(RankType.MOD) && player.getGameMode() == GameMode.CREATIVE) {
        player.setGameMode(GameMode.SURVIVAL);
      }
    } 
  }

  
  public void setVanished(Player player, boolean status) {
    User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getName());
    if (status) {
      this.vanished.add(player.getUniqueId());
      for (Player online : Bukkit.getServer().getOnlinePlayers()) {
        if (!user.getRankType().isAboveOrEqual(RankType.MOD)) {
          online.hidePlayer(player);
        }
      } 
      
      if (isStaffModeActive(player)) {
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setItem(7, getVanishItemFor(player));
      }
    
    } else {
      this.vanished.remove(player.getUniqueId());
      for (Player online : Bukkit.getServer().getOnlinePlayers()) {
        online.showPlayer(player);
      }
      
      if (isStaffModeActive(player)) {
        PlayerInventory playerInventory = player.getInventory();
        playerInventory.setItem(7, getVanishItemFor(player));
      } 
    } 
  }

  
  public void setItems(Player player) {
    PlayerInventory playerInventory = player.getInventory();
    
    ItemStack compass = new ItemStack(Material.COMPASS, 1);
    ItemMeta compassMeta = compass.getItemMeta();
    compassMeta.setDisplayName((new ColorUtils()).translateFromString("&bIşınlanma Pusulası"));
    compassMeta.setLore((new ColorUtils()).translateFromArray(Arrays.asList(new String[] { "&7Sağ tıklama: İlerleyin.", "&7Sol tık: Görüş hattında bloğa taşır." })));
    compass.setItemMeta(compassMeta);
    
    ItemStack book = new ItemStack(Material.BOOK, 1);
    ItemMeta bookMeta = book.getItemMeta();
    bookMeta.setDisplayName((new ColorUtils()).translateFromString("&bİnceleme Aleti"));
    bookMeta.setLore((new ColorUtils()).translateFromArray(Arrays.asList(new String[] { "&7Envanteri incelemek için oyuncuya sağ tıklayın." })));
    book.setItemMeta(bookMeta);
    
    ItemStack woodAxe = new ItemStack(Material.WOOD_AXE, 1);
    ItemMeta woodAxeMeta = woodAxe.getItemMeta();
    woodAxeMeta.setDisplayName((new ColorUtils()).translateFromString("&bWorldEdit Baltası"));
    woodAxeMeta.setLore((new ColorUtils()).translateFromArray(Arrays.asList(new String[] { "&7Sol tıklama bloğu: "," & 7Bir birinci pozisyonu seçin" })));
    woodAxe.setItemMeta(woodAxeMeta);
    
    ItemStack carpet = new ItemStack(Material.CARPET, 1, (short)3);
    ItemMeta carpetMeta = carpet.getItemMeta();
    carpetMeta.setDisplayName((new ColorUtils()).translateFromString("&bDaha iyi Görüş"));
    carpetMeta.setLore((new ColorUtils()).translateFromArray(Arrays.asList(new String[] { "&7Hedefinizdeki daha iyi bir vizyon bulmak için kullanılır" })));
    carpet.setItemMeta(carpetMeta);
    
    ItemStack diamondPickaxe = new ItemStack(Material.DIAMOND_PICKAXE, 1);
    ItemMeta diamondPickaxeMeta = diamondPickaxe.getItemMeta();
    diamondPickaxeMeta.setDisplayName((new ColorUtils()).translateFromString("&bXraycı Bulucu"));
    diamondPickaxeMeta.setLore((new ColorUtils()).translateFromArray(Arrays.asList(new String[] { "&7..." })));
    diamondPickaxe.setItemMeta(diamondPickaxeMeta);
    
    ItemStack record10 = new ItemStack(Material.RECORD_10, 1);
    ItemMeta record10Meta = record10.getItemMeta();
    record10Meta.setDisplayName((new ColorUtils()).translateFromString("&bRastgele Işınlanma"));
    record10Meta.setLore((new ColorUtils()).translateFromArray(Arrays.asList(new String[] { "&7Rasgele bir oynatıcıya ışınlanmak için sağ tıklayın." })));
    record10.setItemMeta(record10Meta);
    
    playerInventory.setItem(0, compass);
    playerInventory.setItem(1, book);
    playerInventory.setItem(2, woodAxe);
    playerInventory.setItem(3, carpet);
    playerInventory.setItem(6, diamondPickaxe);
    playerInventory.setItem(7, getVanishItemFor(player));
    playerInventory.setItem(8, record10);
  }

  
  private ItemStack getVanishItemFor(Player player) {
    ItemStack inkSack = null;
    
    if (isVanished(player)) {
      
      inkSack = new ItemStack(Material.INK_SACK, 1, (short)5);
      ItemMeta inkSackMeta = inkSack.getItemMeta();
      inkSackMeta.setDisplayName((new ColorUtils()).translateFromString("&aGörünmezlik: &aAçık"));
      inkSackMeta.setLore((new ColorUtils()).translateFromArray(Arrays.asList(new String[] { "&7Kaybolma durumunuzu güncellemek için sağ tıklayın." })));
      inkSack.setItemMeta(inkSackMeta);
    }
    else {
      
      inkSack = new ItemStack(Material.INK_SACK, 1, (short)9);
      ItemMeta inkSackMeta = inkSack.getItemMeta();
      inkSackMeta.setDisplayName((new ColorUtils()).translateFromString("&aGörünmezlik: &cKapalı"));
      inkSackMeta.setLore((new ColorUtils()).translateFromArray(Arrays.asList(new String[] { "&7Kaybolma durumunuzu güncellemek için sağ tıklayın." })));
      inkSack.setItemMeta(inkSackMeta);
    } 
    
    return inkSack;
  }

  
  public void openInspectionMenu(final Player player, final Player target) {
    final Inventory inventory = Bukkit.getServer().createInventory(null, 54, (new ColorUtils()).translateFromString("&eİnceleniyor: &a" + ClimpyLibrary.getInstance().getPrefix(target) + target.getName()));
    
    (new BukkitRunnable()
      {
        
        public void run()
        {
          StaffModeListener.this.inspectedPlayer.put(player.getUniqueId(), target.getUniqueId());
          
          PlayerInventory playerInventory = target.getInventory();
          
          ItemStack speckledMelon = new ItemStack(Material.SPECKLED_MELON, (int)target.getHealth());
          ItemMeta speckledMelonMeta = speckledMelon.getItemMeta();
          speckledMelonMeta.setDisplayName((new ColorUtils()).translateFromString("&aSağlık"));
          speckledMelon.setItemMeta(speckledMelonMeta);
          
          ItemStack cookedBeef = new ItemStack(Material.COOKED_BEEF, target.getFoodLevel());
          ItemMeta cookedBeefMeta = cookedBeef.getItemMeta();
          cookedBeefMeta.setDisplayName((new ColorUtils()).translateFromString("&aAçlık"));
          cookedBeef.setItemMeta(cookedBeefMeta);
          
          ItemStack brewingStand = new ItemStack(Material.BREWING_STAND_ITEM, target.getPlayer().getActivePotionEffects().size());
          ItemMeta brewingStandMeta = brewingStand.getItemMeta();
          brewingStandMeta.setDisplayName((new ColorUtils()).translateFromString("&aAktif Efektler"));
          ArrayList<String> brewingStandLore = new ArrayList<String>();
          for (PotionEffect potionEffect : target.getPlayer().getActivePotionEffects()) {
            
            String effectName = potionEffect.getType().getName();
            int effectLevel = potionEffect.getAmplifier();
            effectLevel++;
            brewingStandLore.add((new ColorUtils()).translateFromString("&e" + WordUtils.capitalizeFully(effectName).replace("_", " ") + " " + effectLevel + "&7: &c" + TimeUtils.IntegerCountdown.setFormat(Integer.valueOf(potionEffect.getDuration() / 20))));
          } 
          brewingStandMeta.setLore(brewingStandLore);
          brewingStand.setItemMeta(brewingStandMeta);
          
          ItemStack compass = new ItemStack(Material.COMPASS, 1);
          ItemMeta compassMeta = compass.getItemMeta();
          compassMeta.setDisplayName((new ColorUtils()).translateFromString("&aOyuncu Konumu"));
          compassMeta.setLore((new ColorUtils()).translateFromArray(Arrays.asList(new String[] { "&eDünya&7: &a" + player.getWorld().getName(), "&eKordinatlar ", "  &eX&7: &c" + target.getLocation().getBlockX(), "  &eY&7: &c" + target.getLocation().getBlockY(), "  &eZ&7: &c" + target.getLocation().getBlockZ() })));
          compass.setItemMeta(compassMeta);
          
          ItemStack magmaCream = new ItemStack(Material.MAGMA_CREAM, 1);
          ItemMeta magmaCreamMeta = magmaCream.getItemMeta();
          if (StaffModeListener.this.climpyLibrary.getFreezeListener().isFrozen(target)) {
            
            magmaCreamMeta.setDisplayName((new ColorUtils()).translateFromString("&aDondurma: &aAçık"));
          }
          else {
            
            magmaCreamMeta.setDisplayName((new ColorUtils()).translateFromString("&aDondurma: &cKapalı"));
          } 
          magmaCreamMeta.setLore((new ColorUtils()).translateFromArray(Arrays.asList(new String[] { "&eClick to update freeze status" })));
          magmaCream.setItemMeta(magmaCreamMeta);

          
          ItemStack orangeGlassPane = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)1);
          
          inventory.setContents(playerInventory.getContents());
          inventory.setItem(36, playerInventory.getHelmet());
          inventory.setItem(37, playerInventory.getChestplate());
          inventory.setItem(38, playerInventory.getLeggings());
          inventory.setItem(39, playerInventory.getBoots());
          inventory.setItem(40, playerInventory.getItemInHand());
          for (int i = 41; i <= 46; i++)
          {
            inventory.setItem(i, orangeGlassPane);
          }
          inventory.setItem(47, speckledMelon);
          inventory.setItem(48, cookedBeef);
          inventory.setItem(49, brewingStand);
          inventory.setItem(50, compass);
          inventory.setItem(51, magmaCream);
          for (int i = 52; i <= 53; i++)
          {
            inventory.setItem(i, orangeGlassPane);
          }
          
          if (!player.getOpenInventory().getTitle().equals((new ColorUtils()).translateFromString("&aİnceleniyor: &f" + ClimpyLibrary.getInstance().getPrefix(target) + target.getName()))) {
            
            cancel();
            StaffModeListener.this.inspectedPlayer.remove(player.getUniqueId());
          } 
        }
      }).runTaskTimer(this.climpyLibrary, 0L, 5L);
    
    player.openInventory(inventory);
  }

  
  @EventHandler
  public void onClickInspectionMenu(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();
    if (event.getInventory().getTitle().contains((new ColorUtils()).translateFromString("&aİnceleniyor: "))) {
      
      Player inspected = getInspectedPlayer(player);
      if (event.getRawSlot() == 51)
      {
        if (inspected != null)
        {
          if (this.climpyLibrary.getFreezeListener().isFrozen(inspected)) {
            
            this.climpyLibrary.getFreezeListener().setFreeze(player, inspected, false);
          }
          else {
            
            this.climpyLibrary.getFreezeListener().setFreeze(player, inspected, true);
          } 
        }
      }
    } 
  }

  
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    Player player = (Player)event.getWhoClicked();
    if (isStaffModeActive(player))
    {
      event.setCancelled(true);
    }
    if (event.getClickedInventory() != null && event.getClickedInventory().getTitle().contains((new ColorUtils()).translateFromString("&aİnceleniyor: ")))
    {
      event.setCancelled(true);
    }
  }

  
  @EventHandler
  public void onPlayerInteractBook(PlayerInteractEntityEvent event) {
    if (event.getRightClicked() != null && event.getRightClicked() instanceof Player) {
      Player player = event.getPlayer();
      User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getName());
      
      if (isStaffModeActive(player) && (!player.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD))) {
        
        ItemStack itemStack = player.getItemInHand();
        if (itemStack != null && itemStack.getType() == Material.BOOK) {
          
          Player target = (Player)event.getRightClicked();
          if (target != null && !player.getName().equals(target.getName())) {
            
            openInspectionMenu(player, target);
            player.sendMessage((new ColorUtils()).translateFromString("&aŞimdi envanterini inceliyorsunuz: &f" + ClimpyLibrary.getInstance().getPrefix(target) + target.getName() + "&a."));
          } 
        } 
      } 
    } 
  }

  
  @EventHandler
  public void onPlayerInteractXrayerFinderItem(PlayerInteractEvent event) {
    Action action = event.getAction();
    if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
      
      Player player = event.getPlayer();
      User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getName());

      if (isStaffModeActive(player) && (!player.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD))) {
        
        ItemStack itemStack = player.getItemInHand();
        if (itemStack != null && itemStack.getType() == Material.DIAMOND_PICKAXE)
        {
          player.sendMessage((new ColorUtils()).translateFromString("&cBu özellik şu anda geliştirilme aşamasındadır."));
        }
      } 
    } 
  }

  
  @EventHandler
  public void onPlayerInteractVanishItem(PlayerInteractEvent event) {
    Action action = event.getAction();
    if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {

      Player player = event.getPlayer();
      User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getName());

      if (isStaffModeActive(player) && (!player.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD))) {
        
        ItemStack itemStack = player.getItemInHand();
        if (itemStack != null && itemStack.getType() == Material.INK_SACK)
        {
          if (isVanished(player)) {
            
            setVanished(player, false);
          }
          else {
            
            setVanished(player, true);
          } 
        }
      } 
    } 
  }

  
  @EventHandler
  public void onPlayerInteractRandomTeleportItem(PlayerInteractEvent event) {
    Action action = event.getAction();
    if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
      
      Player player = event.getPlayer();
      User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getName());

      if (isStaffModeActive(player) && (!player.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD))) {
        
        ItemStack itemStack = player.getItemInHand();
        if (itemStack != null && itemStack.getType() == Material.RECORD_10)
        {
          if (Bukkit.getServer().getOnlinePlayers().size() > 1) {
            
            Random random = new Random();
            int size = random.nextInt(Bukkit.getServer().getOnlinePlayers().size());
            Player online = (Player)Bukkit.getServer().getOnlinePlayers().toArray()[size];
            if (online.equals(player)) {
              
              random.nextInt();
              onPlayerInteractRandomTeleportItem(event);
              return;
            } 
            event.setCancelled(true);
            player.teleport(online);
            player.sendMessage((new ColorUtils()).translateFromString("&aRastgele bir şekilde ışınlandınız, oyuncu; &f" + online.getName() + "&a."));
          }
          else {
            
            player.sendMessage((new ColorUtils()).translateFromString("&cTeleport yapacak oyuncular bulunamadı."));
          } 
        }
      } 
    } 
  }

  
  @EventHandler
  public void onPlayerJoin(PlayerJoinEvent event) {
    Player player = event.getPlayer();
    User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getName());
    if (!user.getRankType().isAboveOrEqual(RankType.MOD) && player.getGameMode() == GameMode.CREATIVE)
    {
      player.setGameMode(GameMode.CREATIVE);
    }
    if (!(!player.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD)) && this.vanished.size() > 0) {
      for (UUID uuid : this.vanished) {
        
        Player vanishedPlayer = Bukkit.getServer().getPlayer(uuid);
        if (vanishedPlayer != null)
        {
          player.hidePlayer(vanishedPlayer);
        }
      } 
    }
  }

  
  @EventHandler
  public void onPlayerQuit(PlayerQuitEvent event) {
    Player player = event.getPlayer();
    User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getName());

    if (isStaffModeActive(player)) {
      this.staffMode.remove(player.getUniqueId());
      this.inspectedPlayer.remove(player.getUniqueId());
      PlayerInventory playerInventory = player.getInventory();
      playerInventory.setArmorContents(new ItemStack[] { new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR), new ItemStack(Material.AIR) });
      playerInventory.clear();
      setVanished(player, false);
      if (hasPreviousInventory(player)) {
        loadInventory(player);
      }
      if (!user.getRankType().isAboveOrEqual(RankType.MOD) && player.getGameMode() == GameMode.CREATIVE)
      {
        player.setGameMode(GameMode.SURVIVAL);
      }
    } 
  }

  
  @EventHandler
  public void onPlayerDropItem(PlayerDropItemEvent event) {
    Player player = event.getPlayer();
    if (isStaffModeActive(player))
    {
      event.setCancelled(true);
    }
  }

  
  @EventHandler
  public void onPlayerPickupItem(PlayerPickupItemEvent event) {
    Player player = event.getPlayer();
    if (isVanished(player) || isStaffModeActive(player))
    {
      event.setCancelled(true);
    }
  }

  
  @EventHandler
  public void onBlockPlace(BlockPlaceEvent event) {
    Player player = event.getPlayer();
    Block block = event.getBlock();
    if (isVanished(player) || isStaffModeActive(player))
    {
      if (block != null)
      {
        event.setCancelled(true);
      }
    }
  }

  
  @EventHandler
  public void onBlockBreak(BlockBreakEvent event) {
    Player player = event.getPlayer();
    Block block = event.getBlock();
    if (isVanished(player) || isStaffModeActive(player))
    {
      if (block != null)
      {
        event.setCancelled(true);
      }
    }
  }

  
  @EventHandler
  public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
      
      Player player = (Player)event.getDamager();
      User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getName());

      if (isVanished(player) || (isStaffModeActive(player) && (!player.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD)))) {
        event.setCancelled(true);
      }
    }
    else if (event.getEntity() instanceof org.bukkit.entity.LivingEntity && event.getDamager() instanceof Player) {
      
      Player player = (Player)event.getDamager();
      User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getName());
      if (isVanished(player) || (isStaffModeActive(player) && (!player.isOp() && !user.getRankType().isAboveOrEqual(RankType.MOD)))) {
        event.setCancelled(true);
      }
    } 
  }
}
