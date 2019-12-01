package com.climpy.modules.discord.api;

import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.Game;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.TextChannel;

import javax.security.auth.login.LoginException;
import java.util.List;





public class DiscordAPI {
    private JDA jda;
    private String tocken;
    private String name;

        public DiscordAPI create(String tocken) {
        this.tocken = tocken;
        return this;
    }






    public DiscordAPI setGame(String name) {
        this.name = name;
        return this;
    }

    public DiscordAPI updateGame() {
        if (this.name != null && this.jda != null)
            this.jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, this.name));
        return this;
    }





    public DiscordAPI complete() {
        try {
            this.jda = (new JDABuilder(AccountType.BOT)).setToken(this.tocken).buildAsync();
            if (this.name != null)
                this.jda.getPresence().setGame(Game.of(Game.GameType.DEFAULT, this.name));
        } catch (LoginException|IllegalArgumentException e) {
            e.printStackTrace();
        }
        return this;
    }




    public void logout() {
        if (this.jda != null) {
            this.jda.shutdown();
        }
    }






    public Message sendMessage(long channelId, String message) {
        if (this.jda == null) {
            System.out.println("le jda est null");
            return null;
        }
        TextChannel c = this.jda.getTextChannelById(channelId);
        if (c == null) {
            System.out.println("le channel est null ");
            return null;
        }
        return (Message)c.sendMessage(message).complete();
    }

    public List<TextChannel> getChannels() {
        if (this.jda == null) return null;
        return this.jda.getTextChannels();
    }







    public Message sendMessage(long channelId, EmbedBuilder builder) {
        if (this.jda == null) {
            System.out.println("le jda est null");
            return null;
        }
        return (Message)this.jda.getTextChannelById(channelId).sendMessage(builder.build()).complete();
    }





    public JDA getJda() { return this.jda; }






    public String getTocken() { return this.tocken; }






    public String getName() { return this.name; }
}
