package com.climpy.modules.discord.commands;

import com.climpy.modules.Command;
import com.climpy.modules.discord.CommandParser;
import com.climpy.utils.Lag;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.TextChannel;
import org.bukkit.Bukkit;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ServerInfoCommand extends Command {
    private List<Message> messageList;
    private String usedCommand;

    public ServerInfoCommand() { super("serverinfo", "server info", new String[] { "server", "sunucu" }); }

    @Override
    public String getUsage() {
        return null;
    }

    @Override
    public void onChannelCommand(CommandParser context) {
        TextChannel textChannel = context.getChannel();
        Message senderMessage = context.getMessage();
        MessageChannel messageChannel = context.getChannel();


        //  if (senderMessage.getAuthor().isBot()) return;
        if (!senderMessage.getMember().hasPermission(Permission.MESSAGE_MANAGE)) {
            messageChannel.sendMessage(
                    "İzine sahip değilsin."
            ).complete().delete().queueAfter(5, TimeUnit.SECONDS);
            senderMessage.delete().queueAfter(5, TimeUnit.SECONDS);
            return;
        }

        textChannel.sendMessage(":chart_with_upwards_trend: Senin için güncel sunucu bilgilerini aktarıyorum..").queue();

        Runtime runtime = Runtime.getRuntime();
        int mb = 1024*1024;

        double tps = Lag.getTPS();
        double lag = Math.round((1.0D - tps / 20.0D) * 100.0D);

        EmbedBuilder embedmsg = new EmbedBuilder();
        embedmsg.setColor(new Color(255, 255, 255));
        embedmsg.setDescription("\n:family_mmgg: Çevrim içi oyuncular: " + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + "\n:stopwatch: Boş hafıza: " + runtime.freeMemory() / mb + "\n:control_knobs: Toplam hafıza: " + runtime.totalMemory() / mb + "\n:gear: Sunucu versiyonu: " + Bukkit.getVersion() + "\n:clock1: TPS: " + lag);
        senderMessage.getChannel().sendMessage(embedmsg.build()).queue();
    }

}
