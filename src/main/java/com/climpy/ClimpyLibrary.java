package com.climpy;

import com.climpy.config.FileConfig;
import com.climpy.essentials.*;
import com.climpy.listeners.*;
import com.climpy.managers.LogManager;
import com.climpy.modules.discord.CommandHandler;
import com.climpy.modules.discord.api.DiscordAPI;
import com.climpy.modules.discord.commands.ServerInfoCommand;
import com.climpy.modules.discord.events.MessageEvent;
import com.climpy.profile.ProfilePlugin;
import com.climpy.profile.rank.RankType;
import com.climpy.profile.user.User;
import com.climpy.title.TitleAPI;
import com.climpy.tnt.AnimatedTNT;
import com.climpy.tools.signcolor.Converter;
import com.climpy.utils.ColorUtils;
import com.climpy.utils.Lag;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Getter
public class ClimpyLibrary extends JavaPlugin {
    private static ClimpyLibrary instance;
    public static ClimpyLibrary getInstance() { return instance; }

    public FileConfig mainConfig;
    public LogManager logManager;
    public DiscordAPI discordAPI;

    private FreezeListener freezeListener;
    private StaffModeListener staffModeListener;

    public static boolean enabled = false;
    public static List<?> worlds;

    public void onEnable() {
        instance = this;

        Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeChannelListener());
        getInstance().setupConfig();

        getInstance().registerListener();
        getInstance().registerCommands();
        getInstance().registerManagers();
        getInstance().registerEvents();

        //TODO: Discord bot'unu açar, ancak bungeecord destekli bir sistem daha stabildir.
    //    setupDiscord();

        Bukkit.getServer().getScheduler().scheduleSyncRepeatingTask(this, new Lag(), 100L, 1L);
        worlds = getInstance().getConfig().getList("tnt.deny-worlds");

        enabled = true;

        startLog();

    }

    public void setupDiscord() {
        DiscordAPI bot = new DiscordAPI().create("NjQwNjEzNDkzMzI1NjkyOTg3.Xdj_Sw.px4wdJTcOzPuT46R7tzXEDkgZ40").
                setGame(ClimpyLibrary.getInstance().getConfig().getString("discord.game"))
                .complete();

        bot.getJda().addEventListener(new MessageEvent());

        registerBotCommands();

        System.out.println("== ClimpyLibrary == DISCORD BOTU AKTIF EDILDI!");
    }

    public void registerListener() {
        Bukkit.getServer().getPluginManager().registerEvents(new BungeeChannelListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new TitleAPI(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new Converter(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new JoinListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new ServerMotdListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new QuitListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new DieEvent(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new CrashProtectionListener(), this);
        Bukkit.getServer().getPluginManager().registerEvents(new CommandPreprocessFixListener(), this);
        getServer().getPluginManager().registerEvents(new AnimatedTNT(this), this);


        this.freezeListener = new FreezeListener();
        Bukkit.getServer().getPluginManager().registerEvents(this.freezeListener, this);
        this.staffModeListener = new StaffModeListener();
        Bukkit.getServer().getPluginManager().registerEvents(this.staffModeListener, this);
    }

    public void registerCommands() {
        getCommand("broadcast").setExecutor(new BroadcastCommand());
        getCommand("clearchat").setExecutor(new ClearChatCommand());
        getCommand("fly").setExecutor(new FlyCommand());
        getCommand("gamemode").setExecutor(new GamemodeCommand());
        getCommand("ping").setExecutor(new PingCommand());
        getCommand("day").setExecutor(new TimeCommand());
        getCommand("night").setExecutor(new TimeCommand());
        getCommand("world").setExecutor(new WorldTeleportCommand());
        getCommand("op").setExecutor(new OPCommand());
        getCommand("deop").setExecutor(new OPCommand());
        getCommand("opsifre").setExecutor(new OPCommand());
        getCommand("teleport").setExecutor(new TeleportCommand());
        getCommand("teleportall").setExecutor(new TeleportCommand());
        getCommand("tphere").setExecutor(new TeleportCommand());
        getCommand("tppos").setExecutor(new TeleportCommand());
        getCommand("setslot").setExecutor(new SetSlotCommand());
        getCommand("heal").setExecutor(new HealCommand());
        getCommand("kill").setExecutor(new KillCommand());
        getCommand("invsee").setExecutor(new InvseeCommand());
        getCommand("freeze").setExecutor(new FreezeCommand());
        getCommand("inspect").setExecutor(new InspectCommand());
        getCommand("staff").setExecutor(new StaffModeCommand());
        getCommand("list").setExecutor(new ListCommand());
        getCommand("server").setExecutor(new ServerChangeCommand());
        getCommand("hub").setExecutor(new HubCommand());
    }


    public void registerManagers() {
        this.logManager = new LogManager(this);
    }

    public void registerEvents() {

    }

    private void startLog() {
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "   _____  _  _                           _      _  _                              ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "  /  __ \\| |(_)                         | |    (_)| |                             ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "  | /  \\/| | _  _ __ ___   _ __   _   _ | |     _ | |__   _ __  __ _  _ __  _   _ ");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "  | |    | || || '_ ` _ \\ | '_ \\ | | | || |    | || '_ \\ | '__|/ _` || '__|| | | |");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "  | \\__/\\| || || | | | | || |_) || |_| || |____| || |_) || |  | (_| || |   | |_| |");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "   \\____/|_||_||_| |_| |_|| .__/  \\__, |\\_____/|_||_.__/ |_|   \\__,_||_|    \\__, |");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "                          | |      __/ |                                     __/ |");
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA + "                          |_|     |___/                                     |___/ ");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("                              PLUGIN AKTIF EDILDI                              ");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("");
        Bukkit.getConsoleSender().sendMessage("");
    }

    public void onDisable() {
        System.out.println("[ClimpyLibrary] - Plugin de-aktif!");

        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getName());
            if (this.staffModeListener.isStaffModeActive(player)) {

                this.staffModeListener.setStaffMode(player, false);
                player.sendMessage((new ColorUtils()).translateFromString("&cPersonel modu kapatılıyor, sunucu bir operatör tarafından yeniden başlatılıyor.."));
            }

            if(user.getRankType().isAboveOrEqual(RankType.MOD) || player.isOp()){
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&4Sunucu yenileniyor, yetkili olduğunuz için oyundan atılmadınız."));
            } else {
                String msgKick = ChatColor.translateAlternateColorCodes('&', "&cAz önceki bağlı olduğunuz sunucu yeniden başlatılıyor veya kapatılıyor.");
                player.kickPlayer(msgKick);
            }
        }
    }

    public void setupConfig() {

        this.mainConfig = new FileConfig(this, "config.yml");
    }

    private static void registerBotCommands() {
        CommandHandler commandHandler = new CommandHandler();

        commandHandler.registerCommand(new ServerInfoCommand());
    }

    public String getPrefix(Player player) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getName());
        return user.getRankType().getPrefix();
    }

    public String getSuffix(Player player) {
        User user = ProfilePlugin.getInstance().getUserManager().getUser(player.getName());
        return user.getRankType().getSuffix();
    }

    public FreezeListener getFreezeListener() { return this.freezeListener; }
    public StaffModeListener getStaffModeListener() { return this.staffModeListener; }
}